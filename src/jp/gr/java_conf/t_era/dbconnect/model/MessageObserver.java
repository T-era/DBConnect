package jp.gr.java_conf.t_era.dbconnect.model;

/**
 * ���b�Z�[�W�̕\���������C���^�[�t�F�[�X�B
 * <p>
 * ���b�Z�[�W�\���𔺂�UI�́A���̃C���^�[�t�F�[�X�����������\�����������܂��B<br>
 * ��O/�A�v���P�[�V�����G���[/�����󋵓��\����v�����郂�W���[���́A���̃C���^�[�t�F�[�X�Ɍ����ă��b�Z�[�W��
 * ���������邱�ƂŁA�\�����̂��ӎ������Ƀ��b�Z�[�W�𑗏o���܂��B
 * 
 * @author y-terada
 *
 */
public interface MessageObserver {
	/**
	 * ������INFO���x���̏���\�����܂��B
	 * @param message
	 */
	void setMessage(String message);
	/**
	 * ������ERROR���x���̏���\�����܂��B
	 * @param message
	 */
	void setErrorMessage(String message);
	/**
	 * ������ERROR���x���̏���\�����܂��B
	 * ��O���󂯎��A��O�Ɋ܂܂��(�X�^�b�N�g���[�X�Ȃǂ�)�K�v�ȏ���\���ɉ����܂��B
	 * @param message
	 * @param t ����������O
	 */
	void setErrorMessage(String message, Throwable t);
	/**
	 * ������FATAL���x���̏���\�����܂��B
	 * ��O���󂯎��A��O�Ɋ܂܂��(�X�^�b�N�g���[�X�Ȃǂ�)�K�v�ȏ���\���ɉ����܂��B
	 * @param message
	 */
	void setFatalMessage(String message);
	/**
	 * ������FATAL���x���̏���\�����܂��B
	 * ��O���󂯎��A��O�Ɋ܂܂��(�X�^�b�N�g���[�X�Ȃǂ�)�K�v�ȏ���\���ɉ����܂��B
	 * @param message
	 * @param t ����������O
	 */
	void setFatalMessage(String message, Throwable t);
}
