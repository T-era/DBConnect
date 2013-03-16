package jp.gr.java_conf.t_era.dbconnect.model.database;

import jp.gr.java_conf.t_era.dbconnect.model.ActionInterruptException;
import jp.gr.java_conf.t_era.dbconnect.model.MessageObserver;
import jp.gr.java_conf.t_era.dbconnect.model.QueryResultView;

/**
 * �N�G�����s���W���[�������J����C���^�[�t�F�[�X�ł��B
 * 
 * @author y-terada
 *
 */
public interface QueryExecuter {
	/**
	 * �N�G�������s���A���s���ʂ�Ԃ��܂��B<br>
	 * �N�G���̎��s���ɗ�O����������ꍇ�A��O�̏����͂��̃��\�b�h���Ŋ������܂��B
	 * @param sql ���s����N�G��
	 * @param messageObserver ���s�����b�Z�[�W������I�u�U�[�o�B��O���̏��́A���̃I�u�W�F�N�g�ɒʒm����܂��B
	 * @return �N�G�����s����
	 * @throws ActionInterruptException UI�̃A�N�V���������f�����ׂ��ꍇ�A�X���[����܂��B
	 * �@UI�́A�A�N�V�����̎c��̏������s���Ă͂����܂���B
	 */
	QueryResultView exec(String sql, MessageObserver messageObserver) throws ActionInterruptException;
}
