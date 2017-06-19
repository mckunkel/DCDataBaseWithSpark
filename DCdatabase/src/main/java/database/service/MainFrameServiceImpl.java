package database.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.jlab.groot.data.H2F;

import database.objects.StatusChangeDB;
import database.utils.Coordinate;
import spark.utils.MainFrameQuery;

public class MainFrameServiceImpl implements MainFrameService {

	private MainFrameQuery mainFrameQuery;
	private InsertMYSqlQuery insertMYSqlQuery;
	private Dataset<Row> queryDF = null;
	private Coordinate coordinate;
	private Map<Coordinate, H2F> occupanciesByCoordinate = null;
	private Map<Coordinate, Dataset<Row>> dataSetByCoordinate = null;

	public MainFrameServiceImpl() {
		this.mainFrameQuery = new MainFrameQuery();
		this.insertMYSqlQuery = new InsertMYSqlQueryImpl();
		this.occupanciesByCoordinate = new HashMap<Coordinate, H2F>();
		this.dataSetByCoordinate = new HashMap<Coordinate, Dataset<Row>>();

		createHistograms();
		createDatasets();
	}

	public Dataset<Row> getBySectorAndSuperLayer(int sector, int superLayer) {
		this.mainFrameQuery.setDataset(getDatasetByMap(superLayer, sector));
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
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				Dataset<Row> test = null;
				dataSetByCoordinate.put(new Coordinate(i, j), test);
			}
		}
	}

	public H2F getHistogramByMap(int superLayer, int sector) {
		return this.occupanciesByCoordinate.get(new Coordinate(superLayer - 1, sector - 1));
	}

	public Dataset<Row> getDatasetByMap(int superLayer, int sector) {
		return this.dataSetByCoordinate.get(new Coordinate(superLayer - 1, sector - 1));
	}

	public Map<Coordinate, Dataset<Row>> getDataSetMap() {
		return this.dataSetByCoordinate;
	}

	// for inserting into MYSQL
	public void prepareMYSQLQuery(List<StatusChangeDB> queryList) {
		for (StatusChangeDB statusChangeDB : queryList) {
			System.out.println(statusChangeDB);
		}
	}

	public void shutdown() {
		this.mainFrameQuery.shutdown();
	}

}
