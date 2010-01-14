package scr;

import eawag.chart.Chart;
import eawag.grid.Bug;

public class SCChartLifespan extends Chart{
	
	public SCGrid grid;
	
	public SCChartLifespan(){
		setHTitle("Zeit");
		setVTitle("Lebenserwartung");
		setComment("Zeigt die durchschnittliche Lebenserwartung" +
				" der Agenten über die Zeit an");
	}
	
	public void condition(){
		
		super.condition();
		
		int lifespan = 0;
		for (int x = 0; x < grid.xsize; x++)
			for (int y = 0; y < grid.ysize; y++) {
				Bug bug = grid.getBug(x, y, 1);
				if (bug != null && bug instanceof SCBug) {
					lifespan = ((SCBug)bug).getDeadAgents() == 0 ? 0 :
							((SCBug)bug).getDyingAge() / ((SCBug)bug).getDeadAgents();
				}
			}
		
		lineTo("Lebenserwartung", Chart.TYPE_LINE, grid.getTop().getTime(), lifespan);
	}

}
