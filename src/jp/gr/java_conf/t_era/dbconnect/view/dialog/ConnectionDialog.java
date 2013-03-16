package jp.gr.java_conf.t_era.dbconnect.view.dialog;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.view.MainFrame;
import jp.gr.java_conf.t_era.view.parts.AgreementListener;
import jp.gr.java_conf.t_era.view.parts.OkCancelComponent;

/**
 * データベースとの接続情報を入力するダイアログ
 * @author y-terada
 *
 */
public class ConnectionDialog extends JDialog implements AgreementListener{
	private static final long serialVersionUID = 1L;

	/**
	 * 接続URL
	 */
	private String url;
	/**
	 * DBユーザ名
	 */
	private String userName;
	/**
	 * DBパスワード
	 */
	private String password;

	/**
	 * このダイアログ閉じられた場合について、がキャンセルされた場合true
	 */
	private boolean canceled = true;

	/**
	 * 警告表示欄
	 */
	private final JLabel alertLabel;
	/**
	 * URL入力欄
	 */
	private final JTextField urlField;
	/**
	 * DBユーザ名入力欄
	 */
	private final JTextField userNameField;
	/**
	 * DBパスワード入力欄
	 */
	private final JTextField passwordField;
	/**
	 * 詳細設定コンテナ
	 */
	private final ConnectionDialogCenterContainer conCenter;

	private final MainFrame parent;
	private final Configure config;

	/**
	 * アプリケーションの初期設定を元に、接続情報を指定するダイアログインスタンスを生成します。
	 * @param parent ダイアログの親ウィンドウ
	 */
	public ConnectionDialog(MainFrame parent, Configure config){
		this( parent
				, config
				, config.user.getDefaultUrl()
				, config.user.getDefaultUserName()
				, config.user.getDefaultPassword()
				, config.user.getDefaultAutoCommit()
				, config.user.getDefaultReadOnly());
	}

	/**
	 * 指定されたデフォルト表示で、接続情報を指定するダイアログインスタンスを生成します。
	 * @param parent ダイアログの親ウィンドウ
	 * @param defaultUrl 接続URL入力欄のデフォルト表示
	 * @param defaultUserName 接続URL入力欄のデフォルト表示
	 * @param defaultPassword DBパスワード入力欄のデフォルト表示
	 * @param defaultAutoCommit 自動コミットのデフォルト指定
	 * @param defaultReadOnly 読込み専用のデフォルト指定
	 */
	public ConnectionDialog(MainFrame parent, Configure config, String defaultUrl, String defaultUserName, String defaultPassword, boolean defaultAutoCommit, boolean defaultReadOnly){
		super(parent, "Configure", true);
		this.config = config;
		setSize(400, 213);

		this.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		alertLabel = new JLabel();
		urlField = new JTextField(defaultUrl);
		userNameField = new JTextField(defaultUserName);
		passwordField = new JPasswordField(defaultPassword);
		this.parent = parent;
		this.setLocation(parent.getLocationOnScreen());


		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		conCenter = new ConnectionDialogCenterContainer(urlField, userNameField, passwordField, alertLabel, defaultAutoCommit, defaultReadOnly);
		Container conSouth = OkCancelComponent.getOkCancelComponent(this);
		con.add(conCenter, BorderLayout.CENTER);
		con.add(conSouth, BorderLayout.SOUTH);

		setVisible( true );
	}

	/**
	 * このダイアログがキャンセルされた場合true
	 * @return このダイアログがキャンセルされた場合true
	 */
	public boolean isCanceled(){
		return canceled;
	}

	/**
	 * ダイアログに入力された接続URL
	 * @return 接続URL
	 */
	public String getUrl(){
		if( canceled ){
			throw new IllegalStateException("コード上の問題。メソッドコールの前にisCanceled()チェックが必要。");
		}
		return url;
	}
	/**
	 * ダイアログに入力されたDBユーザ名
	 * @return DBユーザ名
	 */
	public String getUserName(){
		if( canceled ){
			throw new IllegalStateException("コード上の問題。メソッドコールの前にisCanceled()チェックが必要。");
		}
		return userName;
	}
	/**
	 * ダイアログに入力されたDBパスワード
	 * @return DBパスワード
	 */
	public String getPassword(){
		if( canceled ){
			throw new IllegalStateException("コード上の問題。メソッドコールの前にisCanceled()チェックが必要。");
		}
		return password;
	}
	/**
	 * ダイアログで指定された自動コミット指定
	 * @return 自動コミット指定
	 */
	public boolean getAutoCommit(){
		return conCenter.autoCommit();
	}
	/**
	 * ダイアログで指定された読込み専用設定
	 * @return 読込み専用設定
	 */
	public boolean getReadOnly(){
		return conCenter.readOnly();
	}

	/**
	 * このダイアログの、キャンセル時の動作
	 */
	@Override
	public void cancelAction() {
		canceled = true;
		dispose();
	}

	/**
	 * このダイアログの、完了時の動作
	 */
	@Override
	public void okAction(){
		parent.getMainModule().checkConnectInfo(config.user.getDriverName(), urlField.getText(), userNameField.getText(), passwordField.getText(), config.user.getConnectionCheckSql());

		url = urlField.getText();
		userName = userNameField.getText();
		password = passwordField.getText();
		canceled = false;

		dispose();
	}
}
