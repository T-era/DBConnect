package ytel.dbconnect.model;

/**
 * 検索結果　クエリの実行結果としての戻り値
 * @author y-terada
 *
 */
public interface QueryResultView {
	/**
	 * 配列として、タイトルの一覧を返します
	 * @return タイトルの一覧の配列
	 */
	String[] getTitleArray();
	/**
	 * 配列としてデータの一覧を返します。<br/>
	 * [[一行の各レコードの配列]の配列]です。
	 * @return データの一覧
	 */
	String[][] getValueArray();

	/**
	 * クエリ実行にかかった時間を、msec(ミリ秒)単位で返します。
	 * @return クエリの実行時間
	 */
	long getExecuteTime();

	/**
	 * 結果の件数を返します。
	 * @return 件数
	 */
	int getNumOfResult();
}