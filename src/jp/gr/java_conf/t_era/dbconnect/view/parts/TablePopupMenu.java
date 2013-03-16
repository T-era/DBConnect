package jp.gr.java_conf.t_era.dbconnect.view.parts;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.ActionInterruptException;
import jp.gr.java_conf.t_era.dbconnect.model.MessageObserver;
import jp.gr.java_conf.t_era.dbconnect.model.io.CsvWriter;
import jp.gr.java_conf.t_era.view.parts.TableViewport;

public class TablePopupMenu extends JPopupMenu {
	private final TableViewport parent;
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Configure config;

//	private static final String DEFAULT_RESULT_CSV_FILE_NAME = Configure.getString("DEFAULT_RESULT_CSV_FILE_NAME");
	private final boolean defaultOverWrite;
	private final MessageObserver messageObserver;

	public TablePopupMenu(final TableViewport parent, boolean defaultOverWriteSqlFile, MessageObserver messageObserver, Configure config){
		JMenuItem saveCsv = new JMenuItem("Save (csv file)");
		this.config = config;
		saveCsv.setMnemonic('c');
		saveCsv.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveToCsvFile();
				} catch (ActionInterruptException e1) {	}
			}
		});

		add(saveCsv);
		this.parent = parent;
		this.defaultOverWrite = defaultOverWriteSqlFile;
		this.messageObserver = messageObserver;
	}

	/**
	 * 一覧の内容をCSVファイルに書き出します。<br/>
	 * このメソッドの実装では、ダイアログを表示して書き出し先のファイルを決定します。<br/>
	 * 書き出し処理は、{@link CsvWriter}クラスに依存しています。
	 * @throws ActionInterruptException
	 */
	public void saveToCsvFile() throws ActionInterruptException{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setSelectedFile(new File(config.user.getDefaultResultCsvFileName()));
		JCheckBox overWriteFile = new JCheckBox("バックアップを取得せずに上書きする", defaultOverWrite);
		Component component = fileChooser.getComponent(2); // TODO 2??実装依存：何らかの方法で解決できないものか？
		if (component instanceof Container) {
			((Container)component).add(overWriteFile, BorderLayout.SOUTH);
		}

		int ret = fileChooser.showSaveDialog(parent);

		if (ret == JFileChooser.CANCEL_OPTION){
			return;
		}
		File selected = fileChooser.getSelectedFile();
		if (selected == null){
			return;
		}

		CsvWriter writer = new CsvWriter(selected, true, messageObserver);

		writer.write(parent.getTitles(), parent.getValues());
	}
}
