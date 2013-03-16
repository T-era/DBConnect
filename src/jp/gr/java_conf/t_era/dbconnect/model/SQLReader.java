package jp.gr.java_conf.t_era.dbconnect.model;

/**
 * SQL�̓ǂݍ��݂��s���N���X�̃C���^�[�t�F�[�X
 * @author y-terada
 *
 */
public interface SQLReader {
	/**
	 * SQL({@link LabeledSql})��Ǎ��݁A�ǂݍ��݌��ʂ�Ԃ��܂��B
	 * <p/>���̃��\�b�h���ĂԑO�ɁA{@link #hasMore()}���\�b�h�Ō������s���܂��B
	 * @return �ǂݍ��݌���
	 * @throws ActionInterruptException
	 */
	LabeledSql read() throws ActionInterruptException;
	/**
	 * �I�u�W�F�N�g���ێ�����X�g���[������܂��B
	 * @throws ActionInterruptException
	 */
	void close() throws ActionInterruptException;
	/**
	 * ����ɓǍ��݉\��SQL�����邩�ǂ����𔻒肵�܂��B
	 * <p/>���̌��ʂ�true�̏ꍇ�A{@link #read()}���\�b�h��SQL���擾�ł��邱�Ƃ��Ӗ����܂��B
	 * @return �Ǎ��݉\�Ȃ�true
	 */
	boolean hasMore();
}
