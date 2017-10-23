package domain.workingset;

public class MainFrameServiceImpl implements MainFrameService {

	private int fault = -1000;

	@Override
	public int getFault() {
		return fault;
	}

	@Override
	public void setFault(int fault) {
		this.fault = fault;
	}

}
