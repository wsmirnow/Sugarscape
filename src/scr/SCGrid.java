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
 * File: scr/SCGrid.java
 */
package scr;

import eawag.grid.Bug;
import eawag.grid.Grid;

/**
 * Grid
 * 
 * @author Denis Meyer
 */
public class SCGrid extends Grid {
	private static final SCHelper helper = new SCHelper();

	/************************************************/
	// Private Variables
	/************************************************/

	private boolean init = true;

	/************************************************/
	// Other Functions
	/************************************************/

	/**
	 * Action-Function 1. Put SCSugarAgents on every Cell
	 */
	public void action() {

		if (init) {

			Bug b;
			for (int x = 0; x < this.getXSize(); x++) {
				for (int y = 0; y < this.getYSize(); y++) {

					// assume only one level
					b = this.getBug(x, y, 0);
					// amount of sugar
					int r = -1;

					if (b != null) {
						// Bug has been initialized
						if (b instanceof SCSugarBug) {
							// get amount of sugar was set
							r = ((SCSugarBug) b).getCurrentAmountOfSugar();
						}
						b.leave();
					}

					b = newSCSugarBug(r);
					b.moveBug(x, y, 0);
					b.join(this);
					((SCSugarBug) b).updateDepiction();
				}
			}
			init = false;
		}
	}

	/************************************************/
	// Internal Helper-Functions
	/************************************************/

	/**
	 * Returns a new SCBug
	 * 
	 * @return SCBug a new SCBug
	 */
	private SCSugarBug newSCSugarBug(int amountSugar) {

		if (amountSugar < 0)
			amountSugar = getHelper().getRandomIntWithinLimits(0,
					getHelper().maxAmountOfSugarInSugarAgent);

		return new SCSugarBug(amountSugar);
	}

	/**
	 * Returns the Helper
	 * 
	 * @return the Helper
	 */
	public static SCHelper getHelper() {
		return helper;
	}
}
