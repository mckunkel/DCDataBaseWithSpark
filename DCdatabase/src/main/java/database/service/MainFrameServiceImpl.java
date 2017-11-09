package database.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.apache.spark.sql.Dataset;
import org.jlab.groot.data.H2F;

import database.faults.ChannelLogic;
import database.faults.DeadWireLogic;
import database.faults.FaultLogic;
import database.faults.FuseLogic;
import database.faults.HotWireLogic;
import database.faults.PinLogic;
import database.faults.SignalLogic;
import database.objects.StatusChangeDB;
import database.objects.Status_change_type;
import database.ui.panels.DataPanel;
import database.ui.panels.SQLPanel;
import database.utils.Coordinate;
import spark.utils.MainFrameQuery;

public class MainFrameServiceImpl implements MainFrameService {

	private MainFrameQuery mainFrameQuery;
	private Map<Coordinate, H2F> occupanciesByCoordinate = null;
	private Map<Coordinate, Dataset<StatusChangeDB>> dataSetByCoordinate = null;
	private Map<Coordinate, Dataset<StatusChangeDB>> dataComparedSetByCoordinate = null;

	private TreeSet<StatusChangeDB> queryList = null;
	private TreeSet<StatusChangeDB> addBackList = null;
	private Status_change_type status_change_type = null;
	private TreeSet<StatusChangeDB> completeQueryList = null;
	private TreeSet<Integer> intList = null;
	private int nEventsInFile;
	private int runNumber;
	private int sectorNumber;
	private int superLayerNumber;
	private boolean sentTodb;

	private int fault = 0;
	private int bundle = -1000;
	private String userName = null;
	private boolean mouseReady;

	private FaultLogic faultLogic = null;
	// testing passing panels ///I think this is the wrong idea
	private DataPanel dataPanel;
	private SQLPanel sqlPanel;

	private double userPercent;

	public MainFrameServiceImpl() {
		this.mainFrameQuery = new MainFrameQuery();
		this.occupanciesByCoordinate = new HashMap<Coordinate, H2F>();
		this.dataSetByCoordinate = new HashMap<Coordinate, Dataset<StatusChangeDB>>();
		this.dataComparedSetByCoordinate = new HashMap<Coordinate, Dataset<StatusChangeDB>>();

		this.queryList = new TreeSet<>();
		this.addBackList = new TreeSet<>();
		this.completeQueryList = new TreeSet<>();
		this.intList = new TreeSet<>();

		this.mouseReady = false;

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
		this.addBackList = queryList;
	}

	public TreeSet<StatusChangeDB> getAddBackList() {
		return this.addBackList;
	}

	public void removeRowFromMYSQLQuery(TreeSet<StatusChangeDB> statusChangeDBs) {
		ArrayList<StatusChangeDB> listToRemove = new ArrayList<>();
		// System.out.println("removeRow was called in MainFrameServiceImpl");
		for (StatusChangeDB ro : statusChangeDBs) {
			for (StatusChangeDB statusChangeDB : this.completeQueryList) {
				if (statusChangeDB.getSector().equals(ro.getSector())
						&& statusChangeDB.getSuperlayer().equals(ro.getSuperlayer())
						&& statusChangeDB.getLoclayer().equals(ro.getLoclayer())
						&& statusChangeDB.getLocwire().equals(ro.getLocwire())) {
					listToRemove.add(statusChangeDB);
					// System.out.println(statusChangeDB.getSector() + " " +
					// statusChangeDB.getSuperlayer() + " "
					// + statusChangeDB.getLoclayer() + " " +
					// statusChangeDB.getLocwire());
				}
			}
		}
		this.completeQueryList.removeAll(listToRemove);
	}

	public void clearAddBackList() {
		this.addBackList.clear();

	}

	@Override
	public FaultLogic getFault() {
		return this.faultLogic;
	}

	@Override
	public int getFaultNum() {
		return this.fault;
	}

	@Override
	public void setFault(int fault) {
		this.fault = fault;
		switch (fault) {
		case 0:
			this.faultLogic = new ChannelLogic();
			break;
		case 1:
			this.faultLogic = new PinLogic();
			break;
		case 2:
			this.faultLogic = new FuseLogic();
			break;
		case 3:
			this.faultLogic = new SignalLogic();
			break;
		case 4:
			this.faultLogic = new DeadWireLogic();
			break;
		case 5:
			this.faultLogic = new HotWireLogic();
			break;
		default:
			break;
		}
	}

	@Override
	public int getBundle() {
		return bundle;
	}

	@Override
	public void setBundle(int bundle) {
		this.bundle = bundle;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void shutdown() {
		this.mainFrameQuery.shutdown();
	}

	public void setMouseReady() {
		this.mouseReady = true;
	}

	public boolean getMouseReady() {
		return this.mouseReady;
	}

	@Override
	public void setBrokenOrFixed(Status_change_type brokenOrFixed) {
		this.status_change_type = brokenOrFixed;
	}

	public Status_change_type getBrokenOrFixed() {
		return this.status_change_type;
	}

	public void setDataPanel(DataPanel dataPanel) {
		this.dataPanel = dataPanel;
	}

	public DataPanel getDataPanel() {
		return this.dataPanel;
	}

	public void setSQLPanel(SQLPanel sqlPanel) {
		this.sqlPanel = sqlPanel;
	}

	public SQLPanel getSQLPanel() {
		return this.sqlPanel;
	}

	public void setUserPercent(double userPercent) {
		this.userPercent = userPercent;
	}

	public double getUserPercent() {
		return this.userPercent;
	}
}
