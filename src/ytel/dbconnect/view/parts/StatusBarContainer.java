package ytel.dbconnect.view.parts;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

/**
 * DBConnect�̃X�e�[�^�X�o�[�ł��B<p/>
 * �ȉ��̊e���e��\�����闓�������܂��B
 * <ol>
 *  <li/>�ڑ���\����
 *  <li/>���b�Z�[�W��\������ȈՃ��b�Z�[�W��
 *  <li/>SQL���s���Ԃ�\������\����
 *  <li/>�������ʌ����̕\����
 * </ol>
 * @author y-terada
 *
 */
public class StatusBarContainer extends Container{
	private static final long serialVersionUID = 1L;

	/**
	 * �ȈՃ��b�Z�[�W��
	 */
	private final JLabel messageLabel = new JLabel();
	/**
	 * SQL���s���ԕ\����
	 */
	private final JLabel timeLabel = new JLabel( "0", JLabel.RIGHT );
	/**
	 * �������ʌ�����
	 */
	private final JLabel counterLabel = new JLabel( "0", JLabel.RIGHT );

	// �\�����e�̃f�t�H���g�ݒ�
	private static final JLabel urlLabel = new JLabel("Connecting ...");
	private static final JLabel msec = new JLabel("sec");
	private static final JLabel std = new JLabel("��");
	private static final Format timeFormat = new DecimalFormat("###,###.000");
	private static final Format counterFormat = new DecimalFormat("###,###");
	private static final Format connectionFormat = new MessageFormat("{0} ({1})");

	private static final Dimension DEFAULT_LABEL_SIZE = new Dimension( 40, 20 );

	public StatusBarContainer(){
		super();
		this.setLayout( new BorderLayout() );

		Container timer = new Container();
		timer.setLayout( new GridLayout( 1, 5 ) );
		counterLabel.setPreferredSize( DEFAULT_LABEL_SIZE );
		timer.add( counterLabel );
		timer.add( std );
		timer.add( new JLabel() );
		timeLabel.setPreferredSize( DEFAULT_LABEL_SIZE );
		timer.add( timeLabel );
		timer.add( msec );
		this.add( timer, BorderLayout.EAST );

		messageLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.add( messageLabel, BorderLayout.CENTER );

		this.add( urlLabel, BorderLayout.WEST );
	}

	/**
	 * SQL���s���ԕ\�����X�V���܂��B
	 * @param time SQL���s����(�P��:msec)
	 */
	public void setTime( long time ){
		double timeSec = (double)time / 1000.0;
		timeLabel.setText( timeFormat.format( timeSec ) );
	}

	/**
	 * �������ʌ����\�����X�V���܂��B
	 * @param count �������ʌ���
	 */
	public void setCount( int count ){
		counterLabel.setText( counterFormat.format( count ) );
	}

	/**
	 * �ڑ�����̕\�����X�V���܂��B
	 * @param newUrl �ڑ�����
	 */
	public void setUrl(String newUrl, String userName){
		urlLabel.setToolTipText(connectionFormat.format(new Object[] {userName, newUrl}));
	}

	/**
	 * �ȈՃ��b�Z�[�W�\�����X�V���܂��B
	 * @param message �ȈՃ��b�Z�[�W���e
	 */
	public void setMessage( String message ){
		messageLabel.setText(message);
		messageLabel.setToolTipText(message);
	}
}
