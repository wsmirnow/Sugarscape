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
		setVTitle("Anzahl Agenten");
		setComment("Zeigt die Anzahl reicher und armer Agenten"
				+ " über die Zeit an");
	}

	/**
	 * Action Method
	 */
	public void action() {

		int poor = 0;
		int rich = 0;
		
		poor = grid.getD_Poor() == 0 ? 0 : grid.getD_Poor();
		rich = grid.getD_Rich() == 0 ? 0 : grid.getD_Rich();
//		for (int x = 0; x < grid.xsize && !rdy; x++)
//			for (int y = 0; y < grid.ysize && !rdy; y++) {
//				Bug bug = grid.getBug(x, y, 1);
//				if (bug != null && bug instanceof SCBug) {
//					poor = ((SCBug) bug).getD_Poor() == 0 ? 0
//							: ((SCBug) bug).getD_Poor();
//					rich = ((SCBug) bug).getD_Rich() == 0 ? 0
//							: ((SCBug) bug).getD_Rich();
//					rdy = true;
//					}
//				}
			
		lineTo("Arm", Chart.TYPE_LINE, grid.getTop().getTime(),
				poor);
		lineTo("Reich", Chart.TYPE_LINE, grid.getTop().getTime(),
				rich);
		
		poor = 0;
		rich = 0;
	}

}
