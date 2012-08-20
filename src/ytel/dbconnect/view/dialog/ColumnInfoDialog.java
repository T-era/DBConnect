package ytel.dbconnect.view.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import ytel.dbconnect.model.ActionInterruptException;
import ytel.dbconnect.model.QueryResultView;
import ytel.dbconnect.view.MainFrame;
import ytel.view.parts.TableViewport;

/**
 * カラム一覧ダイアログ<br/>
 * (指定されたテーブルのカラム一覧を表示するダイアログ)
 * @author y-terada
 *
 */
public class ColumnInfoDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	/**
	 * 表形式のViewport
	 */
	private final TableViewport tableViewport;
	/**
	 * ダイアログのタイトルのテンプレート
	 */
	private static final String TITLE_TEMPLATE = "Column Info( Table:### )";
	/**
	 * ダイアログのタイトルのテンプレートの置き換え文字(この文字列と一致する部分にテーブル名が入る)
	 */
	private static final String TITLE_TEMPLATE_ENTRY = "###";

	private final JScrollPane scrollPane = new JScrollPane();
	/**
	 * 指定されたテーブル名で、ダイアログオブジェクトを生成します。
	 * @param tableName テーブル名
	 * @param titleList 表示内容(項目名一覧)
	 * @param valueList 表示内容(一覧表)
	 * @param parent 呼び出し元の{@link MainFrame}
	 */
	private ColumnInfoDialog( String tableName, Object[] titleList, Object[][] valueList, Frame parent ){
		super();
		tableViewport = new TableViewport( titleList, valueList, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, MainFrame.RECOMMENDED_FONT );
		constConstractor(tableName);
	}
	/**
	 * オブジェクトの初期化を行います。<br/>
	 * コンストラクタ内で一度だけコールされるメソッドです。<p/>
	 * ダイアログ内のコンポーネントを初期化します。
	 * @param tableName テーブル名
	 */
	private void constConstractor( String tableName ){
		this.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		setTitle( tableName );
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		scrollPane.setViewport( tableViewport );

		con.add(scrollPane, BorderLayout.CENTER);
		this.pack();
	}

	/**
	 * 表示内容を指定します。
	 * @param titleList 表示内容(項目名一覧)
	 * @param valueList 表示内容(一覧表)
	 */
	public void setValue( Object[] titleList, Object[][] valueList ){
		tableViewport.setDatas( titleList, valueList );
	}

	/**
	 * ダイアログタイトルを変更します。
	 */
	public void setTitle( String tableName ){
		super.setTitle( TITLE_TEMPLATE.replaceAll( TITLE_TEMPLATE_ENTRY, tableName ) );
	}

	/**
	 * テーブル名から、カラム一覧ダイアログを生成します。<br/>
	 * ダイアログ生成のためにDBにカラム一覧取得の要求をかけ、
	 * 取得した情報を表示するダイアログインスタンスを返します。
	 * @param tableName テーブル名
	 * @param parent ダイアログ生成元の{@link MainFrame}インスタンス
	 * @return カラム一覧ダイアログオブジェクト
	 * @throws ActionInterruptException 
	 */
	public static ColumnInfoDialog getColumnInfoDialog( String tableName, MainFrame parent ) throws ActionInterruptException{
		QueryResultView columnViewInfo = parent.getMainModule().getColumnInfo(tableName);
		return new ColumnInfoDialog(tableName, columnViewInfo.getTitleArray(), columnViewInfo.getValueArray(), parent );
	}
}
