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

public class SCChartWealth extends Chart{
	
	public SCGrid grid;
	
	public SCChartWealth(){
		setHTitle("Zeit");
		setVTitle("Wohlstand");
		setComment("Zeigt den durchschnittlichen Wohlstand der Agenten über die Zeit an");
	}
	
	public void condition(){
		
		super.condition();
		
		int wealth = 0;
		int count = 0;
		
		for (int x = 0; x < grid.xsize; x++)
			for (int y = 0; y < grid.ysize; y++) {
				Bug bug = grid.getBug(x, y, 1);
				if (bug != null && bug instanceof SCBug) {
					count++;
					wealth += ((SCBug)bug).getCurrWealth();
				}
			}
		
		wealth = count > 0 ? wealth/count : 0;
		lineTo("Wohlstand", Chart.TYPE_LINE, grid.getTop().getTime(), wealth);
	}
	
}
