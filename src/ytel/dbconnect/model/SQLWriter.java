package ytel.dbconnect.model;


/**
 * SQL�̏������݂��s���N���X�̃C���^�[�t�F�[�X
 * @author y-terada
 *
 */
public interface SQLWriter {
	/**
	 * �����݂��s���܂��B
	 * @param sql �������ޓ��e��SQL
	 * @throws ActionInterruptException
	 */
	void write(LabeledSql sql) throws ActionInterruptException;
	/**
	 * ���x���i�^�C�g���j�Ɠ��e���w�肵�āA�������݂��s���܂��B
	 * @param label ���x��
	 * @param entry ���e
	 * @throws ActionInterruptException
	 */
	void write(String label, String entry) throws ActionInterruptException;
	/**
	 * �I�u�W�F�N�g���ێ�����X�g���[����j�����܂��B
	 * @throws ActionInterruptException
	 */
	void close() throws ActionInterruptException;
}
