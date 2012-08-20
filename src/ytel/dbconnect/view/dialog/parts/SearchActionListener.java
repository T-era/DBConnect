package ytel.dbconnect.view.dialog.parts;

/**
 * TABLE 検索のためのリスナ。
 * 再検索が必要になったタイミングでコールバックを受けるためのインターフェース
 *
 * @author 22677478
 *
 */
public interface SearchActionListener {
	/**
	 * (架空の)Searchボタンが押された場合の処理を記述します。
	 * 実際にボタンとしてSearchボタンがなくても、再検索が必要になったタイミングでコールバックされます。
	 * @param condition 検索条件 文字列
	 * @param asRegex 検索条件 正規表現かどうか
	 */
	void searchPushed(String condition, boolean asRegex);
}
