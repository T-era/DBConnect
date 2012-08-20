package bin;

import java.io.FileNotFoundException;
import java.io.IOException;

import ytel.dbconnect.config.Configure;
import ytel.dbconnect.model.ActionInterruptException;
import ytel.dbconnect.view.MainFrame;

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
