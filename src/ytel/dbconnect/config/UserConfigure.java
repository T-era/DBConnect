package ytel.dbconnect.config;

/**
 * DBConnectの設定値管理
 * <p>ユーザ毎が変更可能な設定情報を保持します。
 * <strong>設定値は任意ですが、nullを返した場合に正常に動作することを保障するものではありません。</strong>
 * </p>
 *
 * @author y-terada
 *
 */
public interface UserConfigure {
	String getDriverName();
	String getDefaultUrl();
	String getDefaultUserName();
	String getDefaultPassword();
	String getConnectionCheckSql();
	String getDefaultSqlFileName();
	String getDefaultResultCsvFileName();
	String getTableInfoQuery();
	String getColumnInfoQuery();
	String getColumnInfoQuery_TableNameReplacer();
	String getDataBaseName();

	/**
	 * 自動コミットについての設定値を返します<br/>
	 * 設定ファイルでの文字列指定から、真偽値に変換して返します。
	 * @return 設定値
	 */
	boolean getDefaultAutoCommit();
	/**
	 * リードオンリーについての設定値を返します<br/>
	 * 設定ファイルでの文字列指定から、真偽値に変換して返します。
	 * @return 設定値
	 */
	boolean getDefaultReadOnly();
	String getDefaultSql();

	/**
	 * SQLのコメントアウトを示す文字列(Oracleでは、"--"になるべき)
	 */
	String getSqlCommentOut();
}
