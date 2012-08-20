package ytel.dbconnect.model.io;

import ytel.dbconnect.model.LabeledSql;

/**
 * ラベル（タイトル）付きのSQL<br/>
 * @author y-terada
 *
 */
final class LabeledSqlImpl implements LabeledSql{
	/**
	 * ラベル（タイトル）
	 */
	private final String label;
	/**
	 * SQL
	 */
	private final String sql;

	LabeledSqlImpl(final String label, final String sql){
		this.label = label;
		this.sql = sql;
	}

	@Override
	public String getLabel(){
		return label;
	}
	@Override
	public String getSql(){
		return sql;
	}
}
