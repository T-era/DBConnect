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
 * Transactionメニューを定義します。<br/>
 * DBのトランザクションに関する処理群
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
	 * アプリケーションのコネクション再接続に対するオブザーバ<p/>
	 * コネクションによっては、トランザクションが無効になるため、このメニュー項目を
	 * 無効にします。<br/>
	 * このオブザーバは、{@link ConnectionFactoryImpl}に追加することで、接続再取得の際の
	 * イベントを処理します。
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
