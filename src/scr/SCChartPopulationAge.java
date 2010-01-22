/**
 * Sugarscape
 * Copyright 2009-2010 Denis M., Stefan H., Waldemar S.
 * 
 * Author: Denis M., Stefan H., Waldemar S.
 * Website: http://github.com/CallToPower/Sugarscape
 * AG: Lecture "Regelbasierte Modelle" at the University of Osnabrueck (Germany)
 * 
 * The Sugarscape is free Software:
 * You can redistribute it and/or modify it under the Terms of the
 * GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the
 * License,
 * or (at your Option) any later Version.
 * 
 * The Sugarscape Application is distributed WITHOUT ANY WARRANTY;
 * without even the implied Warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.
 * See the GNU General Public License for more Details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Sugarscape Application.
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 * File: scr/SCChartPopulationAge.java
 */
package scr;

import java.util.HashMap;
import eawag.chart.Chart;
import eawag.grid.Bug;

/**
 * Chart Diagram for the Population Age
 * 
 * @author Waldemar S.
 */
public class SCChartPopulationAge extends Chart {

	private static final SCHelper helper = new SCHelper();

	/**
	 * Grid
	 */
	public SCGrid grid;
	
	/**
	 * Map for the data (bugAge -> bugCount)
	 */
	HashMap<Integer, Integer> bugAgeMap;

	/**
	 * Constructor
	 */
	public SCChartPopulationAge() {
		setVTitle("Anzahl Agenten");
		setHTitle("Altersspanne");
		setComment("Ordnet Agenten deren Altersspanne (5er Schritte) zu.");
		
		bugAgeMap = new HashMap<Integer, Integer>();
	}

	/**
	 * Action Method
	 */
	public void action() {

		if (grid == null || helper == null)
			return;
		
		// clear all data of the last step
		bugAgeMap.clear();

		for (int x = 0; x < grid.xsize; x++)
			for (int y = 0; y < grid.ysize; y++) {
				Bug bug = grid.getBug(x, y, 1);
				if (bug != null && bug instanceof SCBug) {
					int bugAge = ((SCBug) bug).getCurrAge();
					int bugCount = 0;
					if (bugAgeMap.containsKey(bugAge))
						bugCount = bugAgeMap.get(bugAge);
					bugCount++;
					bugAgeMap.put(bugAge, bugCount);
				}
			}

		removeAllPoints("Altersspanne");
		int bugCount = 0;

		for (int age = 0; age <= helper.getMaxAgeMax(); age++) {

			if (bugAgeMap.containsKey(age))
				bugCount += bugAgeMap.get(age);

			if (age % 5 == 0) {
				addHGuide(age, String.valueOf(age));
				lineTo("Altersspanne", Chart.TYPE_BALKEN, age, bugCount);
				bugCount = 0;
			}
		}
	}
}
