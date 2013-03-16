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
 * テーブル情報一覧表示のためのViewportです。<p/>
 * カラム情報一覧表示をサポートするため、マウスクリックイベントを処理します
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
	 * 表示する一覧表にデータを設定します。
	 */
	public void setDatas( Object[] titles, Object[][] values ){
		setDatas(titles, values, new TableViewportTable(values, titles, parent));
	}

	/**
	 * Viewportで表示する一覧表
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
		 * 選択されている項目が変更された場合のアクションです。
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
