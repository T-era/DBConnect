package ytel.dbconnect.model;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import ytel.dbconnect.config.Configure;
import ytel.dbconnect.model.database.DataBaseInfo;
import ytel.dbconnect.model.database.QueryExecuterFactory;
import ytel.dbconnect.model.database.QueryExecuter;
import ytel.dbconnect.model.database.queryresultview.ErrorViewInfo;
import ytel.dbconnect.model.io.CsvWriter;
import ytel.dbconnect.model.io.SQLReaderFactory;
import ytel.dbconnect.model.io.SQLWriterFactory;

/**
 * DBConnect���W���[���p�[�g�̎����ł��B
 * <p>
 * {@link MainModule}��ʂ��đ�����e��̗v�����A�e���W���[���ɒ��p���܂��B
 * 
 * @author y-terada
 *
 */
public class MainModuleImpl implements MainModule{
	private final ConnectionFactoryImpl connectionFactory;
	private final MessageObserver messageObserver;
	private final Configure config;

	/**
	 * �ڑ������w�肵�āA�C���X�^���X�𐶐����܂��B
	 * @param driverClassName JDBC�h���C�o��
	 * @param connectionCheckSql �ڑ��m�F�pSQL 
	 * @param messageObserver �C���X�^���X�����甭�����郁�b�Z�[�W���Ď�����I�u�U�[�o
	 */
	public MainModuleImpl(String driverClassName, String connectionCheckSql, MessageObserver messageObserver, Configure config) {
		this.messageObserver = messageObserver;
		this.config = config;
		this.connectionFactory = new ConnectionFactoryImpl(driverClassName, connectionCheckSql, messageObserver);
	}

	@Override
	public QueryResultView getTableInfo() throws ActionInterruptException{
		DataBaseInfo dbi = new DataBaseInfo(connectionFactory, this.getConfigure());
		try{
			return dbi.getTableInfo();
		} catch (SQLException e) {
			messageObserver.setErrorMessage(e.getMessage());
			return ErrorViewInfo.getInstance(this);
		}
	}

	@Override
	public QueryResultView getColumnInfo(String tableName) throws ActionInterruptException{
		DataBaseInfo dbi = new DataBaseInfo(connectionFactory, this.getConfigure());
		try{
			return dbi.getColumnInfo(tableName);
		} catch (SQLException e) {
			messageObserver.setErrorMessage(e.getMessage());
			return ErrorViewInfo.getInstance(this);
		}
	}

	@Override
	public void setDataBaseInfo(String url, String userName, String password, boolean autoCommit, boolean readOnly) {
		connectionFactory.setConnectInfo(url, userName, password, autoCommit, readOnly);
	}

	@Override
	public void refleshConnection() throws ActionInterruptException{
		try {
			connectionFactory.getNewConnection();
		} catch (SQLException e) {
			messageObserver.setFatalMessage("�R�l�N�V�����̍Ď擾�Ɏ��s���܂����B", e);
			throw new ActionInterruptException("�R�l�N�V�����̍Ď擾�Ɏ��s���܂����B", e);
		}
	}

	@Override
	public int getParameterCount(String sql) throws ActionInterruptException{
		try {
			return QueryExecuterFactory.getParameterCount(connectionFactory, sql);
		} catch (SQLException e){
			messageObserver.setFatalMessage("SQL�p�����[�^�̐����Z�o���邱�Ƃ��ł��܂���ł����B", e);
			throw new ActionInterruptException("SQL�p�����[�^�̐����Z�o���邱�Ƃ��ł��܂���ł����B", e);
		}
	}

	@Override
	public QueryExecuter getQueryExecter(List<String> parameters) {
		return QueryExecuterFactory.getExecuter(connectionFactory, parameters);
	}

	@Override
	public void close() {
		connectionFactory.connectionClose();
	}

	@Override
	public ConnectionInfo getConnectionInfo() {
		return new ConnectionInfo(){
			@Override
			public boolean getAutoCommit() {
				return connectionFactory.getAutoCommit();
			}

			@Override
			public String getPassword() {
				return connectionFactory.getPassword();
			}

			@Override
			public boolean getReadOnly() {
				return connectionFactory.getReadOnly();
			}

			@Override
			public String getUrl() {
				return connectionFactory.getUrl();
			}

			@Override
			public String getUserName() {
				return connectionFactory.getUserName();
			}
		};
	}

	@Override
	public DataBaseInfo getDataBaseInfo() {
		return new DataBaseInfo(connectionFactory, this.getConfigure());
	}

	@Override
	public void commit() throws ActionInterruptException{
		try {
			connectionFactory.getConnection().commit();
		} catch (SQLException e) {
			messageObserver.setFatalMessage("�R�l�N�V�����̃R�~�b�g�Ɏ��s���܂����B", e);
			throw new ActionInterruptException("�R�l�N�V�����̃R�~�b�g�Ɏ��s���܂����B", e);
		}
	}

	@Override
	public void rollback() throws ActionInterruptException{
		try {
			connectionFactory.getConnection().rollback();
		} catch (SQLException e) {
			messageObserver.setFatalMessage("�R�l�N�V�����̃��[���o�b�N�Ɏ��s���܂����B", e);
			throw new ActionInterruptException("�R�l�N�V�����̃��[���o�b�N�Ɏ��s���܂����B", e);
		}
	}

	@Override
	public void addConnectionObserver(ConnectionObserver newObserver) {
		connectionFactory.addObserver(newObserver);
	}

	@Override
	public SQLReader getSeparateSQLReader(File inputFile) throws ActionInterruptException{
		try {
			return SQLReaderFactory.getSeparateSQLReader(inputFile, messageObserver, this.getConfigure());
		} catch (IOException e) {
			messageObserver.setFatalMessage("�t�@�C�����o���Ɏ��s���܂����B", e);
			throw new ActionInterruptException("�t�@�C�����o���Ɏ��s���܂����B", e);
		}
	}

	@Override
	public SQLReader getSingleSQLReader(File inputFile) throws ActionInterruptException{
		try {
			return SQLReaderFactory.getSingleSQLReader(inputFile, messageObserver, this.getConfigure());
		} catch (IOException e) {
			messageObserver.setFatalMessage("�t�@�C�����o���Ɏ��s���܂����B", e);
			throw new ActionInterruptException("�t�@�C�����o���Ɏ��s���܂����B", e);
		}
	}

	@Override
	public SQLWriter getSingleFileSQLWriter(File outputFile) throws ActionInterruptException{
		try{
			return SQLWriterFactory.getSingleFileSQLWriter(outputFile, messageObserver, this.getConfigure());
		} catch (IOException e) {
			messageObserver.setFatalMessage("�t�@�C���Ǎ��݂Ɏ��s���܂����B", e);
			throw new ActionInterruptException("�t�@�C���Ǎ��݂Ɏ��s���܂����B", e);
		}
	}

	@Override
	public boolean checkConnectInfo(
			  String driverName
			, String url
			, String userName
			, String password
			, String connectionCheckSql){
		try {
			ConnectionFactoryImpl.checkConnectInfo(driverName, url, userName, password, connectionCheckSql);
			return true;
		}catch( SQLException e ){
			messageObserver.setErrorMessage(getConfigure().system.getAlertMessage());
			return false;
		} catch (ClassNotFoundException e) {
			messageObserver.setFatalMessage("", e);
			return false;
		}
	}

	@Override
	public DataTableWriter getCsvWriter(File targetFile, boolean takeRecursiveBackup) {
		return new CsvWriter(targetFile, takeRecursiveBackup, messageObserver);
	}

	@Override
	public Configure getConfigure() {
		return config;
	}
}
