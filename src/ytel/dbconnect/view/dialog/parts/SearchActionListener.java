package ytel.dbconnect.view.dialog.parts;

/**
 * TABLE �����̂��߂̃��X�i�B
 * �Č������K�v�ɂȂ����^�C�~���O�ŃR�[���o�b�N���󂯂邽�߂̃C���^�[�t�F�[�X
 *
 * @author 22677478
 *
 */
public interface SearchActionListener {
	/**
	 * (�ˋ��)Search�{�^���������ꂽ�ꍇ�̏������L�q���܂��B
	 * ���ۂɃ{�^���Ƃ���Search�{�^�����Ȃ��Ă��A�Č������K�v�ɂȂ����^�C�~���O�ŃR�[���o�b�N����܂��B
	 * @param condition �������� ������
	 * @param asRegex �������� ���K�\�����ǂ���
	 */
	void searchPushed(String condition, boolean asRegex);
}
