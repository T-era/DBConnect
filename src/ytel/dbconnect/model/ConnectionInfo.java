package ytel.dbconnect.model;

/**
 * �ڑ��ɗp��������Ǘ�����N���X<br>
 * <Strong>���̃C���^�[�t�F�[�X���\������̂́A�ڑ��p�̏��ł����āA�ڑ����({@link java.sql.Connection})
 * �͊܂݂܂���</Strong>
 * 
 * @author y-terada
 *
 */
public interface ConnectionInfo {
	/**
	 * �ڑ����URL��Ԃ����܂��B
	 * @return DB��URL
	 */
	String getUrl();
	/**
	 * �ڑ���DB�̃��[�U����Ԃ����܂��B
	 * @return DB�̃��[�U��
	 */
	String getUserName();
	/**
	 * �ڑ���DB�̃p�X���[�h��Ԃ����܂��B
	 * @return DB�̃p�X���[�h
	 */
	String getPassword();
	/**
	 * �����R�~�b�g�ݒ��Ԃ��܂��B
	 * @return DB�̎����R�~�b�g�ݒ�
	 */
	boolean getAutoCommit();
	/**
	 * �ǂݍ��ݐ�p�ݒ��Ԃ��܂��B
	 * @return DB�̓ǂݍ��ݐ�p�ݒ�
	 */
	boolean getReadOnly();
}
