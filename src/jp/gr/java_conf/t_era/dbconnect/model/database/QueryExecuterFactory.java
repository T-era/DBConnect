package jp.gr.java_conf.t_era.dbconnect.model.database;

import java.sql.SQLException;
import java.util.List;

import jp.gr.java_conf.t_era.dbconnect.model.ConnectionFactory;

/**
 * �K�؂ȃN�G�����s�I�u�W�F�N�g�𐶐����邽�߂̃N���X�B
 * 
 * @author y-terada
 *
 */
public class QueryExecuterFactory {
	/**
	 * �K�؂ȃN�G�����s�I�u�W�F�N�g�𐶐����ĕԂ��܂��B
	 * @param connectionFactory �ڑ����
	 * @param parameters �����̈ꗗ
	 * @return �N�G�����s�I�u�W�F�N�g
	 */
	public static QueryExecuter getExecuter(ConnectionFactory connectionFactory, List<String> parameters){
		if (parameters == null) {
			return new QueryExecNoArgs(connectionFactory);
		} else {
			QueryExecWithArgs eq = new QueryExecWithArgs(connectionFactory);
			eq.setParameter(parameters);
			return eq;
		}
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
	public static int getParameterCount(ConnectionFactory connectionFactory, String sql) throws SQLException{
		return QueryExecWithArgs.getParameterCount(connectionFactory, sql);
	}
}
