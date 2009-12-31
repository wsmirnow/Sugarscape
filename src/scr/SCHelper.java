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
 * File: scr/SCHelper.java
 */
package scr;

import eawag.model.Agent;

/**
 * Helper
 * 
 * @author Denis Meyer
 */
public class SCHelper extends Agent {

	/************************************************/
	// Variables
	/************************************************/

	/**
	 * Error Representation
	 */
	private static final int ERROR = Integer.MAX_VALUE;

	/**
	 * Max. Amount of Sugar in 1 SugarAgent
	 */
	public static int maxAmountOfSugarInSugarAgent = 20; // c

	/**
	 * Expansion Ratio in between [0, 1]
	 */
	public static double expansionRatio = 0.2; // a

	/**
	 * Mode for "Aufgabe" No. 2
	 */
	private static boolean searchActiveForPartner = false;

	/**
	 * The Movement Speed (per Timestep)
	 */
	public static int movementSpeed = 1;

	/**
	 * Metabolism (Time Steps / sugarConsumingRatio Sugar)
	 */
	private static int metabolism = 2;

	/**
	 * Sugar Consuming Ratio
	 */
	private static int sugarConsumingRatio = 1;

	/**
	 * Step for getting sugarMiningRatio Sugar
	 */
	private static int getSugarStep = 2;

	/**
	 * Sugar Mining Ratio
	 */
	private static int sugarMiningRatio = 4;

	/**
	 * Field of Vision Radius of extended Moore Neighbourhood
	 */
	private static int visionRadius = 4;

	/**
	 * Adds a Divisor in the Function hasEnoughSugar() -> return
	 * (getCurrWealth() >= (getInitialSugar() / divideFactorHasEnoughSugar));
	 */
	private static int divideFactorHasEnoughSugar = 1;

	/**
	 * getRandomSex: random >> sexLimit -> bigger Probability for getting female
	 */
	private static int sexLimit = 50;

	/**
	 * Min. min. Fertility Age
	 */
	private static int minFertilityAgeMin = 12;

	/**
	 * Max. min. Fertility Age
	 */
	private static int minFertilityAgeMax = 15;

	/**
	 * Min. max. Fertility Age for female
	 */
	private static int minFertilityAgeFemale = 40;

	/**
	 * Min. max. Fertility Age for male
	 */
	private static int minFertilityAgeMale = 50;

	/**
	 * Max. Fertility Age for female
	 */
	private static int maxFertilityAgeFemale = 50;

	/**
	 * Max. Fertility Age for male
	 */
	private static int maxFertilityAgeMale = 60;

	/**
	 * Min. Wealth for the 1st Generation
	 */
	private static int minWealth1stGen = 50;

	/**
	 * Max. Wealth for the 1st Generation
	 */
	private static int maxWealth1stGen = 100;

	/**
	 * Min. max. Age
	 */
	private static int maxAgeMin = 60;

	/**
	 * Max. max. Age
	 */
	private static int maxAgeMax = 100;

	/************************************************/
	// Functions
	/************************************************/

	/**
	 * Returns a random Integer between 0 and upperLimit
	 * 
	 * @param upperLimit
	 * @return a random Integer between 0 and upperLimit
	 */
	public static int getRandomInt(int upperLimit) {
		return (int) (Math.random() * (upperLimit + 1));
	}

	/**
	 * Returns a random Number between lowerLimit and upperLimit
	 * 
	 * @param lowerLimit
	 * @param upperLimit
	 * @return a random Number between lowerLimit and upperLimit, ERROR if
	 *         something went wrong
	 */
	public static int getRandomIntWithinLimits(int lowerLimit, int upperLimit) {
		if (lowerLimit > upperLimit) {
			return getError();
		}
		long range = (long) upperLimit - (long) lowerLimit + 1;
		long fraction = (long) (range * Math.random());

		return (int) (fraction + lowerLimit);
	}

	public static boolean isSearchActiveForPartner() {
		return searchActiveForPartner;
	}

	/************************************************/
	// Getter-Functions
	/************************************************/

	public static int getMetabolism() {
		return metabolism;
	}

	public static int getSugarConsumingRatio() {
		return sugarConsumingRatio;
	}

	public static int getGetSugarStep() {
		return getSugarStep;
	}

	public static int getSugarMiningRatio() {
		return sugarMiningRatio;
	}

	public static int getVisionRadius() {
		return visionRadius;
	}

	public static int getDivideFactorHasEnoughSugar() {
		return divideFactorHasEnoughSugar;
	}

	public static int getSexLimit() {
		return sexLimit;
	}

	public static int getMaxAgeMax() {
		return maxAgeMax;
	}

	public static int getMaxAgeMin() {
		return maxAgeMin;
	}

	public static int getError() {
		return ERROR;
	}

	public static int getMinWealth1stGen() {
		return minWealth1stGen;
	}

	public static int getMaxWealth1stGen() {
		return maxWealth1stGen;
	}

	public static int getMinFertilityAgeMin() {
		return minFertilityAgeMin;
	}

	public static int getMinFertilityAgeMax() {
		return minFertilityAgeMax;
	}

	public static int getMinFertilityAgeMale() {
		return minFertilityAgeMale;
	}

	public static int getMaxFertilityAgeMale() {
		return maxFertilityAgeMale;
	}

	public static int getMinFertilityAgeFemale() {
		return minFertilityAgeFemale;
	}

	public static int getMaxFertilityAgeFemale() {
		return maxFertilityAgeFemale;
	}
}
