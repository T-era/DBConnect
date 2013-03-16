package jp.gr.java_conf.t_era.dbconnect.view;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.ActionInterruptException;
import jp.gr.java_conf.t_era.dbconnect.model.ConnectionInfo;
import jp.gr.java_conf.t_era.dbconnect.model.ConnectionObserver;
import jp.gr.java_conf.t_era.dbconnect.model.MainModule;
import jp.gr.java_conf.t_era.dbconnect.model.MainModuleImpl;
import jp.gr.java_conf.t_era.dbconnect.model.MessageObserver;
import jp.gr.java_conf.t_era.dbconnect.model.QueryResultView;
import jp.gr.java_conf.t_era.dbconnect.model.database.QueryExecuter;
import jp.gr.java_conf.t_era.dbconnect.view.dialog.ColumnInfoDialog;
import jp.gr.java_conf.t_era.dbconnect.view.dialog.ConnectionDialog;
import jp.gr.java_conf.t_era.dbconnect.view.dialog.ParametersDialog;
import jp.gr.java_conf.t_era.dbconnect.view.dialog.ResultTableDialog;
import jp.gr.java_conf.t_era.dbconnect.view.dialog.TableInfoDialog;
import jp.gr.java_conf.t_era.dbconnect.view.menubar.DBConnectMenuBar;
import jp.gr.java_conf.t_era.dbconnect.view.parts.ResultContainer;
import jp.gr.java_conf.t_era.dbconnect.view.parts.StatusBarContainer;

import jp.gr.java_conf.t_era.view.BorderLayoutBuilder;
import jp.gr.java_conf.t_era.view.dialog.ErrorDialog;
import jp.gr.java_conf.t_era.view.parts.labeledtextarea.TextAreaTabbedPane;

/**
 * DBConnectのメインフレーム
 * @author y-terada
 *
 */
public class MainFrame extends JFrame implements MessageObserver {
	private static final long serialVersionUID = 1L;
	/**
	 * メインフレームで使用する推奨フォント
	 */
	public static final Font RECOMMENDED_FONT = new Font( "Monospaced", Font.PLAIN, 12 );

	/**
	 * SQL入力ペイン(テキストエリア)
	 */
	private final TextAreaTabbedPane sqlPane;
	/**
	 * SQL実行結果表示フィールド(一覧表)
	 */
	private final ResultContainer resultField;

	/**
	 * DBConnectアプリのモジュールパート
	 */
	private final MainModule dbConnectModule;

	/**
	 * テーブル一覧ダイアログ
	 */
	private TableInfoDialog tableInfoDialog = null;
	/**
	 * カラム一覧ダイアログ
	 */
	private ColumnInfoDialog columnInfoDialog = null;
	/**
	 * ステータスバー(画面下部のバー)
	 */
	private final StatusBarContainer statusField = new StatusBarContainer();

	public MainFrame(Configure config) throws ActionInterruptException{
		super(config.system.getAppTitle());
		this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		this.setSize(640,400);

		dbConnectModule = new MainModuleImpl(config.user.getDriverName(), config.user.getConnectionCheckSql(), this, config);
		dbConnectModule.addConnectionObserver(new ConnectionObserver(){
			@Override
			protected void actionPerformed(Connection newConnection, ConnectionInfo newInfo) {
				statusField.setUrl(newInfo.getUrl(), newInfo.getUserName());
			}
		});

		this.addWindowListener( new MainFrameAction() );
		this.setJMenuBar( new DBConnectMenuBar(this, config) );
		Container con = this.getContentPane();

		this.resultField = new ResultContainer(this, config);
		sqlPane = new TextAreaTabbedPane(this, config.user.getDefaultSql(), config.system.getLabelNameTemplate(), RECOMMENDED_FONT);
		JSplitPane centerPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, sqlPane, resultField );
		centerPane.setDividerLocation( sqlPane.getPreferredSize().height );

		BorderLayoutBuilder blb = new BorderLayoutBuilder();
		blb.add(centerPane, BorderLayoutBuilder.Direction.Center);
		blb.add(statusField, BorderLayoutBuilder.Direction.South);
		blb.layoutComponent(con);

		this.setVisible( true );

		ConnectionDialog dialogObj = new ConnectionDialog(this, config);
		if( dialogObj.isCanceled() ){
			System.out.println("キャンセル終了。");
			this.dispose();
			System.exit(0);
		}

