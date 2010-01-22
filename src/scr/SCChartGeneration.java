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
 * File: scr/SCChartGeneration.java
 */
package scr;

import java.util.HashMap;

import eawag.chart.Chart;
import eawag.grid.Bug;

/**
 * Chart Diagram for the Population
 * 
 * @author Waldemar S.
 */
public class SCChartGeneration extends Chart {

	/**
	 * Grid
	 */
	public SCGrid scGrid;

	/**
	 * Constructor
	 */
	public SCChartGeneration() {
		// set titles
		setHTitle("Generation");
		setVTitle("Anzahl Agenten");
		// setComment("");

		removeAllData();
	}

	/**
	 * Action Method
	 */
	public void action() {

		if (scGrid == null)
			return;

		HashMap<Integer, Integer> generationMap = new HashMap<Integer, Integer>();

		// #Bugs
		int bugCount = 0;
		int minGeneration = Integer.MAX_VALUE;
		int maxGeneration = Integer.MIN_VALUE;
		// max(Bugs / Generation)
		int maxAgentsPGen = 0;

		for (int x = 0; x < scGrid.xsize; x++)
			for (int y = 0; y < scGrid.ysize; y++) {
				Bug child = (Bug) scGrid.getBug(x, y, 1);
				if (child != null && child instanceof SCBug) {
					bugCount += 1;
					int gen = ((SCBug) child).getGeneration();
					int agents = 1;
					if (generationMap.containsKey(gen))
						agents = generationMap.get(gen) + 1;
					generationMap.put(gen, agents);
					minGeneration = Math.min(minGeneration, gen);
					maxGeneration = Math.max(maxGeneration, gen);
					maxAgentsPGen = Math.max(maxAgentsPGen, agents);
				}
			}
				

		if (bugCount == 0) {
			// no more Bugs at Grid
			removeAllPoints("Generation");
			return;
		}

		removeAllData();

		// set vertical Data
		int interval = (maxAgentsPGen > 10 ? maxAgentsPGen / 10 : 10);
		for (int i = 0; i <= maxAgentsPGen + interval; i += interval) {
			addVGuide((double) i, String.valueOf(i));
		}

		// set horizontal data and fill diagram
		for (int i = minGeneration; i <= maxGeneration; i++) {
			addHGuide((double) i, String.valueOf(i));
			int val = 0;

			if (generationMap.containsKey(i))
				val = generationMap.get(i);

			lineTo("Generation", Chart.TYPE_BALKEN, i, val);
		}
		generationMap = null;
	}
}
