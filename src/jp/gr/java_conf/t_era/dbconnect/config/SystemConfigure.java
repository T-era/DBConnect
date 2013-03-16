package jp.gr.java_conf.t_era.dbconnect.config;

/**
 * DBConnectの設定値管理
 * <p>
 * 接続先DBや使用するユーザによって変更されない設定情報を保持します。
 * </p>
 *
 * @author y-terada
 *
 */
public interface SystemConfigure {
	/**
	 * 検索の結果がなかった場合の表示
	 */
	String getNullLabel();
	/**
	 * 検索結果
	 */
	String getResultLabel();
	/**
	 * エラー表示
	 */
	String getErrorLabel();
	/**
	 * バージョン情報を記載したXMLファイルの名称
	 */
	String getVersionInfoFileName();
	String getAlertMessage();
	String getAppTitle();
	String getLabelNameTemplate();
	/**
	 * SQLファイルの出力について、デフォルトでの上書き設定の設定値を返します<br/>
	 * @return 設定値
	 */
	boolean getDefaultOverWriteSqlFile();
}
