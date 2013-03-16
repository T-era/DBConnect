package jp.gr.java_conf.t_era.dbconnect.view.dialog;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import jp.gr.java_conf.t_era.dbconnect.model.QueryResultView;
import jp.gr.java_conf.t_era.dbconnect.view.MainFrame;
import jp.gr.java_conf.t_era.view.parts.TableViewport;

public final class ResultTableDialog extends JDialog{
	private static final long serialVersionUID = 1L;

	private final JScrollPane scrollPane = new JScrollPane();

	/**
	 * 指定された内容を一覧表示するダイアログを生成します。
	 * @param titleダイアログタイトル
	 * @param titleList 一覧表のタイトル
	 * @param valueList 一覧表の中身
	 */
	private ResultTableDialog(String title, Object[] titleList, Object[][] valueList){
		super();
		TableViewport tableViewport = new TableViewport( titleList, valueList, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, MainFrame.RECOMMENDED_FONT );

		this.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		setTitle(title);
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		scrollPane.setViewport( tableViewport );

		con.add(scrollPane, BorderLayout.CENTER);
		this.pack();
	}

	/**
	 * SQL結果をダイアログに表示します。
	 * @param sql 参考情報として表示するSQL
	 * @param sqlResult SQL実行結果ビュー
	 */
	public static void viewNewDialog(String sql, QueryResultView sqlResult){
		new ResultTableDialog(sql, sqlResult.getTitleArray(), sqlResult.getValueArray()).setVisible(true);
	}
}
