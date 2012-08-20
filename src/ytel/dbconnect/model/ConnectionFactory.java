package ytel.dbconnect.model;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * �R�l�N�V�����̎擾/�ێ����Ǘ����܂��B<p/>
 * @author y-terada
 *
 */
public interface ConnectionFactory{
	/**
	 * �ڑ�����ݒ肵�܂��B<br/>
	 * ���̃��\�b�h���Ăяo���ƁA�Â��ڑ����ł�DB�ڑ��͔j������܂��B
	 * @param newUrl �ڑ���DB��URL
	 * @param newUserName �ڑ���DB�̃��[�U��
	 * @param newPassword �ڑ���DB�̃��[�U��
	 * @param newAutoCommit
	 * @param newReadOnly
	 */
	void setConnectInfo( String newUrl, String newUserName, String newPassword, boolean newAutoCommit, boolean newReadOnly );
	/**
	 * �ێ����Ă���R�l�N�V������Ԃ��B<p/>
	 * �ێ����Ă���R�l�N�V�����������Ă���ꍇ�A���̃��\�b�h�̖߂�l��
	 * ����ꂽ�R�l�N�V�����ɂȂ�܂��B<br/>
	 * �R�l�N�V�������擾���Ă��Ȃ��ꍇ�Anull��Ԃ����Ƃ�����܂��B
	 * @return DB�Ƃ̃R�l�N�V����
	 * @throws SQLException
	 */
	Connection getConnection() throws SQLException;

	void connectionCommit() throws SQLException;

	void connectionRollback() throws SQLException;

	/**
	 * �R�l�N�V������ڑ�����B���ɗL���ȃR�l�N�V����������ꍇ�͉����s��Ȃ��B<br/>
	 * �Ȃ��ꍇ�͐V�K�ɐڑ�����B<br/>
	 * �ڑ������R�l�N�V�����́A{@link #getConnection()}�Ŏ擾���܂��B<p/>
	 * @throws SQLException
	 */
	public void getNewConnection() throws SQLException;

	/**
	 * �R�l�N�V�����������I�ɕ��܂��B<br/>
	 * ��O�͖�������܂��B
	 */
	public void connectionClose();
//
//	public String getUrl();
//	public String getUserName();
//	public String getPassword();
//	public boolean getAutoCommit();
//	public boolean getReadOnly();

	/**
	 * �R�l�N�V�����擾�C�x���g���󂯎��I�u�U�[�o��ǉ����܂��B<br/>
	 * ���̃I�u�W�F�N�g���ŃR�l�N�V�����̍Ď擾�������ꍇ�A�I�u�U�[�o��{@link ConnectionObserver#actionPerformed(Connection, ConnectionInfo)}
	 * �C�x���g���󂯎��܂��B
	 * @param newObserver �C�x���g���󂯎��I�u�U�[�o
	 */
	public void addObserver(ConnectionObserver newObserver);
}
