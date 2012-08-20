package ytel.dbconnect.view.dialog.parts;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;

/**
 * TABLE検索のためのUIコンポーネント
 *
 * @author 22677478
 *
 */
public class SearchArea {
	private final SearchActionListener listener;

	public SearchArea(SearchActionListener listener) {
		this.listener = listener;
	}
	public Component getComponent() {
		JPanel component = new JPanel(new BorderLayout());

		final JCheckBox asRegex = new JCheckBox("As RegEx");

		final Document doc = new DefaultStyledDocument();
		doc.addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				act(e.getDocument(), asRegex.isSelected());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				act(e.getDocument(), asRegex.isSelected());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				act(e.getDocument(), asRegex.isSelected());
			}
		});
		asRegex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				act(doc, asRegex.isSelected());
			}
		});

		component.add(new JLabel("Condition: "), BorderLayout.WEST);
		component.add(new JTextField(doc, null, 0), BorderLayout.CENTER);
		component.add(asRegex, BorderLayout.EAST);

		return component;
	}
	private void act(Document d, boolean asRegex) {
		try {
			listener.searchPushed(d.getText(0, d.getLength()), asRegex);
		} catch (Exception e) {
			throw new RuntimeException("?", e);
		}
	}
}
