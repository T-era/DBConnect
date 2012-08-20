package ytel.dbconnect.view.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ytel.dbconnect.view.MainFrame;

/**
 * Editメニューを定義します。<br/>
 * エディタ内のテキスト編集に関する処理群
 * @author y-terada
 *
 */
class EditMenu extends JMenu{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	EditMenu(final MainFrame parent){
		super("Edit");

		setMnemonic( KeyEvent.VK_E );
		JMenuItem undo = new JMenuItem("Undo");
		undo.setMnemonic( KeyEvent.VK_U );
		undo.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_Z, KeyEvent.CTRL_MASK ) );
		undo.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ){
				if (parent.getSqlPane().canUndo())
					parent.getSqlPane().undo();
			}
		});
		JMenuItem redo = new JMenuItem("Redo");
		redo.setMnemonic( KeyEvent.VK_R );
		redo.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_Y, KeyEvent.CTRL_MASK ) );
		redo.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ){
				if (parent.getSqlPane().canRedo())
					parent.getSqlPane().redo();
			}
		});
		add( undo );
		add( redo );
	}
}
