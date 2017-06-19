package database.service;

import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.jlab.groot.data.H2F;

import database.objects.StatusChangeDB;
import database.utils.Coordinate;

public interface MainFrameService {

	public void setDataset(Dataset<Row> queryDF);

	public Dataset<Row> getDataset();

	public Dataset<Row> getBySector(int sector);

	public Dataset<Row> getBySuperLayer(int superLayer);

	public Dataset<Row> getByLayer(int layer);

	public Dataset<Row> getBySectorAndSuperLayer(int sector, int superLayer);

	public Dataset<Row> getBySectorAndSuperLayerAndLayer(int sector, int superLayer, int layer);

	public Map<Coordinate, H2F> getHistogramMap();

	public H2F getHistogramByMap(int superLayer, int sector);

	public Dataset<Row> getDatasetByMap(int superLayer, int sector);

	public void setDatasetByMap(Map<Coordinate, Dataset<Row>> queryDF);

	public void prepareMYSQLQuery(List<StatusChangeDB> queryList);

	public void shutdown();

}
