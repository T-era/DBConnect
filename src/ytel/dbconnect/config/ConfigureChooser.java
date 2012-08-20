package ytel.dbconnect.config;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import ytel.view.BorderLayoutBuilder;

/**
 * ユーザ設定のコンフィグファイルを選択するクラス。
 * @author y-terada
 *
 */
public class ConfigureChooser {
	private volatile boolean done = false;
	private NamedConfigure result;

	/**
	 * 引数で渡された選択肢から、コンフィグファイルを選択します。
	 * 選択は、ダイアログUIで行います。
	 * @param configureList 選択肢のリスト
	 */
	ConfigureChooser(final List<NamedConfigure> configureList) {
		final JFrame chooseFrame = new JFrame();
		chooseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container con = chooseFrame.getContentPane();

		final JComboBox chooser = new JComboBox(configureList.toArray());
		JButton button = new JButton("OK");

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chooser.getSelectedIndex() == -1);
				else {
					result = configureList.get(chooser.getSelectedIndex());
					chooseFrame.dispose();
					done = true;
				}
			}
		});

		BorderLayoutBuilder blb = new BorderLayoutBuilder();
		blb.add(chooser, BorderLayoutBuilder.Direction.Center);
		blb.add(button, BorderLayoutBuilder.Direction.South);
		blb.layoutComponent(con);

		chooseFrame.pack();
		chooseFrame.setVisible(true);
	}

	/**
	 * 選択処理(ダイアログ表示)が完了した場合、TRUEになります。
	 * @return 選択処理(ダイアログ表示)が完了した場合TRUE
	 */
	boolean isDone() {
		return done;
	}

	/**
	 * UIで選択した結果を返します。
	 * @return 結果
	 */
	NamedConfigure getSelectedItem() {
		return result;
	}
}
