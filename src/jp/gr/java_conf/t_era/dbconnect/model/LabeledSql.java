package jp.gr.java_conf.t_era.dbconnect.model;

/**
 * ���x���i�^�C�g���j�t����SQL<br/>
 * @author y-terada
 *
 */
public interface LabeledSql {
	/**
	 * ���x���i�^�C�g���j��Ԃ��܂�
	 * @return ���x��
	 */
	String getLabel();
	/**
	 * SQL��Ԃ��܂��B
	 * @return SQL(������^)
	 */
	String getSql();
}
