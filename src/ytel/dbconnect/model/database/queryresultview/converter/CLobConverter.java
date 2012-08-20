package ytel.dbconnect.model.database.queryresultview.converter;

import java.sql.Clob;
import java.sql.SQLException;

import ytel.dbconnect.model.database.queryresultview.QueryResultConverter;

public class CLobConverter implements QueryResultConverter {
	private static final int MAX_LENGTH_TO_SHOW = 1000;
	@Override
	public boolean canConvert(Object arg) {
		return arg instanceof java.sql.Clob;
	}

	@Override
	public String convert(Object arg) throws SQLException {
		Clob clob = (java.sql.Clob)arg;
		long length = clob.length();
		String ret;
		if (length > MAX_LENGTH_TO_SHOW) {
			ret = clob.getSubString(1, MAX_LENGTH_TO_SHOW) + "...";
		} else {
			ret = clob.getSubString(1, (int)length);
		}
		return ret;
	}
}
