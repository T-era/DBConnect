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
 * DBConnectのステータスバーです。<p/>
 * 以下の各内容を表示する欄を持ちます。
 * <ol>
 *  <li/>接続先表示欄
 *  <li/>メッセージを表示する簡易メッセージ欄
 *  <li/>SQL実行時間を表示する表示欄
 *  <li/>検索結果件数の表示欄
 * </ol>
 * @author y-terada
 *
 */
public class StatusBarContainer extends Container{
	private static final long serialVersionUID = 1L;

	/**
	 * 簡易メッセージ欄
	 */
	private final JLabel messageLabel = new JLabel();
	/**
	 * SQL実行時間表示欄
	 */
	private final JLabel timeLabel = new JLabel( "0", JLabel.RIGHT );
	/**
	 * 検索結果件数欄
	 */
	private final JLabel counterLabel = new JLabel( "0", JLabel.RIGHT );

	// 表示内容のデフォルト設定
	private static final JLabel urlLabel = new JLabel("Connecting ...");
	private static final JLabel msec = new JLabel("sec");
	private static final JLabel std = new JLabel("件");
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
	 * SQL実行時間表示を更新します。
	 * @param time SQL実行時間(単位:msec)
	 */
	public void setTime( long time ){
		double timeSec = (double)time / 1000.0;
		timeLabel.setText( timeFormat.format( timeSec ) );
	}

	/**
	 * 検索結果件数表示を更新します。
	 * @param count 検索結果件数
	 */
	public void setCount( int count ){
		counterLabel.setText( counterFormat.format( count ) );
	}

	/**
	 * 接続先情報の表示を更新します。
	 * @param newUrl 接続先情報
	 */
	public void setUrl(String newUrl, String userName){
		urlLabel.setToolTipText(connectionFormat.format(new Object[] {userName, newUrl}));
	}

	/**
	 * 簡易メッセージ表示を更新します。
	 * @param message 簡易メッセージ内容
	 */
	public void setMessage( String message ){
		messageLabel.setText(message);
		messageLabel.setToolTipText(message);
	}
}
