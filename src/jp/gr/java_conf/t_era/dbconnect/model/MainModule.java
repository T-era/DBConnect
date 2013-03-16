package jp.gr.java_conf.t_era.dbconnect.model;

import java.io.File;
import java.util.List;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.database.DataBaseInfo;
import jp.gr.java_conf.t_era.dbconnect.model.database.QueryExecuter;

/**
 * DBConnectのモジュールパートの、UI(View)パートに対して公開するインターフェースです。
 * 
 * @author y-terada
 *
 */
public interface MainModule {
	/**
	 * テーブル一覧情報を返します。
	 * @return テーブル一覧情報
	 * @throws ActionInterruptException
	 */
	QueryResultView getTableInfo() throws ActionInterruptException;
	/**
	 * 指定されたテーブルのカラム一覧情報を返します。
	 * @param tableName テーブル名
	 * @return カラム一覧情報
	 * @throws ActionInterruptException
	 */
	QueryResultView getColumnInfo(String tableName) throws ActionInterruptException;
	/**
	 * データベースへの接続情報を更新します。
	 * @param newUrl 接続URL
	 * @param newUserName 接続ユーザ名
	 * @param newPassword 接続パスワード
	 * @param autoCommit 自動コミット設定
	 * @param readOnly 読込み専用設定
	 */
	void setDataBaseInfo(String newUrl, String newUserName, String newPassword, boolean autoCommit, boolean readOnly);
	/**
	 * DB接続を更新します。<br>
	 * 有効なDB接続がない場合、内部的にコネクションの取得を行います。
	 * 有効なDB接続がある場合の動作については、各実装によります。
	 * @throws ActionInterruptException
	 */
	void refleshConnection() throws ActionInterruptException;
	/**
	 * 指定されたSQLの置換えパラメータの数を返します。
	 * @param sql SQL
	 * @return 置換えパラメータの数
	 * @throws ActionInterruptException
	 */
	int getParameterCount(String sql) throws ActionInterruptException;
	/**
	 * SQL実行モジュールを返します。
	 * @param parameters 置換えパラメータ<br>置換えがない場合、nullか空Listを指定します。<br>
	 * 要素の数は、{@link #getParameterCount(String)}と一致する必要があります。
	 * @return SQL実行モジュール
	 */
	QueryExecuter getQueryExecter(List<String> parameters);
	/**
	 * このモジュールが保持するストリーム/コネクション等をCloseします。<br>
	 * このメソッドは、このメソッドが呼ばれた後にオブジェクトがガベージコレクトされた場合、
	 * 全てのリソースについてリークが起きないことを保障します。
	 */
	void close();
	/**
	 * DBの情報を返します。
	 * @return DBの情報
	 */
	DataBaseInfo getDataBaseInfo();
	/**
	 * 接続用の情報を返します。
	 * @return 接続用の情報
	 */
	ConnectionInfo getConnectionInfo();
	/**
	 * DBをコミットします。
	 * @throws ActionInterruptException
	 */
	void commit() throws ActionInterruptException;
	/**
	 * DBをロールバックします。
	 * @throws ActionInterruptException
	 */
	void rollback() throws ActionInterruptException;
	/**
	 * コネクションの再取得を監視するオブザーバを追加します。
	 * @param newObserver コネクションの再取得に対するオブザーバ
	 */
	void addConnectionObserver(ConnectionObserver newObserver);
	/**
	 * SQLを分けて読み出す{@link SQLReader}インスタンスを返します。
	 * @param inputFile 読み出す対象のファイル
	 * @return SQL読み出しオブジェクト
	 * @throws ActionInterruptException
	 */
	SQLReader getSeparateSQLReader(File inputFile) throws ActionInterruptException;
	/**
	 * ファイルを一連のSQLとして読み出す{@link SQLReader}インスタンスを返します。
	 * @param inputFile 読み出す対象のファイル
	 * @return SQL読み出しオブジェクト
	 * @throws ActionInterruptException
	 */
	SQLReader getSingleSQLReader(File inputFile) throws ActionInterruptException;
	/**
	 * ファイルにSQLを書き出す{@link SQLWriter}インスタンスを返します。
	 * @param outputFile 書出し対象のファイル
	 * @return SQL書出しオブジェクト
	 * @throws ActionInterruptException
	 */
	SQLWriter getSingleFileSQLWriter(File outputFile) throws ActionInterruptException;
	/**
	 * 指定された内容で、有効な接続を取得できるかを判定します。
	 * @param driverName JDBCドライバのクラス名
	 * @param url DBの接続URL
	 * @param userName DBの接続ユーザ名
	 * @param password DBの接続パスワード
	 * @param connectionCheckSql 接続を確認するために実行する簡単なSQL
	 * @return 有効な接続を取得できる情報の場合、true
	 */
	boolean checkConnectInfo(String driverName, String url, String userName, String password, String connectionCheckSql);
	/**
	 * 指定されたファイルにデータを書き出すための、書出しモジュールのインスタンスを生成し、返します。
	 * @param targetFile 書出し対象のファイル
	 * @param takeRecursiveBackup true:書出し対象のファイルが、既に存在するファイルの場合バックアップを取ります。
	 * 　バックアップファイルは、"[元の名前]+_bk"となります。
	 * 　バックアップの取得処理についても、書き出しファイルが既存の場合、再帰的にバックアップを行います。
	 * @return 書出しモジュールのインスタンス
	 */
	DataTableWriter getCsvWriter(File targetFile, boolean takeRecursiveBackup);

	Configure getConfigure();
}
