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
 * File: scr/SCChartSugar.java
 */
package scr;

import eawag.chart.Chart;
import eawag.grid.Bug;

/**
 * Chart Diagram for the Sugar
 * 
 * @author Waldemar S.
 */
public class SCChartSugar extends Chart {

	/**
	 * Grid
	 */
	public SCGrid grid;

	/**
	 * Constructor
	 */
	public SCChartSugar() {
		setHTitle("Zeit");
		setVTitle("Zuckermenge auf dem Feld");
		setComment("Zeigt die Zuckermenge auf dem Feld über die Zeit.");
	}

	/**
	 * Action Method
	 */
	public void action() {

		int sugar = 0;
		for (int x = 0; x < grid.xsize; x++)
			for (int y = 0; y < grid.ysize; y++) {
				Bug bug = grid.getBug(x, y, 0);
				if (bug != null && bug instanceof SCSugarBug)
					sugar += ((SCSugarBug) bug).getCurrentAmountOfSugar();
			}

		lineTo("Zuckermenge", Chart.TYPE_LINE, grid.getTop().getTime(), sugar);
	}

}
