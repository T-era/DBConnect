package jp.gr.java_conf.t_era.dbconnect.model;

import java.sql.Connection;

/**
 * {@link ConnectionFactoryImpl}�N���X���ł̃R�l�N�V�����̍Ď擾���Ď�����A�I�u�U�[�o�ł��B<p/>
 * �R�l�N�V�����̍Ď擾�����m����K�v������ꍇ�A���̃N���X���p�����A{@link #actionPerformed(Connection, ConnectionInfo)}
 * �ɍĎ擾�̍ۂ̓�����������܂��B<p/>
 * @author y-terada
 *
 */
public abstract class ConnectionObserver{
	private ConnectionObserver next = null;
	/**
	 * �A�v���P�[�V�������Q�Ƃ���DB�R�l�N�V�������ύX���ꂽ�ꍇ�ɔ�������C�x���g��
	 * �������܂��B<br>
	 * �Ď��Ώۂ́A�R�l�N�V�����ύX���ɂ��̃��\�b�h���R�[�����܂��B
	 * @param newConnection �V���Ɏ擾�����R�l�N�V����
	 */
	final void callAction(Connection newConnection, ConnectionInfo newInfo){
		this.actionPerformed(newConnection, newInfo);
		if (next != null){
			next.callAction(newConnection, newInfo);
		}
	}

	/**
	 * �A�v���P�[�V�������Q�Ƃ���DB�R�l�N�V�������ύX���ꂽ�ꍇ�ɔ�������C�x���g��
	 * �L�q���܂��B
	 * @param newConnection �V���Ɏ擾�����R�l�N�V����
	 */
	protected abstract void actionPerformed(Connection newConnection, ConnectionInfo newInfo);

	/**
	 * ���̃I�u�W�F�N�g�ɁA�ʂ̃I�u�U�[�o��ǉ����܂��B<p/>
	 * �ǉ������I�u�U�[�o�́A���̃I�u�W�F�N�g���C�x���g�������s������ɃC�x���g�������s���܂��B<p/>
	 * @param newObserver �ǉ�����I�u�U�[�o
	 */
	final void add(ConnectionObserver newObserver){
		if (this.next == null){
			next = newObserver;
		} else {
			next.add(newObserver);
		}
	}
}
