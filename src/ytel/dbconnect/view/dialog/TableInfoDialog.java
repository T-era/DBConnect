package ytel.dbconnect.view.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;

import ytel.dbconnect.model.ActionInterruptException;
import ytel.dbconnect.model.QueryResultView;
import ytel.dbconnect.view.MainFrame;
import ytel.dbconnect.view.dialog.parts.SearchActionListener;
import ytel.dbconnect.view.dialog.parts.SearchArea;
import ytel.dbconnect.view.parts.TableListTableViewport;

/**
 * テーブルの一覧を表示するダイアログ
 * @author y-terada
 *
 */
public class TableInfoDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final String DIALOG_NAME = "Table Info";
	/**
	 * 表形式のViewport
	 */
	private final TableListTableViewport tableViewport;

	private final JScrollPane scrollPane = new JScrollPane();

	private Object[] titleList;
	private Object[][] valueList;

	private TableInfoDialog( String title, Object[] titleList, Object[][] valueList, MainFrame parent ){
		super();
		super.setTitle(title);
		this.titleList = titleList;
		this.valueList = valueList;
		tableViewport = new TableListTableViewport( parent, titleList, valueList );
		constConstractor();
	}

	/**
	 * オブジェクトの初期化を行います。<br/>
	 * コンストラクタ内で一度だけコールされるメソッドです。<p/>
	 * ダイアログ内のコンポーネントを初期化します。
	 */
	private void constConstractor(){
		this.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		scrollPane.setViewport( tableViewport );

		Component searchView = new SearchArea(new SearchActionListener() {
			@Override
			public void searchPushed(String condition, boolean asRegex) {
				filterSet(condition, asRegex);
			}
		}).getComponent();
		con.add(searchView, BorderLayout.NORTH);
		con.add(scrollPane, BorderLayout.CENTER);
		this.pack();
	}
	private void filterSet(String condition, boolean asRegex) {
		List<Object[]> filteredValue = new ArrayList<Object[]>();
		for (Object[] line : valueList) {
			if (asRegex) {
				if (((String)line[0]).matches(condition)) {
					filteredValue.add(line);
				}
			} else {
				if (((String)line[0]).startsWith(condition)) {
					filteredValue.add(line);
				}
			}
		}
		Object[][] filtered = new Object[filteredValue.size()][];

		tableViewport.setDatas(titleList, filteredValue.toArray(filtered));
	}

	/**
	 * 表示内容を指定します。
	 * @param titleList 表示内容(項目名一覧)
	 * @param valueList 表示内容(一覧表)
	 */
	public void setValue( Object[] titleList, Object[][] valueList ){
		this.titleList = titleList;
		this.valueList = valueList;
		tableViewport.setDatas( titleList, valueList );
	}

	/**
	 * テーブル一覧ダイアログを生成します。<br/>
	 * ダイアログ生成のためにDBにテーブル一覧取得の要求をかけ、
	 * 取得した情報を表示するダイアログインスタンスを返します。
	 * @param parent ダイアログ生成元の{@link MainFrame}インスタンス
	 * @return テーブル一覧ダイアログオブジェクト
	 * @throws ActionInterruptException
	 */
	public static TableInfoDialog getTableInfoDialog(MainFrame parent) throws ActionInterruptException{
		QueryResultView tableViewInfo = parent.getMainModule().getTableInfo();

		return new TableInfoDialog( DIALOG_NAME, tableViewInfo.getTitleArray(), tableViewInfo.getValueArray(), parent );
	}
}
