package database.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface MainFrameService {

	public Dataset<Row> getBySector(int sector);

	public Dataset<Row> getBySuperLayer(int superLayer);

	public Dataset<Row> getByLayer(int layer);

	public Dataset<Row> getBySectorAndSuperLayer(int sector, int superLayer);

	public Dataset<Row> getBySectorAndSuperLayerAndLayer(int sector, int superLayer, int layer);

	public void shutdown();

}
