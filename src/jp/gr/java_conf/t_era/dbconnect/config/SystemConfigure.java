package jp.gr.java_conf.t_era.dbconnect.config;

/**
 * DBConnect�̐ݒ�l�Ǘ�
 * <p>
 * �ڑ���DB��g�p���郆�[�U�ɂ���ĕύX����Ȃ��ݒ����ێ����܂��B
 * </p>
 *
 * @author y-terada
 *
 */
public interface SystemConfigure {
	/**
	 * �����̌��ʂ��Ȃ������ꍇ�̕\��
	 */
	String getNullLabel();
	/**
	 * ��������
	 */
	String getResultLabel();
	/**
	 * �G���[�\��
	 */
	String getErrorLabel();
	/**
	 * �o�[�W���������L�ڂ���XML�t�@�C���̖���
	 */
	String getVersionInfoFileName();
	String getAlertMessage();
	String getAppTitle();
	String getLabelNameTemplate();
	/**
	 * SQL�t�@�C���̏o�͂ɂ��āA�f�t�H���g�ł̏㏑���ݒ�̐ݒ�l��Ԃ��܂�<br/>
	 * @return �ݒ�l
	 */
	boolean getDefaultOverWriteSqlFile();
}
