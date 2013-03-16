package jp.gr.java_conf.t_era.dbconnect.view.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jp.gr.java_conf.t_era.view.parts.AgreementListener;
import jp.gr.java_conf.t_era.view.parts.OkCancelComponent;

/**
 * SQL�̒u�����������ɑ΂���A�u��������������w�肷��_�C�A���O
 * @author y-terada
 *
 */
public class ParametersDialog extends JDialog implements AgreementListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String DIALOG_TITLE = "�p�����[�^�w��";
	/**
	 * �O����͂������e��ێ����܂��B<br/>
	 * �C���X�^���X�𒴂��ċ��L���܂��B
	 */
	private static String[] previous = null;

	private String[][] parameters;
	private List<String> retList;

	/**
	 * �w�肳�ꂽ���̍��ڂ���͂��邽�߂̃_�C�A���O�C���X�^���X�𐶐����܂��B<br/>
	 * �O����͂������e������΁A�f�t�H���g�\�����܂��B
	 * @param parent �_�C�A���O�̐e�E�B���h�E
	 * @param count ���ڐ�
	 */
	public ParametersDialog( java.awt.Frame parent, final int count ){
		super(parent, DIALOG_TITLE, true);
		parameters = new String[count][1];
		if (previous != null){
			// �O��ݒ肪����΁A���P����
			for (int i = 0; i < previous.length && i < parameters.length; i ++){
				parameters[i][0] = previous[i];
			}
		}
		JTable table = new JTable(parameters, new String[]{"����"});
		JScrollPane scrollPane = new JScrollPane(table);

		Container con = this.getContentPane();
		con.setLayout( new BorderLayout() );
		con.add(scrollPane, BorderLayout.CENTER);
		con.add(OkCancelComponent.getOkCancelComponent(this), BorderLayout.SOUTH);
		this.pack();
	}

	@Override
	public void cancelAction() {
		retList = null;
		close();
	}

	@Override
	public void okAction() {
		retList = new ArrayList<String>();
		previous = new String[parameters.length];
		for (int i = 0; i < parameters.length; i ++){
			previous[i] = parameters[i][0]; 
			retList.add(parameters[i][0]);
		}
		close();
	}

	/**
	 * �_�C�A���O��j�����܂��B
	 */
	private void close(){
		dispose();
	}

	/**
	 * ���͒l�̓��e��Ԃ��܂��B<br/>
	 * �L���ȓ��͂��Ȃ������ꍇ�Anull��Ԃ��܂��B
	 * @return ���͒l
	 */
	public List<String> getParameters(){
		return retList;
	}
}
