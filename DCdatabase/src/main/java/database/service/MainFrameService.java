package database.service;

import java.util.Map;
import java.util.TreeSet;

import org.apache.spark.sql.Dataset;
import org.jlab.groot.data.H2F;

import database.objects.StatusChangeDB;
import database.utils.Coordinate;

public interface MainFrameService {

	public void setRunNumber(int runNumber);

	public int getRunNumber();

	public void setSelectedSector(int sectorNumber);

	public void setSelectedSuperlayer(int superLayerNumber);

	public int getSelectedSector();

	public int getSelectedSuperlayer();

	public Dataset<StatusChangeDB> getBySectorAndSuperLayer(int sector, int superLayer);

	public Dataset<StatusChangeDB> getComparedBySectorAndSuperLayer(int sector, int superLayer);

	public Map<Coordinate, H2F> getHistogramMap();

	public H2F getHistogramByMap(int superLayer, int sector);

	public Dataset<StatusChangeDB> getDatasetByMap(int superLayer, int sector);

	public Dataset<StatusChangeDB> getComparedDatasetByMap(int superLayer, int sector);

	public Map<Coordinate, Dataset<StatusChangeDB>> getDataSetMap();

	public Map<Coordinate, Dataset<StatusChangeDB>> getComparedDataSetMap();

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

	public boolean sentToDB();

	public void setSentTodb(boolean sentTodb);

	public void prepareAddBackList(TreeSet<StatusChangeDB> queryList);

	public void removeRowFromMYSQLQuery(TreeSet<StatusChangeDB> statusChangeDBs);

	public TreeSet<StatusChangeDB> getAddBackList();

	public void clearAddBackList();

	public void shutdown();

	public int getFault();

	public void setFault(int fault);

	public int getBundle();

	public void setBundle(int bundle);

	public String getUserName();

	public void setUserName(String userName);

}
