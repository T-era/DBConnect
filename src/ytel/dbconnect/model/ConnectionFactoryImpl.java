package ytel.dbconnect.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * �R�l�N�V�����̎擾/�ێ����Ǘ����܂��B<p/>
 * @author y-terada
 *
 */
final class ConnectionFactoryImpl implements ConnectionFactory{
	private final String driverName;
	private String url;
	private String userName;
	private String password;
	private final String connectionCheckSQL;
	private boolean autoCommit;
	private boolean readOnly;
	private Connection con;
	/**
	 * �R�l�N�V�����擾�C�x���g���󂯎��I�u�U�[�o
	 */
	private ConnectionObserver observer;
	/**
	 * �\�����b�Z�[�W���󂯎��I�u�U�[�o
	 */
	private final MessageObserver messageObserver;

	/**
	 * JDBC�h���C�o���w�肵�ăC���X�^���X�𐶐��s���܂��B
	 * @param driverName JDBC�h���C�o
	 * @param connectionCheckSQL �擾�����R�l�N�V�������������邽�߂�SQL
	 * @param messageObserver ���b�Z�[�W������I�u�U�[�o
	 */
	ConnectionFactoryImpl( String driverName, String connectionCheckSQL, MessageObserver messageObserver ){
		this.driverName = driverName;
		this.connectionCheckSQL = connectionCheckSQL;
		this.messageObserver = messageObserver;
	}

	/**
	 * �ڑ�����ݒ肵�܂��B<br/>
	 * ���̃��\�b�h���Ăяo���ƁA�Â��ڑ����ł�DB�ڑ��͔j������܂��B
	 * @param newUrl �ڑ���DB��URL
	 * @param newUserName �ڑ���DB�̃��[�U��
	 * @param newPassword �ڑ���DB�̃��[�U��
	 * @param newAutoCommit
	 * @param newReadOnly
	 */
	@Override
	public void setConnectInfo( String newUrl, String newUserName, String newPassword, boolean newAutoCommit, boolean newReadOnly ){
		connectionClose();
		con = null;

		url = newUrl;
		userName = newUserName;
		password = newPassword;
		autoCommit = newAutoCommit;
		readOnly = newReadOnly;
	}

	/**
	 * �ێ����Ă���R�l�N�V������Ԃ��B<p/>
	 * �ێ����Ă���R�l�N�V�����������Ă���ꍇ�A���̃��\�b�h�̖߂�l��
	 * ����ꂽ�R�l�N�V�����ɂȂ�܂��B<br/>
	 * �R�l�N�V�������擾���Ă��Ȃ��ꍇ�Anull��Ԃ����Ƃ�����܂��B
	 * @return DB�Ƃ̃R�l�N�V����
	 * @throws SQLException
	 */
	@Override
	public Connection getConnection() throws SQLException{
		return con;
	}

	@Override
	public void connectionCommit() throws SQLException{
		con.commit();
	}

	@Override
	public void connectionRollback() throws SQLException{
		con.rollback();
	}

	/**
	 * �R�l�N�V������ڑ�����B���ɗL���ȃR�l�N�V����������ꍇ�͉����s��Ȃ��B<br/>
	 * �Ȃ��ꍇ�͐V�K�ɐڑ�����B<br/>
	 * �ڑ������R�l�N�V�����́A{@link #getConnection()}�Ŏ擾���܂��B<p/>
	 * �L���ȃR�l�N�V���������邩�ǂ����̔���́A{@link #isActiveConnection()}��p���܂��B
	 * @throws SQLException
	 */
	@Override
	public void getNewConnection() throws SQLException{
		if( con != null && ! isActiveConnection() ){
			connectionClose();
		}

		try{
			Class.forName( driverName );
			con = DriverManager.getConnection( url, userName, password );
			try{
				con.setAutoCommit( autoCommit );
				con.setReadOnly( readOnly );
			}catch( SQLException e ){
				connectionClose();
			}
		}catch( ClassNotFoundException e ){
			con = null;
			messageObserver.setFatalMessage("DriverName���擾������AExecQuery.checkConnectInfo()�Ŋm�F����ׂ��B", e);
			throw new SQLException(e);
		}catch( SQLException e ){
			con = null;
			messageObserver.setFatalMessage("�R�l�N�V�����̎擾�Ɏ��s", e);
			throw e;
		}
		if (this.observer != null){
			this.observer.callAction(con, new ConnectionInfo(){
				@Override
				public boolean getAutoCommit() {
					return autoCommit;
				}
				@Override
				public String getPassword() {
					return password;
				}
				@Override
				public boolean getReadOnly() {
					return readOnly;
				}
				@Override
				public String getUrl() {
					return url;
				}
				@Override
				public String getUserName() {
					return userName;
				}
			});
		}
		if( ! isActiveConnection() ){
			throw new SQLException("�擾�����R�l�N�V�������g�p�ł��Ȃ��B");
		}
	}

