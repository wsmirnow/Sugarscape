package scr;


import java.util.Collections;
import java.util.Vector;

import eawag.chart.Chart;
import eawag.grid.Bug;

public class SCChartGini extends Chart {
	
	SCGrid grid;
	
	public SCChartGini() {
		setHTitle("H");
		setVTitle("V");
		setComment("comment");
	}
	
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
					int bugWealth = ((SCBug)bug).getCurrWealth();
					popWealth += bugWealth;
					vec.add(bugWealth);
				}
			}
		
		Collections.sort(vec);
	}
}
