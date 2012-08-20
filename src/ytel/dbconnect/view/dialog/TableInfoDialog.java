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
 * �e�[�u���̈ꗗ��\������_�C�A���O
 * @author y-terada
 *
 */
public class TableInfoDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final String DIALOG_NAME = "Table Info";
	/**
	 * �\�`����Viewport
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
	 * �I�u�W�F�N�g�̏��������s���܂��B<br/>
	 * �R���X�g���N�^���ň�x�����R�[������郁�\�b�h�ł��B<p/>
	 * �_�C�A���O���̃R���|�[�l���g�����������܂��B
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
	 * �\�����e���w�肵�܂��B
	 * @param titleList �\�����e(���ږ��ꗗ)
	 * @param valueList �\�����e(�ꗗ�\)
	 */
	public void setValue( Object[] titleList, Object[][] valueList ){
		this.titleList = titleList;
		this.valueList = valueList;
		tableViewport.setDatas( titleList, valueList );
	}

	/**
	 * �e�[�u���ꗗ�_�C�A���O�𐶐����܂��B<br/>
	 * �_�C�A���O�����̂��߂�DB�Ƀe�[�u���ꗗ�擾�̗v���������A
	 * �擾��������\������_�C�A���O�C���X�^���X��Ԃ��܂��B
	 * @param parent �_�C�A���O��������{@link MainFrame}�C���X�^���X
	 * @return �e�[�u���ꗗ�_�C�A���O�I�u�W�F�N�g
	 * @throws ActionInterruptException
	 */
	public static TableInfoDialog getTableInfoDialog(MainFrame parent) throws ActionInterruptException{
		QueryResultView tableViewInfo = parent.getMainModule().getTableInfo();

		return new TableInfoDialog( DIALOG_NAME, tableViewInfo.getTitleArray(), tableViewInfo.getValueArray(), parent );
	}
}
