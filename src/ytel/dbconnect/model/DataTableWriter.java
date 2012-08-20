package ytel.dbconnect.model;

public interface DataTableWriter {
	public void write(Object[] titles, Object[][] values) throws ActionInterruptException;
}
