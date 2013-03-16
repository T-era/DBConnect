package jp.gr.java_conf.t_era.dbconnect.model.io;

import jp.gr.java_conf.t_era.dbconnect.model.LabeledSql;

/**
 * ���x���i�^�C�g���j�t����SQL<br/>
 * @author y-terada
 *
 */
final class LabeledSqlImpl implements LabeledSql{
	/**
	 * ���x���i�^�C�g���j
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
