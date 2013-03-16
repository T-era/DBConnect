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
 * SQLの置き換え文字に対する、置き換え文字列を指定するダイアログ
 * @author y-terada
 *
 */
public class ParametersDialog extends JDialog implements AgreementListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String DIALOG_TITLE = "パラメータ指定";
	/**
	 * 前回入力した内容を保持します。<br/>
	 * インスタンスを超えて共有します。
	 */
	private static String[] previous = null;

	private String[][] parameters;
	private List<String> retList;

	/**
	 * 指定された個数の項目を入力するためのダイアログインスタンスを生成します。<br/>
	 * 前回入力した内容があれば、デフォルト表示します。
	 * @param parent ダイアログの親ウィンドウ
	 * @param count 項目数
	 */
	public ParametersDialog( java.awt.Frame parent, final int count ){
		super(parent, DIALOG_TITLE, true);
		parameters = new String[count][1];
		if (previous != null){
			// 前回設定があれば、踏襲する
			for (int i = 0; i < previous.length && i < parameters.length; i ++){
				parameters[i][0] = previous[i];
			}
		}
		JTable table = new JTable(parameters, new String[]{"引数"});
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
	 * ダイアログを破棄します。
	 */
	private void close(){
		dispose();
	}

	/**
	 * 入力値の内容を返します。<br/>
	 * 有効な入力がなかった場合、nullを返します。
	 * @return 入力値
	 */
	public List<String> getParameters(){
		return retList;
	}
}
