package ytel.dbconnect.config;

/**
 * DBConnect�̐ݒ�l�Ǘ�
 * <p>���[�U�����ύX�\�Ȑݒ����ێ����܂��B
 * <strong>�ݒ�l�͔C�ӂł����Anull��Ԃ����ꍇ�ɐ���ɓ��삷�邱�Ƃ�ۏႷ����̂ł͂���܂���B</strong>
 * </p>
 *
 * @author y-terada
 *
 */
public interface UserConfigure {
	String getDriverName();
	String getDefaultUrl();
	String getDefaultUserName();
	String getDefaultPassword();
	String getConnectionCheckSql();
	String getDefaultSqlFileName();
	String getDefaultResultCsvFileName();
	String getTableInfoQuery();
	String getColumnInfoQuery();
	String getColumnInfoQuery_TableNameReplacer();
	String getDataBaseName();

	/**
	 * �����R�~�b�g�ɂ��Ă̐ݒ�l��Ԃ��܂�<br/>
	 * �ݒ�t�@�C���ł̕�����w�肩��A�^�U�l�ɕϊ����ĕԂ��܂��B
	 * @return �ݒ�l
	 */
	boolean getDefaultAutoCommit();
	/**
	 * ���[�h�I�����[�ɂ��Ă̐ݒ�l��Ԃ��܂�<br/>
	 * �ݒ�t�@�C���ł̕�����w�肩��A�^�U�l�ɕϊ����ĕԂ��܂��B
	 * @return �ݒ�l
	 */
	boolean getDefaultReadOnly();
	String getDefaultSql();

	/**
	 * SQL�̃R�����g�A�E�g������������(Oracle�ł́A"--"�ɂȂ�ׂ�)
	 */
	String getSqlCommentOut();
}
