package jp.gr.java_conf.t_era.dbconnect.model;

/**
 * GUI�̃C�x���g�������ɁA���W���[�����Ŕ���������O��ʒm���܂��B
 * <p>
 * ��O�ɑ΂���K�؂ȏ���(�Ⴆ�΁AUI�ւ̒ʒm�\���Ȃ�)�́A���W���[�������ӔC�������܂��B
 * ���̗�O�́A�A�N�V������(��O�����ȍ~��)�������p�����čs���Ȃ����Ƃ�ۏႷ�邽�߂̃}�[�J�ł��B
 * <p>
 * UI���ł́A���̗�O������Ԃ����Ƃ��ł��܂��B<br>
 * �������A�����I�ɂ����Listener���̃A�N�V�����������\�b�h�̖����ōs���ׂ��ł��B
 * <p>
 * <strong>
 * ���̃N���X���Acause��message���R���X�g���N�^�ŗv������̂́A�����Ƀf�o�b�O�̍ۂ̗��֐��̂��߂ł��B
 * ���̃N���X��getMessage()��getCause()���g�p����R�[�h�́A�����I�Ɍ��ƍl����ׂ��ł��B
 * </strong>
 *
 * @author y-terada
 *
 */
public class ActionInterruptException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ActionInterruptException(String message, Throwable cause){
		super(message, cause);
	}

	@Override
	@Deprecated
	public String getMessage() {
		return super.getMessage();
	}

	@Override
	@Deprecated
	public Throwable getCause() {
		return super.getCause();
	}

}
