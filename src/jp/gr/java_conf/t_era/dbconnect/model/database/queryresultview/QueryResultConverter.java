package jp.gr.java_conf.t_era.dbconnect.model.database.queryresultview;

import java.sql.SQLException;

public interface QueryResultConverter {
	String convert(Object arg) throws SQLException;
	boolean canConvert(Object arg);
}
