package scr;

import eawag.chart.Chart;
import eawag.grid.Bug;

public class SCChartBodycount extends Chart{
	
	/**
	 * Grid
	 */
	public SCGrid grid;

	/**
	 * Chart
	 */
	public SCChartBodycount() {
		setHTitle("Zeit");
		setVTitle("Lebenserwartung");
		setComment("Zeigt die durchschnittliche Lebenserwartung"
				+ " der Agenten über die Zeit an");
	}

	/**
	 * Condition
	 */
	public void condition() {

		super.condition();

		boolean rdy = false;
		int poor = 0;
		int rich = 0;
		for (int x = 0; x < grid.xsize && !rdy; x++)
			for (int y = 0; y < grid.ysize && !rdy; y++) {
				Bug bug = grid.getBug(x, y, 1);
				if (bug != null && bug instanceof SCBug) {
					poor = ((SCBug) bug).getD_Poor() == 0 ? 0
							: ((SCBug) bug).getD_Poor();
					rich = ((SCBug) bug).getD_Rich() == 0 ? 0
							: ((SCBug) bug).getD_Rich();
					rdy = true;
					}
				}
			
		lineTo("Arm", Chart.TYPE_LINE, grid.getTop().getTime(),
				poor);
		lineTo("Reich", Chart.TYPE_LINE, grid.getTop().getTime(),
				rich);
	}

}
