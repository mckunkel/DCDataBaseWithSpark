package domain;

public class ModulusTest {

	public static void main(String[] args) {
		int placer;
		int xPlace;
		int iInc;
		for (int i = 1; i <= 112; i++) {
			xPlace = (i - 1) / 16 * 16;
			iInc = xPlace / 8;
			placer = (i + iInc) % 18;
			System.out.println("Modulus 8 of " + i + " = " + placer + "  test division " + xPlace);
			// System.out.println(testSwitch(i));
		}
	}

	public static int testSwitch(int x) {
		switch (x) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:

			return 1;
		case 6:
		case 7:
		case 8:
		case 9:
			return 10;
		case 10:
		case 11:
			return 100;
		default:
			return -10000;
		}
	}
}
