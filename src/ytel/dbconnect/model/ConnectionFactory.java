package ytel.dbconnect.model;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * コネクションの取得/維持を管理します。<p/>
 * @author y-terada
 *
 */
public interface ConnectionFactory{
	/**
	 * 接続情報を設定します。<br/>
	 * このメソッドを呼び出すと、古い接続情報でのDB接続は破棄されます。
	 * @param newUrl 接続先DBのURL
	 * @param newUserName 接続先DBのユーザ名
	 * @param newPassword 接続先DBのユーザ名
	 * @param newAutoCommit
	 * @param newReadOnly
	 */
	void setConnectInfo( String newUrl, String newUserName, String newPassword, boolean newAutoCommit, boolean newReadOnly );
	/**
	 * 保持しているコネクションを返す。<p/>
	 * 保持しているコネクションが閉じられている場合、このメソッドの戻り値は
	 * 閉じられたコネクションになります。<br/>
	 * コネクションを取得していない場合、nullを返すことがあります。
	 * @return DBとのコネクション
	 * @throws SQLException
	 */
	Connection getConnection() throws SQLException;

	void connectionCommit() throws SQLException;

	void connectionRollback() throws SQLException;

	/**
	 * コネクションを接続する。既に有効なコネクションがある場合は何も行わない。<br/>
	 * ない場合は新規に接続する。<br/>
	 * 接続したコネクションは、{@link #getConnection()}で取得します。<p/>
	 * @throws SQLException
	 */
	public void getNewConnection() throws SQLException;

	/**
	 * コネクションを強制的に閉じます。<br/>
	 * 例外は無視されます。
	 */
	public void connectionClose();
//
//	public String getUrl();
//	public String getUserName();
//	public String getPassword();
//	public boolean getAutoCommit();
//	public boolean getReadOnly();

	/**
	 * コネクション取得イベントを受け取るオブザーバを追加します。<br/>
	 * このオブジェクト内でコネクションの再取得をした場合、オブザーバは{@link ConnectionObserver#actionPerformed(Connection, ConnectionInfo)}
	 * イベントを受け取ります。
	 * @param newObserver イベントを受け取るオブザーバ
	 */
	public void addObserver(ConnectionObserver newObserver);
}
