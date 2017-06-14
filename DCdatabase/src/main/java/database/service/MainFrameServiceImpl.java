package database.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.jlab.groot.data.H2F;

import database.utils.Coordinate;
import spark.utils.MainFrameQuery;

public class MainFrameServiceImpl implements MainFrameService {

	private MainFrameQuery mainFrameQuery;
	private Dataset<Row> queryDF = null;

	private Map<Coordinate, H2F> occupanciesByCoordinate = null;

	public MainFrameServiceImpl() {
		this.mainFrameQuery = new MainFrameQuery();
		this.occupanciesByCoordinate = new HashMap<Coordinate, H2F>();
		createHistograms();
	}

	public void setDataset(Dataset<Row> queryDF) {
		this.queryDF = queryDF;
		this.mainFrameQuery.setDataset(queryDF);
	}

	public Dataset<Row> getDataset() {
		return this.queryDF;
	}

	public Dataset<Row> getBySector(int sector) {
		return this.mainFrameQuery.getBySector(sector);
	}

	public Dataset<Row> getBySuperLayer(int superLayer) {
		return this.mainFrameQuery.getBySuperLayer(superLayer);
	}

	public Dataset<Row> getByLayer(int layer) {
		return this.mainFrameQuery.getByLayer(layer);
	}

	public Dataset<Row> getBySectorAndSuperLayer(int sector, int superLayer) {
		return this.mainFrameQuery.getBySectorAndSuperLayer(sector, superLayer);
	}

	public Dataset<Row> getBySectorAndSuperLayerAndLayer(int sector, int superLayer, int layer) {
		return this.mainFrameQuery.getBySectorAndSuperLayerAndLayer(sector, superLayer, layer);
	}

	public Map<Coordinate, H2F> getHistogramMap() {
		return this.occupanciesByCoordinate;
	}

	private void createHistograms() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				occupanciesByCoordinate.put(new Coordinate(i, j),
						new H2F("Occupancy all hits SL" + i + "sector" + j, "", 112, 1, 113, 6, 1, 7));
				occupanciesByCoordinate.get(new Coordinate(i, j)).setTitleX("Wire Sector" + (j + 1));
				occupanciesByCoordinate.get(new Coordinate(i, j)).setTitleY("Layer SL" + (i + 1));
			}
		}
	}

	public H2F getHistogramByMap(int superLayer, int sector) {
		return this.occupanciesByCoordinate.get(new Coordinate(superLayer - 1, sector - 1));
	}

	public void shutdown() {
		this.mainFrameQuery.shutdown();
	}

}
