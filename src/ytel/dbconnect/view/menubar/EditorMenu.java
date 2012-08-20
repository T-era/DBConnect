package ytel.dbconnect.view.menubar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import ytel.dbconnect.config.Configure;
import ytel.dbconnect.model.ActionInterruptException;
import ytel.dbconnect.model.LabeledSql;
import ytel.dbconnect.model.SQLReader;
import ytel.dbconnect.model.SQLWriter;
import ytel.dbconnect.view.MainFrame;
import ytel.view.parts.ScrollerWithTextArea;
import ytel.view.parts.labeledtextarea.Labeled;

/**
 * Editorメニューを定義します。<br/>
 * エディタ操作に関する処理群
 * @author y-terada
 *
 */
class EditorMenu extends JMenu{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final FileFilter FILE_FILTER = new FileNameExtensionFilter("SQLファイル", "sql", "txt");

	private final MainFrame parent;
	private final Configure config;

	EditorMenu(final MainFrame parent, final Configure config){
		super("Editor");
		this.parent = parent;
		this.config = config;

		setMnemonic( KeyEvent.VK_R );
		JMenuItem addNew = new JMenuItem("New");
		addNew.setMnemonic( KeyEvent.VK_N );
		addNew.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_N, KeyEvent.CTRL_MASK ) );
		addNew.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ){
				parent.getSqlPane().addNewPane("");
				int index = parent.getSqlPane().getTabCount();
				parent.getSqlPane().setSelectedIndex(index - 1);
			}
		});
		JMenuItem closeAll = new JMenuItem("Close All");
		closeAll.setMnemonic( KeyEvent.VK_C );
		closeAll.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ){
				parent.getSqlPane().closeAll();
			}
		});
		JMenuItem openFile = new JMenuItem("Open File");
		openFile.setMnemonic( KeyEvent.VK_O );
		openFile.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ){
				try {
					openFile();
				} catch (ActionInterruptException e){}
			}
		});
		JMenuItem saveFile = new JMenuItem("Save File");
		saveFile.setMnemonic( KeyEvent.VK_S );
		saveFile.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_S, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK ) );
		saveFile.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent arg0 ){
				try {
					saveFile();
				} catch (ActionInterruptException e){}
			}
		});
		add( addNew );
		add( closeAll );
		add( openFile );
		add( saveFile );
	}

	/**
	 * ファイルを開きます。<br/>
	 * このメソッドの実装では、{@link JFileChooser}でUIから開くファイルを
	 * 指定して読み出します。<p/>
	 * 読み出しの結果は、コンストラクタで指定された{@link MainFrame}に渡します
	 */
	private void openFile() throws ActionInterruptException{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(FILE_FILTER);
		fileChooser.setSelectedFile(new File(config.user.getDefaultSqlFileName()));
		JCheckBox sqlSeparateCheck = new JCheckBox("ファイル読込時に「;」で分割する", true);
		Component component = fileChooser.getComponent(2); // TODO 2??実装依存：何らかの方法で解決できないものか？
		if (component instanceof Container) {
			((Container)component).add(sqlSeparateCheck, BorderLayout.SOUTH);
		}

		int ret = fileChooser.showOpenDialog(this);

		if (ret == JFileChooser.CANCEL_OPTION){
			return;
		}
		File selected = fileChooser.getSelectedFile();
		if (selected == null || ! selected.exists()){
			return;
		}

		readFrom(selected, sqlSeparateCheck.isSelected());
	}
	/**
	 * ファイルから、読み出します。<br/>
	 * separateがtrueの場合、SQL毎に区切って読み出します。<p/>
	 * 読み出しの結果は、コンストラクタで指定された{@link MainFrame}に渡します
	 * @param file 読み込み対象のファイル
	 * @param separate SQLとして区切って読み込むか否か
	 */
	private void readFrom(File file, boolean separate) throws ActionInterruptException{
		SQLReader reader;
		if (separate){
			reader = parent.getMainModule().getSeparateSQLReader(file);
		}else{
			reader = parent.getMainModule().getSingleSQLReader(file);
		}
		try{
			while (reader.hasMore()) {
				LabeledSql sql = reader.read();
				parent.getSqlPane().addNewPane(sql.getLabel(), sql.getSql());
			}
		}finally{
			reader.close();
		}
		parent.setMessage("読み込み完了");
	}

	/**
	 * SQLをファイルに保存します。<br/>
	 * このメソッドの実装では、{@link JFileChooser}でUIから保存先を指定します。<p/>
	 * 書き出す内容は、コンストラクタで指定された{@link MainFrame#getSqlPane()}から取得します
	 */
	private void saveFile() throws ActionInterruptException{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(FILE_FILTER);
		fileChooser.setSelectedFile(new File(config.user.getDefaultSqlFileName()));
		JCheckBox overWriteFile = new JCheckBox("バックアップを取得せずに上書きする", config.system.getDefaultOverWriteSqlFile() );
		Component component = fileChooser.getComponent(2); // TODO 2??実装依存：何らかの方法で解決できないものか？
		if (component instanceof Container) {
			((Container)component).add(overWriteFile, BorderLayout.SOUTH);
		}

		int ret = fileChooser.showSaveDialog(this);

		if (ret == JFileChooser.CANCEL_OPTION){
			return;
		}
		File selected = fileChooser.getSelectedFile();
		if (selected == null){
			return;
		}

		if (selected.exists() && ! overWriteFile.isSelected()){
			backup(selected);
		}
		write(selected);
	}

	/**
	 * 指定されたファイルと同名のファイルが、ファイルシステムに存在する場合、
	 * "_bk"のサフィックスを付けて退避します。<br/>
	 * このメソッドの実装では、再帰的に退避処理を行います。
	 * @param file バックアップ処理を行うファイル
	 */
	private void backup(File file){
		File toFile = new File(file.getPath() + "_bk");
		if (toFile.exists()){
			backup(toFile);
		}
		file.renameTo(toFile);
	}
	/**
	 * 指定されたファイルにエディタの内容を出力します。
	 *
	 * 指定されたファイルがなければ、新規に作成します。
	 * @param file 出力先のファイル
	 */
	private void write(File file) throws ActionInterruptException{
		SQLWriter writer = parent.getMainModule().getSingleFileSQLWriter(file);
		try{
			Map<Labeled, ScrollerWithTextArea> tabes = parent.getSqlPane().getTabes();
			for(Labeled key: tabes.keySet()){
				writer.write(key.getLabel().getText(), tabes.get(key).getTextArea().getText());
			}
		}finally{
			writer.close();
		}
		parent.setMessage("書き込み完了");
	}
}
