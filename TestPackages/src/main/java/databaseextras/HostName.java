/*  +__^_________,_________,_____,________^-.-------------------,
 *  | |||||||||   `--------'     |          |                   O
 *  `+-------------USMC----------^----------|___________________|
 *    `\_,---------,---------,--------------'
 *      / X MK X /'|       /'
 *     / X MK X /  `\    /'
 *    / X MK X /`-------'
 *   / X MK X /
 *  / X MK X /
 * (________(                @author m.c.kunkel
 *  `------'
*/
package databaseextras;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HostName {

	public static String getHostName() {
		String retString = null;
		try {

			// run the Unix "hostname" command
			// using the Runtime exec method:
			Process p = Runtime.getRuntime().exec("hostname");

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			retString = findDomain(stdInput.readLine());

		} catch (IOException e) {
			System.out.println("exception happened - here's what I know: ");
			e.printStackTrace();
			System.exit(-1);
		}
		return retString;
	}

	private static String findDomain(String str) {
		String containsStr = null;
		if (str.contains("ikp")) {
			containsStr = "You are on IKP server";
		} else if (str.contains("jlab.org")) {
			containsStr = "You are on JLab server";
		} else {
			containsStr = "You are on unknown to this server";

		}
		return containsStr;
	}

	public static void main(String args[]) {
		System.out.println(getHostName());

	}
}
