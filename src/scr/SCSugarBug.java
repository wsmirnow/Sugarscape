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
 * File: scr/SCSugarBug.java
 */
package scr;

import eawag.grid.Bug;
import eawag.grid.Depiction;

/**
 * SugarBug
 * 
 * @author Denis Meyer, Waldemar S.
 */
public class SCSugarBug extends Bug {

	private static final SCHelper helper = new SCHelper();

	/************************************************/
	// Private Variables
	/************************************************/

	/**
	 * current amount of sugar
	 */
	private int r;

	/**
	 * amount of sugar this step
	 */
	private double _r;

	/**
	 * max amount of sugar
	 */
	private int c;

	/**
	 * Multiplicator for depictionname
	 */
	private int sugarMltp = 5;
	/**
	 * Depiction basename (Example: Depiction5 for 5 * sugarMltp)
	 */
	private static final String SERNONAME = "Depiction";

	/************************************************/
	// Constructors / setValue-Function
	/************************************************/

	/**
	 * Default Constructor
	 */
	public SCSugarBug() {
		setValue(0);
		this._r = 0;
		setActive(false);
		this.c = r;
	}

	/**
	 * Constructor
	 * 
	 * @param amountOfSugar
	 *            Amount of Sugar
	 */
	public SCSugarBug(int amountOfSugar) {
		setValue(amountOfSugar);
		this._r = amountOfSugar;
		setActive(amountOfSugar > 0);
		this.c = r;
	}

	/**
	 * Sets the Value(s)
	 * 
	 * @param amountOfSugar
	 *            Amount of Sugar
	 */
	private void setValue(int amountOfSugar) {
		this.r = (amountOfSugar > 0) ? amountOfSugar : 0;
		updateDepiction();
	}

	/************************************************/
	// Other Functions
	/************************************************/

	/**
	 * Calculates the Amount of Sugar in the next Step
	 */
	public void calcAmountOfSugarNextStep() {

		double _r_tmp = getNext_R();

		if (_r_tmp != -1) {
			this._r = _r_tmp;
		}
	}

	/**
	 * Updates the Depiction
	 */
	public void updateDepiction() {
		String depictName = getSernoname() + (r / sugarMltp);
		super.setDepiction((Depiction) (getBase().findBySerno(depictName)));
	}

	/**
	 * Sets the Depiction
	 * 
	 * @param depict
	 *            Depiction
	 */
	public void setDepiction(Depiction depict) {
		if (z != 0)
			return;

		int depictR = getRbySerno(depict.serno);
		_r = (double) depictR * sugarMltp;
		setValue(depictR);
		setActive(depictR > 0);
		super.setDepiction(depict);
	}

	/**
	 * Condition-Function. Set Amount of Sugar.
	 */
	public void condition() {

		setValue((int) (this._r - (this._r % 1)));
		super.condition();
	}

	/**
	 * Action-Function
	 */
	public void action() {
		calcAmountOfSugarNextStep();
	}

	/**
	 * Return new calculated _r, that would be the Amount of Sugar next Step.
	 * 
	 * @return new calculated _r
	 */
	private double getNext_R() {
		// return Math.min(this._r + helper.expansionRatio,
		// helper.expansionRatio.maxAmountOfSugarInSugarAgent);
		return Math.min(this._r + helper.getExpansionRatio(), this.c);
	}

	/**
	 * Returns R by Serno
	 * 
	 * @param serno
	 *            Serno
	 * @return R by Serno
	 */
	public static int getRbySerno(String serno) {

		int r = -1;
		if (serno.startsWith(getSernoname())) {
			try {
				r = Integer.parseInt(serno.replace(getSernoname(), ""));
			} catch (NumberFormatException ex) {
				System.err.println("Can not parse r by sernoname: " + serno);
			}
		}
		return r;
	}

	/**
	 * Returns the Sugar and removes it
	 * 
	 * @param amount
	 *            Amount of Sugar to return and internally remove
	 * @return the Sugar and removes it
	 */
	public int getSugar(int amount) {
		if (r >= amount) {
			_r -= amount;
			return amount;
		} else {
			_r = 0;
			return r;
		}
	}

	/************************************************/
	// Getter-Functions
	/************************************************/

	/**
	 * Returns the current Amount of Sugar
	 * 
	 * @return the current Amount of Sugar
	 */
	public int getCurrentAmountOfSugar() {
		return this.r;
	}

	/**
	 * Return the max amount of sugar
	 * 
	 * @return max amount of sugar
	 */
	public int getMaxAmountOfSugar() {
		return this.c;
	}

	/************************************************/
	// Setter-Functions
	/************************************************/

	/**
	 * Sets a new Amount of Sugar
	 * 
	 * @param amountOfSugar
	 *            the Amount of Sugar to set
	 */
	public void setAmountOfSugar(int amountOfSugar) {
		setValue(amountOfSugar);
	}

	/**
	 * Returns the Serno Name
	 * 
	 * @return the Serno Name
	 */
	public static String getSernoname() {
		return SERNONAME;
	}
}
