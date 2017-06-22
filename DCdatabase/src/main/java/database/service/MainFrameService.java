package database.service;

import java.util.Map;
import java.util.TreeSet;

import org.apache.spark.sql.Dataset;
import org.jlab.groot.data.H2F;

import database.objects.StatusChangeDB;
import database.utils.Coordinate;

public interface MainFrameService {

	public Dataset<StatusChangeDB> getBySectorAndSuperLayer(int sector, int superLayer);

	public Map<Coordinate, H2F> getHistogramMap();

	public H2F getHistogramByMap(int superLayer, int sector);

	public Dataset<StatusChangeDB> getDatasetByMap(int superLayer, int sector);

	public Map<Coordinate, Dataset<StatusChangeDB>> getDataSetMap();

	public void prepareMYSQLQuery(TreeSet<StatusChangeDB> queryList);

	public TreeSet<StatusChangeDB> getMYSQLQuery();

	public void addToCompleteSQLList(TreeSet<StatusChangeDB> tempList);

	public void clearTempSQLList();

	public TreeSet<StatusChangeDB> getCompleteSQLList();

	public void setListIndices(TreeSet<Integer> intList);

	public TreeSet<Integer> getListIndices();

	public void setSQLList();

	public int getnEventsInFile();

	public void setnEventsInFile(int nEventsInFile);

	public void shutdown();

}
