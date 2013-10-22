package b;

import java.io.FileNotFoundException;
import java.io.IOException;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.ActionInterruptException;
import jp.gr.java_conf.t_era.dbconnect.view.MainFrame;

/**
 * í èÌãNìÆ
 * @author y-terada
 *
 */
public class DBConnect {
	public static void main( String[] args ) throws ActionInterruptException, FileNotFoundException, IOException, InterruptedException{
		Configure config = new Configure("./", ".properties");

		MainFrame obj = new MainFrame(config);
		System.out.println(obj);
	}
}
