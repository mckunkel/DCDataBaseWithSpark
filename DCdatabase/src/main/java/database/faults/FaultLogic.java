package database.faults;

import java.util.List;
import java.util.Map;

import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.EmbeddedCanvas;

public interface FaultLogic {

	public void drawLogic(EmbeddedCanvas canvas, H2F aH2f, int xBin, int yBin);

	public void setFaultToDB();

	public int setBundle(int xBin, int yBin);

	public Map<Integer, List<Integer>> getFaultWires(int xBin, int yBin);

}
