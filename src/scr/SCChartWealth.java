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
 * File: scr/SCChartWealth.java
 */
package scr;

import eawag.chart.Chart;
import eawag.grid.Bug;

/**
 * Helper
 * 
 * @author Stefan H., Waldemar S.
 */
public class SCChartWealth extends Chart {

	/**
	 * Grid
	 */
	public SCGrid grid;

	/**
	 * Constructor
	 */
	public SCChartWealth() {
		setHTitle("Zeit");
		setVTitle("Wohlstand");
		setComment("Zeigt den durchschnittlichen Wohlstand der Agenten über die Zeit an");
	}

	/**
	 * Condition
	 */
	public void condition() {

		super.condition();

		int wealth = 0;
		int minWealth = Integer.MAX_VALUE;
		int maxWealth = Integer.MIN_VALUE;
		int count = 0;

		for (int x = 0; x < grid.xsize; x++)
			for (int y = 0; y < grid.ysize; y++) {
				Bug bug = grid.getBug(x, y, 1);
				if (bug != null && bug instanceof SCBug) {
					count++;
					int bugWealt = ((SCBug) bug).getCurrWealth();
					wealth += bugWealt;
					minWealth = Math.min(minWealth, bugWealt);
					maxWealth = Math.max(maxWealth, bugWealt);
				}
			}

		wealth = count > 0 ? wealth / count : 0;
		minWealth = count > 0 ? minWealth : 0;
		maxWealth = count > 0 ? maxWealth : 0;

		lineTo("Durchschnitt", Chart.TYPE_LINE, grid.getTop().getTime(), wealth);
		lineTo("Min", Chart.TYPE_LINE, grid.getTop().getTime(), minWealth);
		lineTo("Max", Chart.TYPE_LINE, grid.getTop().getTime(), maxWealth);
	}

}
