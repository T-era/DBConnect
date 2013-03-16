package jp.gr.java_conf.t_era.dbconnect.model;

public interface DataTableWriter {
	public void write(Object[] titles, Object[][] values) throws ActionInterruptException;
}
