package ytel.dbconnect.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import ytel.dbconnect.model.ActionInterruptException;
import ytel.dbconnect.model.ConnectionFactory;
import ytel.dbconnect.model.MessageObserver;
import ytel.dbconnect.model.QueryResultView;
import ytel.dbconnect.model.database.queryresultview.NullViewInfo;
import ytel.dbconnect.model.database.queryresultview.QueryResultViewImpl;

/**
 * �u������������A���ŁASQL�����s���܂��B
 * @author y-terada
 *
 */
class QueryExecWithArgs implements QueryExecuter{
	private List<String>parameters;
	private final ConnectionFactory connectionFactory;

	QueryExecWithArgs(ConnectionFactory connectionFactory){
		this.connectionFactory = connectionFactory;
	}

	/**
	 * �w�肳�ꂽSQL�̃p�����[�^�������������̐����擾���܂��B
	 * 
	 * # Oracle��Connection#getParameterMetaData���T�|�[�g���Ȃ������ŁA�\����͂��K�v�B
	 * # ����ł̓R�����g�╶���񃊃e������?���܂�SQL�͂��܂����삵�Ȃ��B
	 * @param connectionFactory
	 * @param sql
	 * @return SQL���s���ɕK�v�ƂȂ�p�����[�^�̐�
	 * @throws SQLException
	 */
	static int getParameterCount(ConnectionFactory connectionFactory, String sql) throws SQLException{
		return sql.replaceAll("[^?]", "").length();
		/*
		TODO Connection#getParameterMetaData���T�|�[�g����JDBC�h���C�o�ł́A�ȉ��̕����X�}�[�g���B
		
		Connection con = connectionFactory.getConnection();
		PreparedStatement stmt = con.prepareStatement(sql);
		try{
			return stmt.getParameterMetaData().getParameterCount();
		}finally{
			stmt.close();
		}*/
	}

	void setParameter( List<String> parameters ){
		this.parameters = Collections.unmodifiableList(parameters);
	}

	@Override
	public QueryResultView exec(String sql, MessageObserver messageObserver) throws ActionInterruptException{
		try{
			Connection con = connectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			for (int index = 0; parameters != null && index < parameters.size(); index ++){
				stmt.setString(index + 1, parameters.get(index));
			}
			try{
				return execSQL(stmt, sql, messageObserver);
			}finally{
				stmt.close();
			}
		} catch (SQLException e) {
			messageObserver.setErrorMessage("SQL���s���s", e);
			throw new ActionInterruptException("SQL���s���s", e);
		}
	}

	/**
	 * Statement�ɑ΂���SQL�̎��s�����A���ʂ�{@link QueryResultView}�Ƃ��ĕԂ��܂��B
	 * @param stmt �N�G�������s����DB�X�e�[�g�����g
	 * @param sql ���s����SQL
	 * @param messageObserver ���s�����b�Z�[�W������I�u�U�[�o
	 * @return ��������
	 * @throws SQLException
	 */
	private QueryResultView execSQL( PreparedStatement stmt, String sql, MessageObserver messageObserver ) throws SQLException{
		ResultSet rslt = null;

		if( stmt.execute() ){
			System.gc();
			long start = System.currentTimeMillis();
			rslt = stmt.getResultSet();
			long sqlExectTime = System.currentTimeMillis() - start;

			try{
				return QueryResultViewImpl.parseViewInfo(rslt, sqlExectTime);
			}finally{
				rslt.close();
				if (messageObserver != null) {
					messageObserver.setMessage("�������s");
				}
			}
		}else{
			if (messageObserver != null) {
				messageObserver.setMessage("�X�V���s");
			}
			System.gc();
			long start = System.currentTimeMillis();
			int updateCount = stmt.getUpdateCount();
			long sqlExectTime = System.currentTimeMillis() - start;

			return new NullViewInfo("�X�V���s", updateCount, sqlExectTime);
		}
	}
}
