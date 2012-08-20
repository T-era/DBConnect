package ytel.dbconnect.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * コネクションの取得/維持を管理します。<p/>
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
	 * コネクション取得イベントを受け取るオブザーバ
	 */
	private ConnectionObserver observer;
	/**
	 * 表示メッセージを受け取るオブザーバ
	 */
	private final MessageObserver messageObserver;

	/**
	 * JDBCドライバを指定してインスタンスを生成行します。
	 * @param driverName JDBCドライバ
	 * @param connectionCheckSQL 取得したコネクションを検査するためのSQL
	 * @param messageObserver メッセージを受取るオブザーバ
	 */
	ConnectionFactoryImpl( String driverName, String connectionCheckSQL, MessageObserver messageObserver ){
		this.driverName = driverName;
		this.connectionCheckSQL = connectionCheckSQL;
		this.messageObserver = messageObserver;
	}

	/**
	 * 接続情報を設定します。<br/>
	 * このメソッドを呼び出すと、古い接続情報でのDB接続は破棄されます。
	 * @param newUrl 接続先DBのURL
	 * @param newUserName 接続先DBのユーザ名
	 * @param newPassword 接続先DBのユーザ名
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
	 * 保持しているコネクションを返す。<p/>
	 * 保持しているコネクションが閉じられている場合、このメソッドの戻り値は
	 * 閉じられたコネクションになります。<br/>
	 * コネクションを取得していない場合、nullを返すことがあります。
	 * @return DBとのコネクション
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
	 * コネクションを接続する。既に有効なコネクションがある場合は何も行わない。<br/>
	 * ない場合は新規に接続する。<br/>
	 * 接続したコネクションは、{@link #getConnection()}で取得します。<p/>
	 * 有効なコネクションがあるかどうかの判定は、{@link #isActiveConnection()}を用います。
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
			messageObserver.setFatalMessage("DriverNameを取得したら、ExecQuery.checkConnectInfo()で確認するべき。", e);
			throw new SQLException(e);
		}catch( SQLException e ){
			con = null;
			messageObserver.setFatalMessage("コネクションの取得に失敗", e);
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
			throw new SQLException("取得したコネクションが使用できない。");
		}
	}

	/**
	 * 検査SQLを使って、コネクションが有効であるか判定します。<br/>
	 * 検査には、{@link #connectionCheckSQL}を使用します。
	 * @return 有効な場合true
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
	 * コネクションを強制的に閉じます。<br/>
	 * 例外は無視されます。
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
	 * この接続の接続先URLを返します。
	 * @return 接続先URL
	 */
	String getUrl(){
		return url;
	}
	/**
	 * この接続のDBユーザ名を返します。
	 * @return DBユーザ名
	 */
	String getUserName(){
		return userName;
	}
	/**
	 * この接続のDBパスワードを返します。
	 * @return DBパスワード
	 */
	String getPassword(){
		return password;
	}
	/**
	 * この接続の自動コミット設定を返します。
	 * @return 自動コミット設定
	 */
	boolean getAutoCommit(){
		return autoCommit;
	}
	/**
	 * この接続の読込み専用設定を返します。
	 * @return 読込み専用設定
	 */
	boolean getReadOnly(){
		return readOnly;
	}

	/**
	 * コネクション取得イベントを受け取るオブザーバを追加します。
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
	 * 指定された接続情報が有効であるかどうかを確認します。<p/>
	 * この実装では、実際にコネクションを取得し、チェックSQLを実行してみることで確認します。
	 * 失敗した場合はすべて例外となります。
	 * @param driverName JDBCドライバ
	 * @param url 接続先DBのURL
	 * @param userName DBのユーザ名
	 * @param password DBのパスワード
	 * @param connectionCheckSQL チェックSQL
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
