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
 * �f�[�^�x�[�X�Ƃ̐ڑ�������͂���_�C�A���O
 * @author y-terada
 *
 */
public class ConnectionDialog extends JDialog implements AgreementListener{
	private static final long serialVersionUID = 1L;

	/**
	 * �ڑ�URL
	 */
	private String url;
	/**
	 * DB���[�U��
	 */
	private String userName;
	/**
	 * DB�p�X���[�h
	 */
	private String password;

	/**
	 * ���̃_�C�A���O����ꂽ�ꍇ�ɂ��āA���L�����Z�����ꂽ�ꍇtrue
	 */
	private boolean canceled = true;

	/**
	 * �x���\����
	 */
	private final JLabel alertLabel;
	/**
	 * URL���͗�
	 */
	private final JTextField urlField;
	/**
	 * DB���[�U�����͗�
	 */
	private final JTextField userNameField;
	/**
	 * DB�p�X���[�h���͗�
	 */
	private final JTextField passwordField;
	/**
	 * �ڍאݒ�R���e�i
	 */
	private final ConnectionDialogCenterContainer conCenter;

	private final MainFrame parent;
	private final Configure config;

	/**
	 * �A�v���P�[�V�����̏����ݒ�����ɁA�ڑ������w�肷��_�C�A���O�C���X�^���X�𐶐����܂��B
	 * @param parent �_�C�A���O�̐e�E�B���h�E
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
	 * �w�肳�ꂽ�f�t�H���g�\���ŁA�ڑ������w�肷��_�C�A���O�C���X�^���X�𐶐����܂��B
	 * @param parent �_�C�A���O�̐e�E�B���h�E
	 * @param defaultUrl �ڑ�URL���͗��̃f�t�H���g�\��
	 * @param defaultUserName �ڑ�URL���͗��̃f�t�H���g�\��
	 * @param defaultPassword DB�p�X���[�h���͗��̃f�t�H���g�\��
	 * @param defaultAutoCommit �����R�~�b�g�̃f�t�H���g�w��
	 * @param defaultReadOnly �Ǎ��ݐ�p�̃f�t�H���g�w��
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
	 * ���̃_�C�A���O���L�����Z�����ꂽ�ꍇtrue
	 * @return ���̃_�C�A���O���L�����Z�����ꂽ�ꍇtrue
	 */
	public boolean isCanceled(){
		return canceled;
	}

	/**
	 * �_�C�A���O�ɓ��͂��ꂽ�ڑ�URL
	 * @return �ڑ�URL
	 */
	public String getUrl(){
		if( canceled ){
			throw new IllegalStateException("�R�[�h��̖��B���\�b�h�R�[���̑O��isCanceled()�`�F�b�N���K�v�B");
		}
		return url;
	}
	/**
	 * �_�C�A���O�ɓ��͂��ꂽDB���[�U��
	 * @return DB���[�U��
	 */
	public String getUserName(){
		if( canceled ){
			throw new IllegalStateException("�R�[�h��̖��B���\�b�h�R�[���̑O��isCanceled()�`�F�b�N���K�v�B");
		}
		return userName;
	}
	/**
	 * �_�C�A���O�ɓ��͂��ꂽDB�p�X���[�h
	 * @return DB�p�X���[�h
	 */
	public String getPassword(){
		if( canceled ){
			throw new IllegalStateException("�R�[�h��̖��B���\�b�h�R�[���̑O��isCanceled()�`�F�b�N���K�v�B");
		}
		return password;
	}
	/**
	 * �_�C�A���O�Ŏw�肳�ꂽ�����R�~�b�g�w��
	 * @return �����R�~�b�g�w��
	 */
	public boolean getAutoCommit(){
		return conCenter.autoCommit();
	}
	/**
	 * �_�C�A���O�Ŏw�肳�ꂽ�Ǎ��ݐ�p�ݒ�
	 * @return �Ǎ��ݐ�p�ݒ�
	 */
	public boolean getReadOnly(){
		return conCenter.readOnly();
	}

	/**
	 * ���̃_�C�A���O�́A�L�����Z�����̓���
	 */
	@Override
	public void cancelAction() {
		canceled = true;
		dispose();
	}

	/**
	 * ���̃_�C�A���O�́A�������̓���
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
