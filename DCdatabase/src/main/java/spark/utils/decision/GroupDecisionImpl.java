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
package spark.utils.decision;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.jlab.groot.data.H2F;

public class GroupDecisionImpl implements GroupDecision {

	public Map<Integer, Pair<Integer, Integer>> BadHVPin(H2F aH2F) {
		return HVPinDecision.BadHVPin(aH2F);
	}

}
