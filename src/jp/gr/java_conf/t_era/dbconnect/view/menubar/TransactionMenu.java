package jp.gr.java_conf.t_era.dbconnect.view.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jp.gr.java_conf.t_era.dbconnect.model.ActionInterruptException;
import jp.gr.java_conf.t_era.dbconnect.model.ConnectionInfo;
import jp.gr.java_conf.t_era.dbconnect.model.ConnectionObserver;
import jp.gr.java_conf.t_era.dbconnect.view.MainFrame;

/**
 * Transaction���j���[���`���܂��B<br/>
 * DB�̃g�����U�N�V�����Ɋւ��鏈���Q
 * @author y-terada
 *
 */
class TransactionMenu extends JMenu {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final JMenuItem commitMenu;
	private final JMenuItem rollbackMenu;

	TransactionMenu(final MainFrame parent){
		super("Transaction");

		setMnemonic( KeyEvent.VK_T );

		commitMenu = new JMenuItem("Commit");
		{
			commitMenu.setMnemonic('C');
			commitMenu.setAccelerator(
					KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
			commitMenu.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					try{
						parent.getMainModule().commit();
						parent.setMessage("Commit done.");
					} catch (ActionInterruptException ex) {}
				}
			});
		}
		rollbackMenu = new JMenuItem("Rollback");
		{
			rollbackMenu.setMnemonic('R');
			rollbackMenu.setAccelerator(
					KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
			rollbackMenu.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					try{
						parent.getMainModule().rollback();
						parent.setMessage("Rollback done.");
					} catch (ActionInterruptException ex){}
				}
			});
		}
		this.add(commitMenu);
		this.add(rollbackMenu);
		parent.getMainModule().addConnectionObserver(new PrivateConnectionObserver());
	}

	/**
	 * �A�v���P�[�V�����̃R�l�N�V�����Đڑ��ɑ΂���I�u�U�[�o<p/>
	 * �R�l�N�V�����ɂ���ẮA�g�����U�N�V�����������ɂȂ邽�߁A���̃��j���[���ڂ�
	 * �����ɂ��܂��B<br/>
	 * ���̃I�u�U�[�o�́A{@link ConnectionFactoryImpl}�ɒǉ����邱�ƂŁA�ڑ��Ď擾�̍ۂ�
	 * �C�x���g���������܂��B
	 * @author y-terada
	 *
	 */
	private class PrivateConnectionObserver extends ConnectionObserver {
		@Override
		protected void actionPerformed(Connection newConnection, ConnectionInfo newInfo) {
			if (newConnection == null){
				return;
			}
			try{
				if (newConnection.isReadOnly()
						|| newConnection.getAutoCommit()
						|| newConnection.isClosed()) {
					commitMenu.setEnabled(false);
				} else {
					commitMenu.setEnabled(true);
				}
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
}
