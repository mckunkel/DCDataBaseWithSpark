package database.service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.apache.spark.sql.Dataset;
import org.jlab.groot.data.H2F;

import database.objects.StatusChangeDB;
import database.utils.Coordinate;
import spark.utils.MainFrameQuery;

public class MainFrameServiceImpl implements MainFrameService {

	private MainFrameQuery mainFrameQuery;
	private Map<Coordinate, H2F> occupanciesByCoordinate = null;
	private Map<Coordinate, Dataset<StatusChangeDB>> dataSetByCoordinate = null;
	private Map<Coordinate, Dataset<StatusChangeDB>> dataComparedSetByCoordinate = null;

	private TreeSet<StatusChangeDB> queryList = null;
	private TreeSet<StatusChangeDB> completeQueryList = null;
	private TreeSet<Integer> intList = null;
	private int nEventsInFile;
	private int runNumber;
	private int sectorNumber;
	private int superLayerNumber;
	private boolean sentTodb;

	public MainFrameServiceImpl() {
		this.mainFrameQuery = new MainFrameQuery();
		this.occupanciesByCoordinate = new HashMap<Coordinate, H2F>();
		this.dataSetByCoordinate = new HashMap<Coordinate, Dataset<StatusChangeDB>>();
		this.dataComparedSetByCoordinate = new HashMap<Coordinate, Dataset<StatusChangeDB>>();

		this.queryList = new TreeSet<>();
		this.completeQueryList = new TreeSet<>();
		this.intList = new TreeSet<>();
		createHistograms();
		createDatasets();
	}

	public void setRunNumber(int runNumber) {
		this.runNumber = runNumber;
	}

	public int getRunNumber() {
		return this.runNumber;
	}

	public void setSelectedSector(int sectorNumber) {
		this.sectorNumber = sectorNumber;
	}

	public void setSelectedSuperlayer(int superLayerNumber) {
		this.superLayerNumber = superLayerNumber;
	}

	public int getSelectedSector() {
		return this.sectorNumber;
	}

	public int getSelectedSuperlayer() {
		return this.superLayerNumber;
	}

	public Dataset<StatusChangeDB> getBySectorAndSuperLayer(int sector, int superLayer) {
		this.mainFrameQuery.setDataset(getDatasetByMap(superLayer, sector));
		return this.mainFrameQuery.getBySectorAndSuperLayer(sector, superLayer);
	}

	public Dataset<StatusChangeDB> getComparedBySectorAndSuperLayer(int sector, int superLayer) {
		this.mainFrameQuery.setDataset(getComparedDatasetByMap(superLayer, sector));
		return this.mainFrameQuery.getBySectorAndSuperLayer(sector, superLayer);
	}

	public Map<Coordinate, H2F> getHistogramMap() {
		return this.occupanciesByCoordinate;
	}

	private void createHistograms() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				occupanciesByCoordinate.put(new Coordinate(i, j),
						new H2F("Occupancy all hits SL" + i + "sector" + j, "", 112, 1, 113, 6, 1, 7));
				occupanciesByCoordinate.get(new Coordinate(i, j)).setTitleX("Wire");
				occupanciesByCoordinate.get(new Coordinate(i, j)).setTitleY("Layer");
				occupanciesByCoordinate.get(new Coordinate(i, j))
						.setTitle("Sector " + (j + 1) + " Superlayer" + (i + 1));
			}
		}
	}

	private void createDatasets() {
		for (int i = 0; i < 6; i++) {// superLayer
			for (int j = 0; j < 6; j++) {// sector
				Dataset<StatusChangeDB> test = null;
				dataSetByCoordinate.put(new Coordinate(i, j), test);
				dataComparedSetByCoordinate.put(new Coordinate(i, j), test);
			}
		}
	}

	public H2F getHistogramByMap(int superLayer, int sector) {
		return this.occupanciesByCoordinate.get(new Coordinate(superLayer - 1, sector - 1));
	}

	public Dataset<StatusChangeDB> getDatasetByMap(int superLayer, int sector) {
		return this.dataSetByCoordinate.get(new Coordinate(superLayer - 1, sector - 1));
	}

	public Dataset<StatusChangeDB> getComparedDatasetByMap(int superLayer, int sector) {
		return this.dataComparedSetByCoordinate.get(new Coordinate(superLayer - 1, sector - 1));
	}

	public Map<Coordinate, Dataset<StatusChangeDB>> getDataSetMap() {
		return this.dataSetByCoordinate;
	}

	public Map<Coordinate, Dataset<StatusChangeDB>> getComparedDataSetMap() {
		return this.dataComparedSetByCoordinate;
	}

	// for inserting into MYSQL
	public void prepareMYSQLQuery(TreeSet<StatusChangeDB> queryList) {
		this.queryList = queryList;
	}

	public TreeSet<StatusChangeDB> getMYSQLQuery() {
		return this.queryList;
	}

	public void addToCompleteSQLList(TreeSet<StatusChangeDB> tempList) {
		this.completeQueryList.addAll(tempList);
	};

	public TreeSet<StatusChangeDB> getCompleteSQLList() {
		return this.completeQueryList;
	};

	public void setListIndices(TreeSet<Integer> intList) {
		this.intList = intList;
	}

	public TreeSet<Integer> getListIndices() {
		return this.intList;
	}

	public void clearTempSQLList() {
		this.queryList.clear();
	};

	public void setSQLList() {

	};

	public int getnEventsInFile() {
		return nEventsInFile;
	}

	public void setnEventsInFile(int nEventsInFile) {
		this.nEventsInFile = nEventsInFile;
	}

	public void setSentTodb(boolean sentTodb) {
		this.sentTodb = sentTodb;
	}

	public boolean sentToDB() {
		return this.sentTodb;
	}

	public void prepareAddBackList(TreeSet<StatusChangeDB> queryList) {
		// this.queryList = queryList;
		for (StatusChangeDB statusChangeDB : queryList) {
			System.out.println(statusChangeDB);
		}
	}

	public void shutdown() {
		this.mainFrameQuery.shutdown();
	}

}
