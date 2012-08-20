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
 * �J�����ꗗ�_�C�A���O<br/>
 * (�w�肳�ꂽ�e�[�u���̃J�����ꗗ��\������_�C�A���O)
 * @author y-terada
 *
 */
public class ColumnInfoDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	/**
	 * �\�`����Viewport
	 */
	private final TableViewport tableViewport;
	/**
	 * �_�C�A���O�̃^�C�g���̃e���v���[�g
	 */
	private static final String TITLE_TEMPLATE = "Column Info( Table:### )";
	/**
	 * �_�C�A���O�̃^�C�g���̃e���v���[�g�̒u����������(���̕�����ƈ�v���镔���Ƀe�[�u����������)
	 */
	private static final String TITLE_TEMPLATE_ENTRY = "###";

	private final JScrollPane scrollPane = new JScrollPane();
	/**
	 * �w�肳�ꂽ�e�[�u�����ŁA�_�C�A���O�I�u�W�F�N�g�𐶐����܂��B
	 * @param tableName �e�[�u����
	 * @param titleList �\�����e(���ږ��ꗗ)
	 * @param valueList �\�����e(�ꗗ�\)
	 * @param parent �Ăяo������{@link MainFrame}
	 */
	private ColumnInfoDialog( String tableName, Object[] titleList, Object[][] valueList, Frame parent ){
		super();
		tableViewport = new TableViewport( titleList, valueList, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, MainFrame.RECOMMENDED_FONT );
		constConstractor(tableName);
	}
	/**
	 * �I�u�W�F�N�g�̏��������s���܂��B<br/>
	 * �R���X�g���N�^���ň�x�����R�[������郁�\�b�h�ł��B<p/>
	 * �_�C�A���O���̃R���|�[�l���g�����������܂��B
	 * @param tableName �e�[�u����
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
	 * �\�����e���w�肵�܂��B
	 * @param titleList �\�����e(���ږ��ꗗ)
	 * @param valueList �\�����e(�ꗗ�\)
	 */
	public void setValue( Object[] titleList, Object[][] valueList ){
		tableViewport.setDatas( titleList, valueList );
	}

	/**
	 * �_�C�A���O�^�C�g����ύX���܂��B
	 */
	public void setTitle( String tableName ){
		super.setTitle( TITLE_TEMPLATE.replaceAll( TITLE_TEMPLATE_ENTRY, tableName ) );
	}

	/**
	 * �e�[�u��������A�J�����ꗗ�_�C�A���O�𐶐����܂��B<br/>
	 * �_�C�A���O�����̂��߂�DB�ɃJ�����ꗗ�擾�̗v���������A
	 * �擾��������\������_�C�A���O�C���X�^���X��Ԃ��܂��B
	 * @param tableName �e�[�u����
	 * @param parent �_�C�A���O��������{@link MainFrame}�C���X�^���X
	 * @return �J�����ꗗ�_�C�A���O�I�u�W�F�N�g
	 * @throws ActionInterruptException 
	 */
	public static ColumnInfoDialog getColumnInfoDialog( String tableName, MainFrame parent ) throws ActionInterruptException{
		QueryResultView columnViewInfo = parent.getMainModule().getColumnInfo(tableName);
		return new ColumnInfoDialog(tableName, columnViewInfo.getTitleArray(), columnViewInfo.getValueArray(), parent );
	}
}
