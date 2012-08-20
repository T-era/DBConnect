package ytel.dbconnect.model.database.queryresultview;

import ytel.dbconnect.model.MainModule;
import ytel.dbconnect.model.QueryResultView;

/**
 * クエリ実行エラーの際の、表示用検索結果
 * @author y-terada
 *
 */
public class ErrorViewInfo implements QueryResultView{
	private final MainModule main;

	private ErrorViewInfo(MainModule main) {
		this.main = main;
	}

	@Override
	public String[] getTitleArray() {
		return new String[]{main.getConfigure().system.getErrorLabel()};
	}

	@Override
	public String[][] getValueArray() {
		return new String[][]{{main.getConfigure().system.getNullLabel()}};
	}

	@Override
	public long getExecuteTime() {
		return -1;
	}

	@Override
	public int getNumOfResult() {
		return -1;
	}

	/**
	 * 生成済みの、このクラスのインスタンスを返します。
	 * @return インスタンス
	 */
	public static ErrorViewInfo getInstance(MainModule main){
		return new ErrorViewInfo(main);
	}
}
