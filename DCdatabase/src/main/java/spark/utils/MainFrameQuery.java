package spark.utils;

import static org.apache.spark.sql.functions.col;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class MainFrameQuery extends AbstractQuery {
	Dataset<Row> queryDF;

	public MainFrameQuery() {

	}

	public void setDataset(Dataset<Row> queryDF) {
		this.queryDF = queryDF;
	}

	public Dataset<Row> getBySector(int sector) {
		return queryDF.filter(col("sector").equalTo(sector));
	}

	public Dataset<Row> getBySuperLayer(int superLayer) {
		return queryDF.filter(col("superLayer").equalTo(superLayer));
	}

	public Dataset<Row> getByLayer(int layer) {
		return queryDF.filter(col("layer").equalTo(layer));
	}

	public Dataset<Row> getBySectorAndSuperLayer(int sector, int superLayer) {
		return this.getBySector(sector).filter(col("superLayer").equalTo(superLayer));
	}

	public Dataset<Row> getBySectorAndSuperLayerAndLayer(int sector, int superLayer, int layer) {
		return this.getBySectorAndSuperLayer(sector, superLayer).filter(col("layer").equalTo(layer));
	}

}
