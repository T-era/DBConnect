//package ytel.dbconnect.model.database.queryresultview;
//
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import ytel.dbconnect.model.QueryResultView;
//
///**
// * 複数行複数列のデータ<br/>
// * 各列にタイトルを保持します。
// * @author y-terada
// *
// */
//public class QueryResultViewImpl implements QueryResultView{
//	/**
//	 * 各列のタイトル
//	 */
//	private final List<String> titleList;
//	/**
//	 * データ
//	 * List&lt各行の要素&ltタイトル, 中身&gt&gt
//	 */
//	private final List<Map<String, Object>> valueList;
//
//	/**
//	 * クエリ実行時間
//	 */
//	private final long executeTime;
//
//	/**
//	 * タイトルを指定するコンストラクタ
//	 * @param titleList タイトルの一覧
//	 * @param valueList 表示内容の一覧
//	 * @param executeTime クエリ実行時間
//	 */
//	private QueryResultViewImpl(List<String> titleList, List<Map<String, Object>> valueList, long executeTime){
//		this.titleList = Collections.unmodifiableList( titleList );
//		this.valueList = valueList;
//		this.executeTime = executeTime;
//	}
//
//	/**
//	 * 指定されたSQL検索結果から、このクラスのインスタンスを生成します。
//	 * @param rslt
//	 * @return 指定されたSQL検索結果を保持するデータ
//	 * @throws SQLException
//	 */
//	public static QueryResultViewImpl parseViewInfo(ResultSet rslt, long executeTime) throws SQLException{
//		ResultSetMetaData rsmd = rslt.getMetaData();
//		List<String> tempTitleList = new ArrayList<String>();
//		for( int i = 1; i <= rsmd.getColumnCount(); i ++ ){
//			tempTitleList.add( rsmd.getColumnLabel(i) );
//		}
//
//		List<Map<String, Object>> tempValueList = new ArrayList<Map<String, Object>>();
//		while( rslt.next() ){
//			Map<String, Object> aRecord = new HashMap<String, Object>();
//			for( String key : tempTitleList ){
//				aRecord.put( key, rslt.getObject( key ) );
//			}
//			tempValueList.add( aRecord );
//		}
//		return new QueryResultViewImpl(tempTitleList, tempValueList, executeTime);
//	}
//
//	/**
//	 * 配列として、タイトルの一覧を返します
//	 * @return タイトルの一覧の配列
//	 */
//	@Override
//	public String[] getTitleArray(){
//		return Arrays.copyOf(titleList.toArray(), titleList.toArray().length, String[].class);
//	}
//	/**
//	 * 配列としてデータの一覧を返します。<br/>
//	 * [[一行の各レコードの配列]の配列]です。
//	 * @return データの一覧
//	 */
//	@Override
//	public String[][] getValueArray(){
//		String[][] ret = new String[valueList.size()][];
//
//		int i = 0;
//		for( Iterator<Map<String, Object>> iteOut = valueList.iterator(); iteOut.hasNext(); i ++ ){
//			ret[i] = new String[titleList.size()];
//			Map<String, Object> aRecord = iteOut.next();
//
//			int j = 0;
//			for( Iterator<String> ite = titleList.iterator(); ite.hasNext(); j ++ ){
//				Object val = aRecord.get(ite.next());
//				if (val == null){
//					ret[i][j] = "";
//				} else {
//					ret[i][j] = val.toString();
//				}
//			}
//		}
//		return ret;
//	}
//
//	@Override
//	public long getExecuteTime() {
//		return executeTime;
//	}
//
//	@Override
//	public int getNumOfResult() {
//		return valueList.size();
//	}
//}
package ytel.dbconnect.model.database.queryresultview;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ytel.dbconnect.model.QueryResultView;

/**
 * 複数行複数列のデータ<br/>
 * 各列にタイトルを保持します。
 * @author y-terada
 *
 */
public class QueryResultViewImpl implements QueryResultView{
	/**
	 * 各列のタイトル
	 */
	private final List<String> titleList;
	/**
	 * データ
	 * List&lt各行の要素&ltタイトル, 中身&gt&gt
	 */
	private final List<Map<Integer, Object>> valueList;

	/**
	 * クエリ実行時間
	 */
	private final long executeTime;

	/**
	 * タイトルを指定するコンストラクタ
	 * @param titleList タイトルの一覧
	 * @param valueList 表示内容の一覧
	 * @param executeTime クエリ実行時間
	 */
	QueryResultViewImpl(List<String> titleList, List<Map<Integer, Object>> valueList, long executeTime){
		this.titleList = Collections.unmodifiableList( titleList );
		this.valueList = valueList;
		this.executeTime = executeTime;
	}

	// TODO ファクトリの初期化方法検討。(各Converterは、自動ロード？？設定ファイルで指定？？)
	private static final QueryResultViewFactory factory = new QueryResultViewFactory();
	/**
	 * 指定されたSQL検索結果から、このクラスのインスタンスを生成します。
	 * @param rslt
	 * @return 指定されたSQL検索結果を保持するデータ
	 * @throws SQLException
	 */
	public static QueryResultViewImpl parseViewInfo(ResultSet rslt, long executeTime) throws SQLException{
		return factory.parseViewInfo(rslt, executeTime);
	}

	/**
	 * 配列として、タイトルの一覧を返します
	 * @return タイトルの一覧の配列
	 */
	@Override
	public String[] getTitleArray(){
		return Arrays.copyOf(titleList.toArray(), titleList.toArray().length, String[].class);
	}
	/**
	 * 配列としてデータの一覧を返します。<br/>
	 * [[一行の各レコードの配列]の配列]です。
	 * @return データの一覧
	 */
	@Override
	public String[][] getValueArray(){
		String[][] ret = new String[valueList.size()][];

		int i = 0;
		for( Iterator<Map<Integer, Object>> iteOut = valueList.iterator(); iteOut.hasNext(); i ++ ){
			ret[i] = new String[titleList.size()];
			Map<Integer, Object> aRecord = iteOut.next();

			int j = 0;
			for( j = 0; j < titleList.size(); j ++ ){
				Object val = aRecord.get(j);
				if (val == null){
					ret[i][j] = "";
				} else {
					ret[i][j] = val.toString();
				}
			}
		}
		return ret;
	}

	@Override
	public long getExecuteTime() {
		return executeTime;
	}

	@Override
	public int getNumOfResult() {
		return valueList.size();
	}
}
