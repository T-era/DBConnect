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
 * Editor���j���[���`���܂��B<br/>
 * �G�f�B�^����Ɋւ��鏈���Q
 * @author y-terada
 *
 */
class EditorMenu extends JMenu{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final FileFilter FILE_FILTER = new FileNameExtensionFilter("SQL�t�@�C��", "sql", "txt");

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
	 * �t�@�C�����J���܂��B<br/>
	 * ���̃��\�b�h�̎����ł́A{@link JFileChooser}��UI����J���t�@�C����
	 * �w�肵�ēǂݏo���܂��B<p/>
	 * �ǂݏo���̌��ʂ́A�R���X�g���N�^�Ŏw�肳�ꂽ{@link MainFrame}�ɓn���܂�
	 */
	private void openFile() throws ActionInterruptException{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(FILE_FILTER);
		fileChooser.setSelectedFile(new File(config.user.getDefaultSqlFileName()));
		JCheckBox sqlSeparateCheck = new JCheckBox("�t�@�C���Ǎ����Ɂu;�v�ŕ�������", true);
		Component component = fileChooser.getComponent(2); // TODO 2??�����ˑ��F���炩�̕��@�ŉ����ł��Ȃ����̂��H
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
	 * �t�@�C������A�ǂݏo���܂��B<br/>
	 * separate��true�̏ꍇ�ASQL���ɋ�؂��ēǂݏo���܂��B<p/>
	 * �ǂݏo���̌��ʂ́A�R���X�g���N�^�Ŏw�肳�ꂽ{@link MainFrame}�ɓn���܂�
	 * @param file �ǂݍ��ݑΏۂ̃t�@�C��
	 * @param separate SQL�Ƃ��ċ�؂��ēǂݍ��ނ��ۂ�
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
		parent.setMessage("�ǂݍ��݊���");
	}

	/**
	 * SQL���t�@�C���ɕۑ����܂��B<br/>
	 * ���̃��\�b�h�̎����ł́A{@link JFileChooser}��UI����ۑ�����w�肵�܂��B<p/>
	 * �����o�����e�́A�R���X�g���N�^�Ŏw�肳�ꂽ{@link MainFrame#getSqlPane()}����擾���܂�
	 */
	private void saveFile() throws ActionInterruptException{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(FILE_FILTER);
		fileChooser.setSelectedFile(new File(config.user.getDefaultSqlFileName()));
		JCheckBox overWriteFile = new JCheckBox("�o�b�N�A�b�v���擾�����ɏ㏑������", config.system.getDefaultOverWriteSqlFile() );
		Component component = fileChooser.getComponent(2); // TODO 2??�����ˑ��F���炩�̕��@�ŉ����ł��Ȃ����̂��H
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
	 * �w�肳�ꂽ�t�@�C���Ɠ����̃t�@�C�����A�t�@�C���V�X�e���ɑ��݂���ꍇ�A
	 * "_bk"�̃T�t�B�b�N�X��t���đޔ����܂��B<br/>
	 * ���̃��\�b�h�̎����ł́A�ċA�I�ɑޔ��������s���܂��B
	 * @param file �o�b�N�A�b�v�������s���t�@�C��
	 */
	private void backup(File file){
		File toFile = new File(file.getPath() + "_bk");
		if (toFile.exists()){
			backup(toFile);
		}
		file.renameTo(toFile);
	}
	/**
	 * �w�肳�ꂽ�t�@�C���ɃG�f�B�^�̓��e���o�͂��܂��B
	 *
	 * �w�肳�ꂽ�t�@�C�����Ȃ���΁A�V�K�ɍ쐬���܂��B
	 * @param file �o�͐�̃t�@�C��
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
		parent.setMessage("�������݊���");
	}
}
