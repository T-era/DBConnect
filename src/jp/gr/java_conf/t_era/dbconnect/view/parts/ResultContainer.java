package jp.gr.java_conf.t_era.dbconnect.view.parts;

import java.awt.Dimension;

import javax.swing.JScrollPane;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.MessageObserver;
import jp.gr.java_conf.t_era.dbconnect.view.MainFrame;
import jp.gr.java_conf.t_era.view.parts.TableViewport;

/**
 * �X�N���[���@�\�t���ňꗗ�\��\������R���e�i�ł��B<br/>
 * @author y-terada
 *
 */
public class ResultContainer extends JScrollPane{
	private static final long serialVersionUID = 1L;

	/**
	 * �ꗗ�\�̕\�����e
	 */
	private final TableViewport viewPort;
	private final Configure config;

	public ResultContainer(MessageObserver messageObserver, Configure config){
		super( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
		this.config = config;
		viewPort = new TableViewport(MainFrame.RECOMMENDED_FONT);
		viewPort.setComponentPopupMenu(new TablePopupMenu(viewPort, config.system.getDefaultOverWriteSqlFile(), messageObserver, config));

		setPreferredSize( new Dimension(200,200) );

		setViewNull();
		setVisible( true );
	}

	/**
	 * �ꗗ�\���X�V���܂��B
	 * @param titleArray �ꗗ�\�̃^�C�g��
	 * @param resultArray �ꗗ�\�̓��e
	 */
	public void setDatas(String[] titleArray, String[][] resultArray) {
		int sizeX = titleArray.length;
		if( sizeX == 0 ){
			setViewNull();
			return;
		}
	
		viewPort.setDatas(titleArray, resultArray);
		this.setViewport( viewPort );
	}

	/**
	 * ��s���ɁA�w�肳�ꂽ�������\�����܂��B
	 * @param dispString
	 */
	public void setViewText( String dispString ){
		viewPort.setDatas( new Object[]{config.system.getResultLabel()}, new Object[][]{{dispString}} );
		this.setViewport( viewPort );
	}

	/**
	 * �ꗗ�\�����Ԃɂ��܂��B
	 */
	public void setViewNull(){
		viewPort.setDatas( new Object[]{config.system.getErrorLabel()}, new Object[][]{{config.system.getNullLabel()}} );
		this.setViewport( viewPort );
	}
}
