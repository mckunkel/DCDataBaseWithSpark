package domain.workingset;

public class MainFrameServiceImpl implements MainFrameService {

	private int fault = -1000;
	private int bundle = -1000;
	private String userName = null;

	@Override
	public int getFault() {
		return fault;
	}

	@Override
	public void setFault(int fault) {
		this.fault = fault;
	}

	@Override
	public int getBundle() {
		return bundle;
	}

	@Override
	public void setBundle(int bundle) {
		this.bundle = bundle;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
