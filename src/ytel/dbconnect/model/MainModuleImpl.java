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
 * DBConnectモジュールパートの実装です。
 * <p>
 * {@link MainModule}を通じて送られる各種の要求を、各モジュールに中継します。
 * 
 * @author y-terada
 *
 */
public class MainModuleImpl implements MainModule{
	private final ConnectionFactoryImpl connectionFactory;
	private final MessageObserver messageObserver;
	private final Configure config;

	/**
	 * 接続情報を指定して、インスタンスを生成します。
	 * @param driverClassName JDBCドライバ名
	 * @param connectionCheckSql 接続確認用SQL 
	 * @param messageObserver インスタンス内から発生するメッセージを監視するオブザーバ
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
			messageObserver.setFatalMessage("コネクションの再取得に失敗しました。", e);
			throw new ActionInterruptException("コネクションの再取得に失敗しました。", e);
		}
	}

	@Override
	public int getParameterCount(String sql) throws ActionInterruptException{
		try {
			return QueryExecuterFactory.getParameterCount(connectionFactory, sql);
		} catch (SQLException e){
			messageObserver.setFatalMessage("SQLパラメータの数を算出することができませんでした。", e);
			throw new ActionInterruptException("SQLパラメータの数を算出することができませんでした。", e);
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
			messageObserver.setFatalMessage("コネクションのコミットに失敗しました。", e);
			throw new ActionInterruptException("コネクションのコミットに失敗しました。", e);
		}
	}

	@Override
	public void rollback() throws ActionInterruptException{
		try {
			connectionFactory.getConnection().rollback();
		} catch (SQLException e) {
			messageObserver.setFatalMessage("コネクションのロールバックに失敗しました。", e);
			throw new ActionInterruptException("コネクションのロールバックに失敗しました。", e);
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
			messageObserver.setFatalMessage("ファイル書出しに失敗しました。", e);
			throw new ActionInterruptException("ファイル書出しに失敗しました。", e);
		}
	}

	@Override
	public SQLReader getSingleSQLReader(File inputFile) throws ActionInterruptException{
		try {
			return SQLReaderFactory.getSingleSQLReader(inputFile, messageObserver, this.getConfigure());
		} catch (IOException e) {
			messageObserver.setFatalMessage("ファイル書出しに失敗しました。", e);
			throw new ActionInterruptException("ファイル書出しに失敗しました。", e);
		}
	}

	@Override
	public SQLWriter getSingleFileSQLWriter(File outputFile) throws ActionInterruptException{
		try{
			return SQLWriterFactory.getSingleFileSQLWriter(outputFile, messageObserver, this.getConfigure());
		} catch (IOException e) {
			messageObserver.setFatalMessage("ファイル読込みに失敗しました。", e);
			throw new ActionInterruptException("ファイル読込みに失敗しました。", e);
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