	/**
	 * ����SQL���g���āA�R�l�N�V�������L���ł��邩���肵�܂��B<br/>
	 * �����ɂ́A{@link #connectionCheckSQL}���g�p���܂��B
	 * @return �L���ȏꍇtrue
	 */
	private boolean isActiveConnection(){
		try{
			Statement stmt = con.createStatement();
			try{
				stmt.execute( connectionCheckSQL );
			}finally{
				stmt.close();
			}
		}catch( SQLException e ){
			return false;
		}
		return true;
	}

	/**
	 * �R�l�N�V�����������I�ɕ��܂��B<br/>
	 * ��O�͖�������܂��B
	 */
	@Override
	public void connectionClose(){
		if( con == null ){
			return;
		}
		try{
			try{
				con.rollback();
			}finally{
				con.close();
			}
		}catch( SQLException e ){
			messageObserver.setErrorMessage("", e);
		}
	}

	/**
	 * ���̐ڑ��̐ڑ���URL��Ԃ��܂��B
	 * @return �ڑ���URL
	 */
	String getUrl(){
		return url;
	}
	/**
	 * ���̐ڑ���DB���[�U����Ԃ��܂��B
	 * @return DB���[�U��
	 */
	String getUserName(){
		return userName;
	}
	/**
	 * ���̐ڑ���DB�p�X���[�h��Ԃ��܂��B
	 * @return DB�p�X���[�h
	 */
	String getPassword(){
		return password;
	}
	/**
	 * ���̐ڑ��̎����R�~�b�g�ݒ��Ԃ��܂��B
	 * @return �����R�~�b�g�ݒ�
	 */
	boolean getAutoCommit(){
		return autoCommit;
	}
	/**
	 * ���̐ڑ��̓Ǎ��ݐ�p�ݒ��Ԃ��܂��B
	 * @return �Ǎ��ݐ�p�ݒ�
	 */
	boolean getReadOnly(){
		return readOnly;
	}

	/**
	 * �R�l�N�V�����擾�C�x���g���󂯎��I�u�U�[�o��ǉ����܂��B
	 * @param newObserver
	 */
	@Override
	public void addObserver(ConnectionObserver newObserver){
		if (this.observer == null){
			this.observer = newObserver;
		} else {
			this.observer.add(newObserver);
		}
	}

	/**
	 * �w�肳�ꂽ�ڑ���񂪗L���ł��邩�ǂ������m�F���܂��B<p/>
	 * ���̎����ł́A���ۂɃR�l�N�V�������擾���A�`�F�b�NSQL�����s���Ă݂邱�ƂŊm�F���܂��B
	 * ���s�����ꍇ�͂��ׂė�O�ƂȂ�܂��B
	 * @param driverName JDBC�h���C�o
	 * @param url �ڑ���DB��URL
	 * @param userName DB�̃��[�U��
	 * @param password DB�̃p�X���[�h
	 * @param connectionCheckSQL �`�F�b�NSQL
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void checkConnectInfo(
			  String driverName
			, String url
			, String userName
			, String password
			, String connectionCheckSQL ) throws SQLException, ClassNotFoundException{
		Class.forName( driverName );
		try{
			Connection con = DriverManager.getConnection( url, userName, password );
			try{
				con.setReadOnly( true );
			}catch( SQLException e ){
				con.close();
				throw e;
			}
		}catch( SQLException eOut ){
			throw eOut;
		}
	}
}
