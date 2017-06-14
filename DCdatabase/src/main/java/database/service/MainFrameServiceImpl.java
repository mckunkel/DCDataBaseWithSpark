package database.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;

import database.objects.TBHits;
import spark.utils.MainFrameQuery;
import spark.utils.SparkManager;

public class MainFrameServiceImpl implements MainFrameService {

	private MainFrameQuery mainFrameQuery;
	private Dataset<Row> queryDF = null;
	private List<Row> queryList = null;

	// testing
	private List<TBHits> tbHitList = new ArrayList<TBHits>();
	private Encoder<TBHits> TBHitsEncoder = Encoders.bean(TBHits.class);

	public MainFrameServiceImpl() {
		this.mainFrameQuery = new MainFrameQuery();
		this.queryList = new ArrayList<Row>();
	}

	public void setDatasetStruct(Dataset<Row> queryDF) {
		ExpressionEncoder<Row> encoder = RowEncoder.apply(queryDF.schema());
		this.queryDF = new Dataset<Row>(SparkManager.getSession(), queryDF.queryExecution().logical(), encoder);
		setDataset(queryDF);
	}

	public void setDataset(Dataset<Row> queryDF) {
		this.queryDF = queryDF;
	}

	public void setDataList(List<Row> collectAsList) {
		this.queryList = collectAsList;
	}

	public Dataset<Row> getDataset() {
		return this.queryDF;
	}

	public List<Row> getDatasetAsList() {
		return this.queryList;
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

	public void setSchema() {

	}

}
