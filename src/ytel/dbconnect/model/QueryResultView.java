package ytel.dbconnect.model;

/**
 * �������ʁ@�N�G���̎��s���ʂƂ��Ă̖߂�l
 * @author y-terada
 *
 */
public interface QueryResultView {
	/**
	 * �z��Ƃ��āA�^�C�g���̈ꗗ��Ԃ��܂�
	 * @return �^�C�g���̈ꗗ�̔z��
	 */
	String[] getTitleArray();
	/**
	 * �z��Ƃ��ăf�[�^�̈ꗗ��Ԃ��܂��B<br/>
	 * [[��s�̊e���R�[�h�̔z��]�̔z��]�ł��B
	 * @return �f�[�^�̈ꗗ
	 */
	String[][] getValueArray();

	/**
	 * �N�G�����s�ɂ����������Ԃ��Amsec(�~���b)�P�ʂŕԂ��܂��B
	 * @return �N�G���̎��s����
	 */
	long getExecuteTime();

	/**
	 * ���ʂ̌�����Ԃ��܂��B
	 * @return ����
	 */
	int getNumOfResult();
}