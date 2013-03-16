package jp.gr.java_conf.t_era.dbconnect.view.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JRadioButton;

import jp.gr.java_conf.t_era.view.parts.RadioButtonComponent;

/**
 * �f�[�^�x�[�X�Ƃ̐ڑ�������͂���_�C�A���O�́A
 * �ڑ����̏ڍׂ��w�肷��R���e�i
 * @author y-terada
 *
 */
class ConnectionDialogCenterContainer extends Container {
	private static final long serialVersionUID = 1L;

	/**
	 * ���W�I�{�^���@�����R�~�b�g�w��itrue�j
	 */
	private final JRadioButton autoCommitTrue;
	/**
	 * ���W�I�{�^���@�����R�~�b�g�w��ifalse�j
	 */
	private final JRadioButton autoCommitFalse;
	/**
	 * ���W�I�{�^���@�Ǎ��ݐ�p�ݒ�itrue�j
	 */
	private final JRadioButton readOnlyTrue;
	/**
	 * ���W�I�{�^���@�Ǎ��ݐ�p�ݒ�ifalse�j
	 */
	private final JRadioButton readOnlyFalse;

	/**
	 * �R���e�i�𐶐����܂��B
	 * @param urlField �ڑ�URL�w�藓
	 * @param userNameField DB���[�U���w�藓
	 * @param passwordField DB�p�X���[�h�w�藓
	 * @param alertLabel �x���\�����x��
	 * @param defaultAutoCommit �����R�~�b�g�f�t�H���g�w��
	 * @param defaultReadOnly �Ǎ��ݐ�p�f�t�H���g�w��
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
			// �㔼��
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
			// �^��
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
			// ���[
			conSouth.setLayout( new GridLayout(1,0) );
			conSouth.add( alertLabel );
		}
	}

	/**
	 * �_�C�A���O�ł̎w��:�����R�~�b�g
	 * @return �_�C�A���O�ł̎w��
	 */
	boolean autoCommit(){
		return autoCommitTrue.isSelected();
	}
	/**
	 * �_�C�A���O�ł̎w��:�Ǎ��ݐ�p
	 * @return �_�C�A���O�ł̎w��
	 */
	boolean readOnly(){
		return readOnlyTrue.isSelected();
	}
}
