package ytel.dbconnect.view.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import ytel.dbconnect.config.Configure;
import ytel.dbconnect.model.ActionInterruptException;
import ytel.dbconnect.model.ConnectionInfo;
import ytel.dbconnect.view.MainFrame;
import ytel.dbconnect.view.dialog.ConnectionDialog;

/**
 *DataBaseメニューを定義します
 * @author y-terada
 *
 */
class DataBaseMenu extends JMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final MainFrame parent;
	private final Configure config;

	DataBaseMenu(final MainFrame parent, final Configure config){
		super("DataBase");
		this.parent = parent;
		this.config = config;

		setMnemonic( KeyEvent.VK_D );

		JMenuItem dbConnect = new JMenuItem("DB Connect");
		dbConnect.setMnemonic( KeyEvent.VK_C );
		dbConnect.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_C, KeyEvent.SHIFT_MASK + KeyEvent.CTRL_MASK ) );
		dbConnect.addActionListener( new ReConfAction() );
		JMenuItem tableInfo = new JMenuItem("Table Info");
		tableInfo.setMnemonic( KeyEvent.VK_T );
		tableInfo.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_T, KeyEvent.CTRL_MASK ) );
		tableInfo.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				try {
					parent.showTableInfoDialog();
				} catch (ActionInterruptException e) {}
			}
		});
		JMenuItem doExecute = new JMenuItem("Run");
		doExecute.setMnemonic( KeyEvent.VK_R );
		doExecute.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_R, KeyEvent.CTRL_MASK ) );
		doExecute.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ){
				try{
					parent.doAction();
				} catch (ActionInterruptException e) {}
			}
		});
		JMenuItem doExecuteWithArgument = new JMenuItem("Run with args");
		doExecuteWithArgument.setMnemonic( KeyEvent.VK_Q );
		doExecuteWithArgument.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_Q, KeyEvent.CTRL_MASK ) );
		doExecuteWithArgument.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ){
				try {
					parent.doActionWithArgs();
				} catch (ActionInterruptException e) {}
			}
		});
		JMenuItem doExecuteNewWindow = new JMenuItem("Run (open new Window)");
		doExecuteNewWindow.setMnemonic( KeyEvent.VK_W );
		doExecuteNewWindow.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_W, KeyEvent.CTRL_MASK ) );
		doExecuteNewWindow.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ){
				try {
					parent.doActionWithArgsNewWindow();
				} catch (ActionInterruptException e) {}
			}
		});


		add( dbConnect );
		add( tableInfo );
		add( new JSeparator() );
		add( doExecute );
		add( doExecuteWithArgument );
		add( doExecuteNewWindow );
		add( new JSeparator() );
		add(new TransactionMenu(parent));
	}

	/**
	 * 接続情報再設定のアクション
	 * @author y-terada
	 *
	 */
	class ReConfAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ConnectionInfo connectionInfo = parent.getMainModule().getConnectionInfo();
			ConnectionDialog dialogObj = new ConnectionDialog(
					  parent
					, config
					, connectionInfo.getUrl()
					, connectionInfo.getUserName()
					, connectionInfo.getPassword()
					, connectionInfo.getAutoCommit()
					, connectionInfo.getReadOnly() );
			if( dialogObj.isCanceled() ){
			}else{
				parent.getMainModule().setDataBaseInfo(dialogObj.getUrl(), dialogObj.getUserName(), dialogObj.getPassword(), dialogObj.getAutoCommit(), dialogObj.getReadOnly());
				try {
					parent.getMainModule().refleshConnection();
				} catch (ActionInterruptException ex) {}
			}
		}
	}
}
