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
 * ���[�U�ݒ�̃R���t�B�O�t�@�C����I������N���X�B
 * @author y-terada
 *
 */
public class ConfigureChooser {
	private volatile boolean done = false;
	private NamedConfigure result;

	/**
	 * �����œn���ꂽ�I��������A�R���t�B�O�t�@�C����I�����܂��B
	 * �I���́A�_�C�A���OUI�ōs���܂��B
	 * @param configureList �I�����̃��X�g
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
	 * �I������(�_�C�A���O�\��)�����������ꍇ�ATRUE�ɂȂ�܂��B
	 * @return �I������(�_�C�A���O�\��)�����������ꍇTRUE
	 */
	boolean isDone() {
		return done;
	}

	/**
	 * UI�őI���������ʂ�Ԃ��܂��B
	 * @return ����
	 */
	NamedConfigure getSelectedItem() {
		return result;
	}
}
