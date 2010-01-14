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
 * Chart Diagram for the Population
 * 
 * @author Waldemar S.
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
		setHTitle("H");
		setVTitle("V");
		setComment("comment");
	}

	/**
	 * Condition
	 */
	public void condition() {

		super.condition();

		if (grid == null)
			return;

		Vector<Integer> vec = new Vector<Integer>();
		int popWealth = 0;

		for (int x = 0; x < grid._xsize; x++)
			for (int y = 0; y < grid.ysize; y++) {
				Bug bug = grid.getBug(x, y, 1);
				if (bug != null && bug instanceof SCBug) {
					int bugWealth = ((SCBug) bug).getCurrWealth();
					popWealth += bugWealth;
					vec.add(bugWealth);
				}
			}

		Collections.sort(vec);
	}
}
