package scr;


import java.util.Collections;
import java.util.Vector;

import eawag.chart.Chart;
import eawag.grid.Bug;

public class SCChartGini extends Chart {
	
	SCGrid grid;
	
	public SCChartGini() {
		setHTitle("Zeit");
		setVTitle("Ginikoeffizient");
		setComment("Zeigt den Ginikoeffizienten an");
	}
	
	public void condition() {
		
		super.condition();
		
		Vector<Double> vec = new Vector<Double>();
		double real = 0.0;		//tatsaechliche verteilung
		double optimal = 0.0;	//Gleichverteilung
		double gini = 0.0;
		int popWealth = 0;
		
		for (int x = 0; x < grid._xsize; x++)
			for (int y = 0; y < grid.ysize; y++) {
				Bug bug = grid.getBug(x, y, 1);
				if (bug != null && bug instanceof SCBug) {
					vec.add((double)((SCBug)bug).getCurrWealth());
				}
			}
		
		Collections.sort(vec);
		
		for(double d : vec){
			real += (0.5*d + popWealth);
			popWealth += d;
		}
		
		optimal = 0.5 * vec.size() * popWealth;	//Gleichverteilung
		
		gini = (optimal - real) / optimal;

		lineTo("Ginikoeffizient", Chart.TYPE_LINE, grid.getTop().getTime(), gini);
		
	}
	
}
