package jp.gr.java_conf.t_era.dbconnect.model.database.queryresultview;

import jp.gr.java_conf.t_era.dbconnect.model.QueryResultView;

/**
 * �������ʂ��Ȃ��ꍇ�́A�\���p��������
 * @author y-terada
 *
 */
public class NullViewInfo implements QueryResultView{
	/**
	 * �^�C�g��
	 */
	private final String title;
	/**
	 * SQL���s����
	 */
	private final long executeTime;
	/**
	 * �����\��
	 */
	private final int numOfResult;

	/**
	 * �C���X�^���X�𐶐����܂��B
	 * @param title �^�C�g��
	 * @param numOfResult �X�V����
	 * @param executeTime ���s����
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
