package jp.gr.java_conf.t_era.dbconnect.view.menubar;

import javax.swing.JMenuBar;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.view.MainFrame;

/**
 * メニューバーの内容を定義します。
 * @author y-terada
 *
 */
public class DBConnectMenuBar extends JMenuBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DBConnectMenuBar(final MainFrame parent, final Configure config){

		this.add(new DataBaseMenu(parent, config));
		this.add(new EditMenu(parent));
		this.add(new EditorMenu(parent, config));

		this.add(new HelpMenu(parent, config));
	}
}
