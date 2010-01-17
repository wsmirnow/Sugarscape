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
		
		Vector<Integer> vec = new Vector<Integer>();
		int popWealth = 0;
		int count = 0;
		int bugWealth = 0;
		
		for (int x = 0; x < grid._xsize; x++)
			for (int y = 0; y < grid.ysize; y++) {
				Bug bug = grid.getBug(x, y, 1);
				if (bug != null && bug instanceof SCBug) {
					count++;
					bugWealth = ((SCBug)bug).getCurrWealth();
					popWealth += bugWealth;
					vec.add(bugWealth);
				}
			}
		
		Collections.sort(vec);
		
		double perBug = (double)(vec.size()/100) * 0.01; //ein prozent Bug normalisiert
		double perWealth = (double)(popWealth/100);	//ein prozent des wohlstands
		
		Vector<Double> percent = new Vector<Double>();	//prozentualer wohlsand geordnet v_i
		for(int i = 0; i < vec.size(); i++){
			percent.add((double)(vec.get(i)/perWealth) * 0.01);	//wohlstand des bugs i durch ein prozent normiert
		}
		
		double gini = calculateGini(percent, perBug);

		lineTo("Ginikoeffizient", Chart.TYPE_LINE, grid.getTop().getTime(), gini);
		
	}
	
	private static double getY_i(Vector<Double> percent, int i){	//sum_i v_i
		
		double y_i = 0.0;
		
		for(int j = 0; j < i; j++){
			y_i += percent.get(j);
		}
		return y_i;
	}
	
	private static double calculateArea(Vector<Double> percent, double perBug){
		double area = 0.0;
		
		for(int i = 0; i < percent.size(); i++){
			area = perBug * (0.5 * (getY_i(percent, i) + getY_i(percent, i-1)));
		}
		return area;
	}
	
	private static double calculateGini(Vector<Double> percent, double perBug){
		
		return ((double)(1 - (2 * calculateArea(percent, perBug))));
	}
}
