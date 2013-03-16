package jp.gr.java_conf.t_era.dbconnect.view.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JRadioButton;

import jp.gr.java_conf.t_era.view.parts.RadioButtonComponent;

/**
 * データベースとの接続情報を入力するダイアログの、
 * 接続情報の詳細を指定するコンテナ
 * @author y-terada
 *
 */
class ConnectionDialogCenterContainer extends Container {
	private static final long serialVersionUID = 1L;

	/**
	 * ラジオボタン　自動コミット指定（true）
	 */
	private final JRadioButton autoCommitTrue;
	/**
	 * ラジオボタン　自動コミット指定（false）
	 */
	private final JRadioButton autoCommitFalse;
	/**
	 * ラジオボタン　読込み専用設定（true）
	 */
	private final JRadioButton readOnlyTrue;
	/**
	 * ラジオボタン　読込み専用設定（false）
	 */
	private final JRadioButton readOnlyFalse;

	/**
	 * コンテナを生成します。
	 * @param urlField 接続URL指定欄
	 * @param userNameField DBユーザ名指定欄
	 * @param passwordField DBパスワード指定欄
	 * @param alertLabel 警告表示ラベル
	 * @param defaultAutoCommit 自動コミットデフォルト指定
	 * @param defaultReadOnly 読込み専用デフォルト指定
	 */
	ConnectionDialogCenterContainer( Component urlField, Component userNameField, Component passwordField, Component alertLabel, boolean defaultAutoCommit, boolean defaultReadOnly ){
		Container conNorth = new Container();
		Container conCenter = new Container();
		Container conSouth = new Container();
		setLayout( new BorderLayout() );
		add( conNorth, BorderLayout.NORTH );
		add( conCenter, BorderLayout.CENTER );
		add( conSouth, BorderLayout.SOUTH );
		{
			// 上半分
			conNorth.setLayout( new BorderLayout() );
			Container conNorthCenter = new Container();
			Container conNorthWest = new Container();
			{
				conNorthCenter.setLayout( new GridLayout(3,0) );
				conNorthCenter.add( urlField );
				conNorthCenter.add( userNameField );
				conNorthCenter.add( passwordField );
			}{
				conNorthWest.setLayout( new GridLayout(3,0) );
				conNorthWest.add( new JLabel("URL:") );
				conNorthWest.add( new JLabel("User:") );
				conNorthWest.add( new JLabel("Pass:") );
			}
			conNorth.add( conNorthCenter, BorderLayout.CENTER );
			conNorth.add( conNorthWest, BorderLayout.WEST );
		}{
			// 真中
			conCenter.setLayout( new GridLayout(2,2) );

			autoCommitTrue = new JRadioButton( "True" );
			autoCommitFalse = new JRadioButton( "False" );
			if( defaultAutoCommit ){
				autoCommitTrue.setSelected( true );
			}else{
				autoCommitFalse.setSelected( true );
			}
			Component autoCommit = new RadioButtonComponent("AutoCommit", autoCommitTrue, autoCommitFalse );
			conCenter.add( autoCommit );

			readOnlyTrue = new JRadioButton( "True" );
			readOnlyFalse = new JRadioButton( "False" );
			if( defaultReadOnly ){
				readOnlyTrue.setSelected( true );
			}else{
				readOnlyFalse.setSelected( true );
			}
			Component readOnly = new RadioButtonComponent("ReadOnly", readOnlyTrue, readOnlyFalse );
			conCenter.add( readOnly );
		}{
			// 下端
			conSouth.setLayout( new GridLayout(1,0) );
			conSouth.add( alertLabel );
		}
	}

	/**
	 * ダイアログでの指定:自動コミット
	 * @return ダイアログでの指定
	 */
	boolean autoCommit(){
		return autoCommitTrue.isSelected();
	}
	/**
	 * ダイアログでの指定:読込み専用
	 * @return ダイアログでの指定
	 */
	boolean readOnly(){
		return readOnlyTrue.isSelected();
	}
}
