package jp.gr.java_conf.t_era.dbconnect.model.database.queryresultview;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.t_era.dbconnect.model.database.queryresultview.converter.CLobConverter;

public class QueryResultViewFactory {
	private final List<QueryResultConverter> converterList;

	QueryResultViewFactory() {
		converterList = new ArrayList<QueryResultConverter>();
		converterList.add(new CLobConverter());
	}

	/**
	 * 指定されたSQL検索結果から、このクラスのインスタンスを生成します。
	 * @param rslt
	 * @return 指定されたSQL検索結果を保持するデータ
	 * @throws SQLException
	 */
	public QueryResultViewImpl parseViewInfo(ResultSet rslt, long executeTime) throws SQLException{
		ResultSetMetaData rsmd = rslt.getMetaData();
		List<String> tempTitleList = new ArrayList<String>();
		for( int i = 1; i <= rsmd.getColumnCount(); i ++ ){
			tempTitleList.add( rsmd.getColumnLabel(i) );
		}

		List<Map<Integer, Object>> tempValueList = new ArrayList<Map<Integer, Object>>();
		while( rslt.next() ){
			Map<Integer, Object> aRecord = new HashMap<Integer, Object>();
			for( int i = 0; i < rsmd.getColumnCount(); i ++ ){
				aRecord.put(i, convert(rslt.getObject(i+1)));
			}
			tempValueList.add( aRecord );
		}
		return new QueryResultViewImpl(tempTitleList, tempValueList, executeTime);
	}

	private String convert(Object arg) throws SQLException {
		for (QueryResultConverter converter : converterList) {
			if (converter.canConvert(arg)) {
				return converter.convert(arg);
			}
		}
		return arg == null ? "" : arg.toString();
	}
}
