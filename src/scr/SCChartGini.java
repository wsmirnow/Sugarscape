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
 * File: scr/SCChartGini.java
 */
package scr;

import java.util.Collections;
import java.util.Vector;

import eawag.chart.Chart;
import eawag.grid.Bug;

/**
 * Chart Diagram for Gini coefficient
 * 
 * @author Waldemar S. Stefan H.
 * 
 */
public class SCChartGini extends Chart {

	/**
	 * Grid
	 */
	SCGrid grid;

	/**
	 * Chart
	 */
	public SCChartGini() {
		setHTitle("Zeit");
		setVTitle("Ginikoeffizient");
		setComment("Zeigt den Ginikoeffizienten an");
	}

	/**
	 * Condition Method
	 */
	public void condition() {

		super.condition();

		if (grid == null)
			return;

		Vector<Double> vec = new Vector<Double>();
		double real = 0.0; // tatsaechliche verteilung
		double optimal = 0.0; // Gleichverteilung
		double gini = 0.0;
		int popWealth = 0;

		for (int x = 0; x < grid._xsize; x++)
			for (int y = 0; y < grid.ysize; y++) {
				Bug bug = grid.getBug(x, y, 1);
				if (bug != null && bug instanceof SCBug) {
					vec.add((double) ((SCBug) bug).getCurrWealth());
				}
			}

		Collections.sort(vec);

		for (double d : vec) {
			real += (0.5 * d + popWealth);
			popWealth += d;
		}

		optimal = 0.5 * vec.size() * popWealth; // Gleichverteilung

		gini = (optimal - real) / optimal;

		lineTo("Ginikoeffizient", Chart.TYPE_LINE, grid.getTop().getTime(),
				gini);

	}

}
