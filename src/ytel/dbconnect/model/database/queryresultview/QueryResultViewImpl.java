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
// * �����s������̃f�[�^<br/>
// * �e��Ƀ^�C�g����ێ����܂��B
// * @author y-terada
// *
// */
//public class QueryResultViewImpl implements QueryResultView{
//	/**
//	 * �e��̃^�C�g��
//	 */
//	private final List<String> titleList;
//	/**
//	 * �f�[�^
//	 * List&lt�e�s�̗v�f&lt�^�C�g��, ���g&gt&gt
//	 */
//	private final List<Map<String, Object>> valueList;
//
//	/**
//	 * �N�G�����s����
//	 */
//	private final long executeTime;
//
//	/**
//	 * �^�C�g�����w�肷��R���X�g���N�^
//	 * @param titleList �^�C�g���̈ꗗ
//	 * @param valueList �\�����e�̈ꗗ
//	 * @param executeTime �N�G�����s����
//	 */
//	private QueryResultViewImpl(List<String> titleList, List<Map<String, Object>> valueList, long executeTime){
//		this.titleList = Collections.unmodifiableList( titleList );
//		this.valueList = valueList;
//		this.executeTime = executeTime;
//	}
//
//	/**
//	 * �w�肳�ꂽSQL�������ʂ���A���̃N���X�̃C���X�^���X�𐶐����܂��B
//	 * @param rslt
//	 * @return �w�肳�ꂽSQL�������ʂ�ێ�����f�[�^
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
//	 * �z��Ƃ��āA�^�C�g���̈ꗗ��Ԃ��܂�
//	 * @return �^�C�g���̈ꗗ�̔z��
//	 */
//	@Override
//	public String[] getTitleArray(){
//		return Arrays.copyOf(titleList.toArray(), titleList.toArray().length, String[].class);
//	}
//	/**
//	 * �z��Ƃ��ăf�[�^�̈ꗗ��Ԃ��܂��B<br/>
//	 * [[��s�̊e���R�[�h�̔z��]�̔z��]�ł��B
//	 * @return �f�[�^�̈ꗗ
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
 * �����s������̃f�[�^<br/>
 * �e��Ƀ^�C�g����ێ����܂��B
 * @author y-terada
 *
 */
public class QueryResultViewImpl implements QueryResultView{
	/**
	 * �e��̃^�C�g��
	 */
	private final List<String> titleList;
	/**
	 * �f�[�^
	 * List&lt�e�s�̗v�f&lt�^�C�g��, ���g&gt&gt
	 */
	private final List<Map<Integer, Object>> valueList;

	/**
	 * �N�G�����s����
	 */
	private final long executeTime;

	/**
	 * �^�C�g�����w�肷��R���X�g���N�^
	 * @param titleList �^�C�g���̈ꗗ
	 * @param valueList �\�����e�̈ꗗ
	 * @param executeTime �N�G�����s����
	 */
	QueryResultViewImpl(List<String> titleList, List<Map<Integer, Object>> valueList, long executeTime){
		this.titleList = Collections.unmodifiableList( titleList );
		this.valueList = valueList;
		this.executeTime = executeTime;
	}

	// TODO �t�@�N�g���̏��������@�����B(�eConverter�́A�������[�h�H�H�ݒ�t�@�C���Ŏw��H�H)
	private static final QueryResultViewFactory factory = new QueryResultViewFactory();
	/**
	 * �w�肳�ꂽSQL�������ʂ���A���̃N���X�̃C���X�^���X�𐶐����܂��B
	 * @param rslt
	 * @return �w�肳�ꂽSQL�������ʂ�ێ�����f�[�^
	 * @throws SQLException
	 */
	public static QueryResultViewImpl parseViewInfo(ResultSet rslt, long executeTime) throws SQLException{
		return factory.parseViewInfo(rslt, executeTime);
	}

	/**
	 * �z��Ƃ��āA�^�C�g���̈ꗗ��Ԃ��܂�
	 * @return �^�C�g���̈ꗗ�̔z��
	 */
	@Override
	public String[] getTitleArray(){
		return Arrays.copyOf(titleList.toArray(), titleList.toArray().length, String[].class);
	}
	/**
	 * �z��Ƃ��ăf�[�^�̈ꗗ��Ԃ��܂��B<br/>
	 * [[��s�̊e���R�[�h�̔z��]�̔z��]�ł��B
	 * @return �f�[�^�̈ꗗ
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
