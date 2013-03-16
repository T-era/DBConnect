package jp.gr.java_conf.t_era.dbconnect.view.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.xml.sax.SAXException;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.MessageObserver;
import jp.gr.java_conf.t_era.dbconnect.view.MainFrame;
import jp.gr.java_conf.t_era.util.UtilityVersionInfo;
import jp.gr.java_conf.t_era.version.view.VersionDialog;

/**
 * Helpメニューを定義します。<br/>
 * @author y-terada
 *
 */
class HelpMenu extends JMenu{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	HelpMenu(final MainFrame parent, final Configure config){
		super("Help");
		setMnemonic( KeyEvent.VK_H );

		JMenuItem about = new JMenuItem("About this");
		about.setMnemonic( KeyEvent.VK_A );
		about.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ){
				showDialog(parent, parent, config);
			}
		});

		this.add(about);

		JMenuItem lib = new JMenuItem("About Libraries");
		lib.setMnemonic( KeyEvent.VK_L );
		lib.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ){
				showDialogLiburalies(parent, parent);
			}
		});

		this.add(lib);
	}

	public static void showDialog(JFrame parent, MessageObserver messageObserver, Configure config){
		File versionFile = new File(config.system.getVersionInfoFileName());
		if (versionFile == null || ! versionFile.exists()) {
			return;
		}

		try{
			VersionDialog.getDialog(parent, "", versionFile).setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
			messageObserver.setErrorMessage(e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			messageObserver.setErrorMessage(e.getMessage());
		}
	}

	public static void showDialogLiburalies(JFrame parent, MessageObserver messageObserver){
		try{
			VersionDialog.getDialog(parent, "", UtilityVersionInfo.getVersion()).setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
			messageObserver.setErrorMessage(e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			messageObserver.setErrorMessage(e.getMessage());
		}
	}
}
