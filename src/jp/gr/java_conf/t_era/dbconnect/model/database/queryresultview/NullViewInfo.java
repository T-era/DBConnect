package jp.gr.java_conf.t_era.dbconnect.model.database.queryresultview;

import jp.gr.java_conf.t_era.dbconnect.model.QueryResultView;

/**
 * 検索結果がない場合の、表示用検索結果
 * @author y-terada
 *
 */
public class NullViewInfo implements QueryResultView{
	/**
	 * タイトル
	 */
	private final String title;
	/**
	 * SQL実行時間
	 */
	private final long executeTime;
	/**
	 * 件数表示
	 */
	private final int numOfResult;

	/**
	 * インスタンスを生成します。
	 * @param title タイトル
	 * @param numOfResult 更新件数
	 * @param executeTime 実行時間
	 */
	public NullViewInfo(String title, int numOfResult, long executeTime){
		this.title = title;
		this.executeTime = executeTime;
		this.numOfResult = numOfResult;
	}

	@Override
	public String[] getTitleArray() {
		return new String[]{title};
	}

	@Override
	public String[][] getValueArray() {
		return new String[][]{{Integer.toString(numOfResult)}};
	}

	@Override
	public long getExecuteTime() {
		return executeTime;
	}

	@Override
	public int getNumOfResult() {
		return numOfResult;
	}
}
