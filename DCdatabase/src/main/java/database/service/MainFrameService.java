package database.service;

import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.jlab.groot.data.H2F;

import database.objects.StatusChangeDB;
import database.utils.Coordinate;

public interface MainFrameService {

	public Dataset<Row> getBySectorAndSuperLayer(int sector, int superLayer);

	public Map<Coordinate, H2F> getHistogramMap();

	public H2F getHistogramByMap(int superLayer, int sector);

	public Dataset<Row> getDatasetByMap(int superLayer, int sector);

	public Map<Coordinate, Dataset<Row>> getDataSetMap();

	public void prepareMYSQLQuery(List<StatusChangeDB> queryList);

	public void shutdown();

}