		setConnectInfo(dialogObj.getUrl(), dialogObj.getUserName(), dialogObj.getPassword(), dialogObj.getAutoCommit(), dialogObj.getReadOnly() );
	}

	/**
	 * オブジェクトに接続情報を設定を設定します。
	 * @param newUrl DBの接続URL
	 * @param newUserName DBのユーザ名
	 * @param newPassword DBの接続パスワード
	 * @param autoCommit コネクションの自動コミット設定
	 * @param readOnly コネクションの読込み専用設定
	 * @throws ActionInterruptException
	 */
	private void setConnectInfo(String newUrl, String newUserName, String newPassword, boolean autoCommit, boolean readOnly) throws ActionInterruptException{
		dbConnectModule.setDataBaseInfo(newUrl, newUserName, newPassword, autoCommit, readOnly);

		dbConnectModule.refleshConnection();
	}

	/**
	 * 検索実行のアクション
	 */
	public void doAction() throws ActionInterruptException{
		QueryExecuter eq = dbConnectModule.getQueryExecter(null);

		QueryResultView tableInfo = eq.exec(sqlPane.getCurrentTextArea().getText(), this);

		resultField.setDatas(tableInfo.getTitleArray(),tableInfo.getValueArray());
		showQueryResultStatus(tableInfo);

	}

	/**
	 * 検索実行のアクション(パラメータあり)
	 */
	public void doActionWithArgs() throws ActionInterruptException{
		String sql = sqlPane.getCurrentTextArea().getText();
		QueryResultView tableInfo = queryExecute(sql);

		resultField.setDatas(tableInfo.getTitleArray(),tableInfo.getValueArray());
		showQueryResultStatus(tableInfo);
	}

	public void doActionWithArgsNewWindow() throws ActionInterruptException{
		String sql = sqlPane.getCurrentTextArea().getText();
		QueryResultView tableInfo = queryExecute(sql);

		resultField.setDatas(tableInfo.getTitleArray(),tableInfo.getValueArray());
		showQueryResultStatus(tableInfo);
		ResultTableDialog.viewNewDialog(sql, tableInfo);
	}

	private QueryResultView queryExecute(String sql) throws ActionInterruptException {
		int countQuestion = dbConnectModule.getParameterCount(sql);

		List<String> params = null;
		if (countQuestion > 0){
			params = getParameters(countQuestion);
			if (params == null){
				throw new ActionInterruptException("コーディングエラー", null);
			}
		}
		QueryExecuter eq = dbConnectModule.getQueryExecter(params);

		return eq.exec(sql, this);
	}

	private void showQueryResultStatus(QueryResultView view) {
		statusField.setTime(view.getExecuteTime());
		statusField.setCount(view.getNumOfResult());
	}

	/**
	 * テーブル一覧ダイアログを表示します。
	 * @throws ActionInterruptException
	 */
	public void showTableInfoDialog() throws ActionInterruptException{
		if( tableInfoDialog == null ){
			tableInfoDialog = TableInfoDialog.getTableInfoDialog(this);
		}else{
			QueryResultView tableViewInfo = dbConnectModule.getTableInfo();
			tableInfoDialog.setValue(tableViewInfo.getTitleArray(), tableViewInfo.getValueArray());
		}
		tableInfoDialog.setVisible( true );
	}

	/**
	 * カラム一覧ダイアログを表示します。
	 * @param tableName  表示対象のテーブル名
	 * @param openNewDialog 新規でウィンドウを開く場合true。
	 *   falseを指定すると、すでにカラムビューが開いていればそのビューに表示します。
	 * @throws ActionInterruptException
	 */
	public void showColumnInfo( String tableName, boolean openNewDialog ) throws ActionInterruptException{
		ColumnInfoDialog temp;
		if (openNewDialog) {
			temp = ColumnInfoDialog.getColumnInfoDialog(tableName, this);
			temp.setVisible(true);
		} else {
			if( columnInfoDialog == null ){
				columnInfoDialog = ColumnInfoDialog.getColumnInfoDialog(tableName, this );
			}else{
				columnInfoDialog.setTitle( tableName );

				QueryResultView columnViewInfo = dbConnectModule.getColumnInfo(tableName);
				columnInfoDialog.setValue(columnViewInfo.getTitleArray(), columnViewInfo.getValueArray());
			}
			columnInfoDialog.setVisible( true );
		}
	}


	/**
	 * モジュールパートを返します。
	 * @return モジュールパート
	 */
	public MainModule getMainModule() {
		return dbConnectModule;
	}

	/**
	 * フレームに含まれるSQL入力ペインを返します。
	 * @return SQL入力ペイン
	 */
	public TextAreaTabbedPane getSqlPane(){
		return sqlPane;
	}
	/**
	 * フレームに含まれるSQL実行結果表示フィールドを返します
	 * @return SQL実行結果表示フィールド
	 */
	public ResultContainer getResuletField(){
		return resultField;
	}

	/**
	 * UIを通じてSQLパラメータに置き換える文字列を取得します。<br/>
	 * SQLパラメータの数は、引数で与えられます。
	 * <p/>この実装では、ダイアログでUIから置換え文字列を取得します。
	 * @param countQuestion SQLパラメータの数
	 * @return 取得した置換え文字列
	 */
	private List<String> getParameters(int countQuestion){
		ParametersDialog dialog = new ParametersDialog(this, countQuestion);
		dialog.setVisible(true);
		List<String> ret = dialog.getParameters();
		return ret;
	}

	/**
	 * メインフレームに対するウィンドウ操作のアクション
	 * @author y-terada
	 *
	 */
	private class MainFrameAction extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent arg0){
			sqlPane.closeAll();
			if (sqlPane.getTabCount() == 0){
				dbConnectModule.close();
				System.exit(0);
			}
		}
	}

	@Override
	public void setMessage(String message) {
		this.statusField.setMessage(MessageFormat.format("{0}({1,time})", new Object[]{message, new Date()}));
	}

	@Override
	public void setErrorMessage(String message){
		this.statusField.setMessage(MessageFormat.format("Error:{0}({1,time})", new Object[]{message, new Date()}));
	}

	@Override
	public void setErrorMessage(String message, Throwable t){
		this.statusField.setMessage(MessageFormat.format("Error:{0}({1,time}):{2}", new Object[]{message, new Date(), t.getMessage()}));
	}

	@Override
	public void setFatalMessage(String message){
		new ErrorDialog(message, null, this).setVisible(true);

	}

	@Override
	public void setFatalMessage(String message, Throwable t){
		new ErrorDialog(message, t, this).setVisible(true);
	}
}
