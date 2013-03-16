package jp.gr.java_conf.t_era.dbconnect.view.parts;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import jp.gr.java_conf.t_era.dbconnect.model.ActionInterruptException;
import jp.gr.java_conf.t_era.dbconnect.view.MainFrame;
import jp.gr.java_conf.t_era.view.parts.TableViewport;

/**
 * �e�[�u�����ꗗ�\���̂��߂�Viewport�ł��B<p/>
 * �J�������ꗗ�\�����T�|�[�g���邽�߁A�}�E�X�N���b�N�C�x���g���������܂�
 * @author y-terada
 *
 */
public class TableListTableViewport extends TableViewport {
	private static final long serialVersionUID = 1L;
	protected final MainFrame parent;

	public TableListTableViewport( MainFrame parent ){
		this( parent, ListSelectionModel.SINGLE_SELECTION );
	}
	public TableListTableViewport( MainFrame parent, Object[] titles, Object[][] values ){
		this( parent, titles, values, ListSelectionModel.SINGLE_SELECTION );
	}
	public TableListTableViewport( MainFrame parent, int selectionMode ){
		super(selectionMode, new TableViewportTable(parent), MainFrame.RECOMMENDED_FONT);
		this.parent = parent;
	}
	public TableListTableViewport( MainFrame parent, Object[] titles, Object[][] values, int selectionMode  ){
		super(selectionMode, new TableViewportTable(values, titles, parent), MainFrame.RECOMMENDED_FONT);
		this.parent = parent;
		super.titles = titles;
		super.values = values;
	}

	/**
	 * �\������ꗗ�\�Ƀf�[�^��ݒ肵�܂��B
	 */
	public void setDatas( Object[] titles, Object[][] values ){
		setDatas(titles, values, new TableViewportTable(values, titles, parent));
	}

	/**
	 * Viewport�ŕ\������ꗗ�\
	 * @author y-terada
	 *
	 */
	private static class TableViewportTable extends JTable{
		private static final long serialVersionUID = 1L;
		private final MainFrame parent;
		TableViewportTable(MainFrame parent){
			this(new Object[][] {}, new Object[] {}, parent);
		}
		TableViewportTable( Object[][] values, Object[] titles, MainFrame parent ){
			super( values, titles );
			this.parent = parent;
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() != MouseEvent.BUTTON1) {
						try {
							int selectedIndex = rowAtPoint(e.getPoint());
							showColumnInfo(selectedIndex, true);
						} catch (ActionInterruptException ex) {
							throw new RuntimeException(ex);
						}
					}
				}
			});
		}

		/**
		 * �I������Ă��鍀�ڂ��ύX���ꂽ�ꍇ�̃A�N�V�����ł��B
		 */
		@Override
		public void valueChanged(ListSelectionEvent lse){
			super.valueChanged( lse );
			try{
				if( this.contains( lse.getLastIndex(), 0 ) && lse.getValueIsAdjusting() ){
					ListSelectionModel lsm = (ListSelectionModel)lse.getSource();
					showColumnInfo(lsm.getLeadSelectionIndex(), false);
				}
			} catch (ActionInterruptException e) {
				throw new RuntimeException(e);
			}
		}
		private void showColumnInfo(int selectedIndex, boolean openNewDialog) throws ActionInterruptException {
			String tableName = (String)getValueAt(selectedIndex, 0 );
			parent.showColumnInfo( tableName, openNewDialog);
		}
	}
}
