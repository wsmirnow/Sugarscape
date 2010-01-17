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

/**
 * Helper
 * 
 * @author Denis Meyer
 */
public class SCHelper {

	/************************************************/
	// Variables
	/************************************************/

	/**
	 * Extended Von Neumann Neighborhood or every neighboor
	 */
	private boolean extendedVonNeumannNeighborhood = true;

	/**
	 * Flag for active Search for a Partner
	 */
	private boolean searchActiveForPartner = true;

	/**
	 * Field of Vision Radius of extended Moore Neighbourhood at Reproduce
	 */
	private int visionRadiusReproduce = 2;

	/**
	 * Field of Vision Radius of extended Moore Neighbourhood
	 */
	private int visionRadius = 4;

	/**
	 * Expansion Ratio in between [0, 1]
	 */
	private double expansionRatio = 0.2; // a

	/**
	 * Metabolism (Time Steps / sugarConsumingRatio Sugar)
	 */
	private int metabolism = 2;

	/**
	 * Displays since what amount of Sugar the Bug (who is searching for a
	 * Partner) must move
	 */
	private int searchActiveForPartnerSugarMiningLimit = 10;

	/**
	 * Error Representation
	 */
	private static final int ERROR = Integer.MAX_VALUE;

	/**
	 * Max. Amount of Sugar in 1 SugarAgent
	 */
	private int maxAmountOfSugarInSugarAgent = 20; // c

	/**
	 * The Movement Speed (per Timestep)
	 */
	private int movementSpeed = 1;

	/**
	 * Sugar Consuming Ratio
	 */
	private int sugarConsumingRatio = 1;

	/**
	 * Step for getting sugarMiningRatio Sugar
	 */
	private int getSugarStep = 2;

	/**
	 * Sugar Mining Ratio
	 */
	private int sugarMiningRatio = 4;

	/**
	 * Adds a Divisor in the Function hasEnoughSugar() -> return
	 * (getCurrWealth() >= (getInitialSugar() / divideFactorHasEnoughSugar));
	 */
	private int divideFactorHasEnoughSugar = 1;

	/**
	 * getRandomSex: random >> sexLimit -> bigger Probability for getting female
	 */
	private int sexLimit = 50;

	/**
	 * Min. min. Fertility Age
	 */
	private int minFertilityAgeMin = 12;

	/**
	 * Max. min. Fertility Age
	 */
	private int minFertilityAgeMax = 15;

	/**
	 * Min. max. Fertility Age for female
	 */
	private int minFertilityAgeFemale = 40;

	/**
	 * Min. max. Fertility Age for male
	 */
	private int minFertilityAgeMale = 50;

	/**
	 * Max. Fertility Age for female
	 */
	private int maxFertilityAgeFemale = 50;

	/**
	 * Max. Fertility Age for male
	 */
	private int maxFertilityAgeMale = 60;

	/**
	 * Min. Wealth for the 1st Generation
	 */
	private int minWealth1stGen = 50;

	/**
	 * Max. Wealth for the 1st Generation
	 */
	private int maxWealth1stGen = 100;

	/**
	 * Min. max. Age
	 */
	private int maxAgeMin = 65;

	/**
	 * Max. max. Age
	 */
	private int maxAgeMax = 80;

	/************************************************/
	// Functions
	/************************************************/

	/**
	 * Returns a random Integer between 0 and upperLimit
	 * 
	 * @param upperLimit
	 * @return a random Integer between 0 and upperLimit
	 */
	public int getRandomInt(int upperLimit) {
		if (upperLimit > 0) {
			return (int) (Math.random() * (upperLimit + 1));
		} else {
			return getError();
		}
	}

	/**
	 * Returns a random Number between lowerLimit and upperLimit
	 * 
	 * @param lowerLimit
	 * @param upperLimit
	 * @return a random Number between lowerLimit and upperLimit, ERROR if
	 *         something went wrong
	 */
	public int getRandomIntWithinLimits(int lowerLimit, int upperLimit) {
		if (lowerLimit > upperLimit) {
			return getError();
		}
		long range = (long) upperLimit - (long) lowerLimit + 1;
		long fraction = (long) (range * Math.random());

		return (int) (fraction + lowerLimit);
	}

	/************************************************/
	// Setter-Functions
	/************************************************/

	/**
	 * Setter
	 * 
	 * @param maxAmountOfSugarInSugarAgent
	 */
	public void setMaxAmountOfSugarInSugarAgent(int maxAmountOfSugarInSugarAgent) {
		if (maxAmountOfSugarInSugarAgent > 0) {
			this.maxAmountOfSugarInSugarAgent = maxAmountOfSugarInSugarAgent;
		}
	}

	/************************************************/
	// Getter-Functions
	/************************************************/

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public double getExpansionRatio() {
		return expansionRatio;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public double getMovementSpeed() {
		return movementSpeed;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public boolean searchActiveForPartner() {
		return searchActiveForPartner;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int searchActiveForPartnerSugarMiningLimit() {
		return searchActiveForPartnerSugarMiningLimit;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getMetabolism() {
		return metabolism;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getSugarConsumingRatio() {
		return sugarConsumingRatio;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getGetSugarStep() {
		return getSugarStep;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getSugarMiningRatio() {
		return sugarMiningRatio;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getVisionRadius() {
		return visionRadius;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getVisionRadiusReproduce() {
		return visionRadiusReproduce;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getDivideFactorHasEnoughSugar() {
		return divideFactorHasEnoughSugar;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getSexLimit() {
		return sexLimit;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getMaxAgeMax() {
		return maxAgeMax;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getMaxAgeMin() {
		return maxAgeMin;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getError() {
		return ERROR;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public boolean getExtendedVonNeumannNeighborhood() {
		return extendedVonNeumannNeighborhood;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getMinWealth1stGen() {
		return minWealth1stGen;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getMaxWealth1stGen() {
		return maxWealth1stGen;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getMinFertilityAgeMin() {
		return minFertilityAgeMin;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getMinFertilityAgeMax() {
		return minFertilityAgeMax;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getMinFertilityAgeMale() {
		return minFertilityAgeMale;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getMaxFertilityAgeMale() {
		return maxFertilityAgeMale;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getMinFertilityAgeFemale() {
		return minFertilityAgeFemale;
	}

	/**
	 * Getter
	 * 
	 * @return var
	 */
	public int getMaxFertilityAgeFemale() {
		return maxFertilityAgeFemale;
	}

	/**
	 * Setter
	 * 
	 * @return var
	 */
	public int getMaxAmountOfSugarInSugarAgent() {
		return maxAmountOfSugarInSugarAgent;
	}
}
