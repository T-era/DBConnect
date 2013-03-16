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
 * DBConnect�̃��C���t���[��
 * @author y-terada
 *
 */
public class MainFrame extends JFrame implements MessageObserver {
	private static final long serialVersionUID = 1L;
	/**
	 * ���C���t���[���Ŏg�p���鐄���t�H���g
	 */
	public static final Font RECOMMENDED_FONT = new Font( "Monospaced", Font.PLAIN, 12 );

	/**
	 * SQL���̓y�C��(�e�L�X�g�G���A)
	 */
	private final TextAreaTabbedPane sqlPane;
	/**
	 * SQL���s���ʕ\���t�B�[���h(�ꗗ�\)
	 */
	private final ResultContainer resultField;

	/**
	 * DBConnect�A�v���̃��W���[���p�[�g
	 */
	private final MainModule dbConnectModule;

	/**
	 * �e�[�u���ꗗ�_�C�A���O
	 */
	private TableInfoDialog tableInfoDialog = null;
	/**
	 * �J�����ꗗ�_�C�A���O
	 */
	private ColumnInfoDialog columnInfoDialog = null;
	/**
	 * �X�e�[�^�X�o�[(��ʉ����̃o�[)
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
			System.out.println("�L�����Z���I���B");
			this.dispose();
			System.exit(0);
		}

		setConnectInfo(dialogObj.getUrl(), dialogObj.getUserName(), dialogObj.getPassword(), dialogObj.getAutoCommit(), dialogObj.getReadOnly() );
	}

	/**
	 * �I�u�W�F�N�g�ɐڑ�����ݒ��ݒ肵�܂��B
	 * @param newUrl DB�̐ڑ�URL
	 * @param newUserName DB�̃��[�U��
	 * @param newPassword DB�̐ڑ��p�X���[�h
	 * @param autoCommit �R�l�N�V�����̎����R�~�b�g�ݒ�
	 * @param readOnly �R�l�N�V�����̓Ǎ��ݐ�p�ݒ�
	 * @throws ActionInterruptException
	 */
	private void setConnectInfo(String newUrl, String newUserName, String newPassword, boolean autoCommit, boolean readOnly) throws ActionInterruptException{
		dbConnectModule.setDataBaseInfo(newUrl, newUserName, newPassword, autoCommit, readOnly);

		dbConnectModule.refleshConnection();
	}

	/**
	 * �������s�̃A�N�V����
	 */
	public void doAction() throws ActionInterruptException{
		QueryExecuter eq = dbConnectModule.getQueryExecter(null);

		QueryResultView tableInfo = eq.exec(sqlPane.getCurrentTextArea().getText(), this);

		resultField.setDatas(tableInfo.getTitleArray(),tableInfo.getValueArray());
		showQueryResultStatus(tableInfo);

	}

	/**
	 * �������s�̃A�N�V����(�p�����[�^����)
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
				throw new ActionInterruptException("�R�[�f�B���O�G���[", null);
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
	 * �e�[�u���ꗗ�_�C�A���O��\�����܂��B
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
	 * �J�����ꗗ�_�C�A���O��\�����܂��B
	 * @param tableName  �\���Ώۂ̃e�[�u����
	 * @param openNewDialog �V�K�ŃE�B���h�E���J���ꍇtrue�B
	 *   false���w�肷��ƁA���łɃJ�����r���[���J���Ă���΂��̃r���[�ɕ\�����܂��B
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
	 * ���W���[���p�[�g��Ԃ��܂��B
	 * @return ���W���[���p�[�g
	 */
	public MainModule getMainModule() {
		return dbConnectModule;
	}

	/**
	 * �t���[���Ɋ܂܂��SQL���̓y�C����Ԃ��܂��B
	 * @return SQL���̓y�C��
	 */
	public TextAreaTabbedPane getSqlPane(){
		return sqlPane;
	}
	/**
	 * �t���[���Ɋ܂܂��SQL���s���ʕ\���t�B�[���h��Ԃ��܂�
	 * @return SQL���s���ʕ\���t�B�[���h
	 */
	public ResultContainer getResuletField(){
		return resultField;
	}

	/**
	 * UI��ʂ���SQL�p�����[�^�ɒu�������镶������擾���܂��B<br/>
	 * SQL�p�����[�^�̐��́A�����ŗ^�����܂��B
	 * <p/>���̎����ł́A�_�C�A���O��UI����u������������擾���܂��B
	 * @param countQuestion SQL�p�����[�^�̐�
	 * @return �擾�����u����������
	 */
	private List<String> getParameters(int countQuestion){
		ParametersDialog dialog = new ParametersDialog(this, countQuestion);
		dialog.setVisible(true);
		List<String> ret = dialog.getParameters();
		return ret;
	}

	/**
	 * ���C���t���[���ɑ΂���E�B���h�E����̃A�N�V����
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
