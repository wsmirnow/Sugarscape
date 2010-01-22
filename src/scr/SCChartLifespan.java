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
 * File: scr/SCChartLifespan.java
 */
package scr;

import eawag.chart.Chart;
import eawag.grid.Bug;

/**
 * Chart Diagram for average Age of Bugs
 * 
 * @author Stefan H., Waldemar S.
 */
public class SCChartLifespan extends Chart {

	/**
	 * Grid
	 */
	public SCGrid grid;

	/**
	 * Chart
	 */
	public SCChartLifespan() {
		setHTitle("Zeit");
		setVTitle("Lebenserwartung");
		setComment("Zeigt die durchschnittliche Lebenserwartung"
				+ " der Agenten über die Zeit an");
	}

	/**
	 * Action Method
	 */
	public void action() {
		
		int lifespan = 0;
		int poor = 0;
		int rich = 0;
		
		lifespan = grid.getDeadAgents() == 0 ? 0 : grid.getAgeOfDeath() / grid.getDeadAgents();
//		poor = grid.getD_Poor() == 0 ? 0 : grid.getD_AgePoor() / grid.getD_Poor();
//		rich = grid.getD_Rich() == 0 ? 0 : grid.getD_AgeRich() / grid.getD_Rich();			

		lineTo("Lebenserwartung", Chart.TYPE_LINE, grid.getTop().getTime(), lifespan);
//		lineTo("Arm", Chart.TYPE_LINE, grid.getTop().getTime(),
//				poor);
//		lineTo("Reich", Chart.TYPE_LINE, grid.getTop().getTime(),
//				rich);
	}

}
