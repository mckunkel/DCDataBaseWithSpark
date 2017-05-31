package database.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import spark.utils.MainFrameQuery;

public class MainFrameServiceImpl implements MainFrameService {

	private MainFrameQuery mainFrameQuery;

	public MainFrameServiceImpl(Dataset<Row> queryDF) {
		this.mainFrameQuery = new MainFrameQuery(queryDF);
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

	public void shutdown() {
		this.mainFrameQuery.shutdown();
	}

}
