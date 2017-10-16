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
package testpackages;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import database.objects.StatusChangeDB;

public class IntrospctorTest {

	public static Map<String, Object> beanProperties(StatusChangeDB bean) {
		try {
			Map<String, Object> map = new HashMap<>();
			Arrays.asList(Introspector.getBeanInfo(bean.getClass(), StatusChangeDB.class).getPropertyDescriptors())
					.stream()
					// filter out properties with setters only
					.filter(pd -> Objects.nonNull(pd.getReadMethod())).forEach(pd -> { // invoke
																						// method
																						// to
																						// get
																						// value
						try {
							Object value = pd.getReadMethod().invoke(bean);
							if (value != null) {
								map.put(pd.getName(), value);
							}
						} catch (Exception e) {
							// add proper error handling here
						}
					});
			return map;
		} catch (IntrospectionException e) {
			// and here, too
			return Collections.emptyMap();
		}
	}

	public static void main(String[] args) throws IntrospectionException {
		// for (PropertyDescriptor propertyDescriptor :
		// Introspector.getBeanInfo(StatusChangeDB.class)
		// .getPropertyDescriptors()) {
		//
		// // propertyEditor.getReadMethod() exposes the getter
		// // btw, this may be null if you have a write-only property
		// System.out.println(propertyDescriptor.getReadMethod());
		// }

		IntrospctorTest introspctorTest = new IntrospctorTest();
		StatusChangeDB astatus = new StatusChangeDB();
		Map<String, Object> aMap = introspctorTest.beanProperties(astatus);
		for (Object string : aMap) {
			System.out.println(string);
		}
	}
}
