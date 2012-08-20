package ytel.dbconnect.model;

/**
 * 接続に用いる情報を管理するクラス<br>
 * <Strong>このインターフェースが表現するのは、接続用の情報であって、接続情報({@link java.sql.Connection})
 * は含みません</Strong>
 * 
 * @author y-terada
 *
 */
public interface ConnectionInfo {
	/**
	 * 接続先のURLを返すします。
	 * @return DBのURL
	 */
	String getUrl();
	/**
	 * 接続先DBのユーザ名を返すします。
	 * @return DBのユーザ名
	 */
	String getUserName();
	/**
	 * 接続先DBのパスワードを返すします。
	 * @return DBのパスワード
	 */
	String getPassword();
	/**
	 * 自動コミット設定を返します。
	 * @return DBの自動コミット設定
	 */
	boolean getAutoCommit();
	/**
	 * 読み込み専用設定を返します。
	 * @return DBの読み込み専用設定
	 */
	boolean getReadOnly();
}
