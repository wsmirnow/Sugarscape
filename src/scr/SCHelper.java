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
	 * Error Representation
	 */
	public static final int ERROR = Integer.MAX_VALUE;
	
	/**
	 * Extended Von Neumann Neighborhood or every neighboor
	 */
	public boolean extendedVonNeumannNeighborhood = false;

	/**
	 * Max. Amount of Sugar in 1 SugarAgent
	 */
	public int maxAmountOfSugarInSugarAgent = 20; // c

	/**
	 * Expansion Ratio in between [0, 1]
	 */
	public double expansionRatio = 0.1; // a

	/**
	 * Mode for "Aufgabe" No. 2
	 */
	public boolean searchActiveForPartner = true;

	/**
	 * The Movement Speed (per Timestep)
	 */
	public int movementSpeed = 1;

	/**
	 * Metabolism (Time Steps / sugarConsumingRatio Sugar)
	 */
	public int metabolism = 2;

	/**
	 * Sugar Consuming Ratio
	 */
	public int sugarConsumingRatio = 1;

	/**
	 * Step for getting sugarMiningRatio Sugar
	 */
	public int getSugarStep = 2;

	/**
	 * Sugar Mining Ratio
	 */
	public int sugarMiningRatio = 4;

	/**
	 * Field of Vision Radius of extended Moore Neighbourhood
	 */
	public int visionRadius = 4;

	/**
	 * Field of Vision Radius of extended Moore Neighbourhood at Reproduce
	 * TODO hochsetzen, um mehr Generationen zu bekommen
	 */
	public int visionRadiusReproduce = 4;

	/**
	 * Adds a Divisor in the Function hasEnoughSugar() -> return
	 * (getCurrWealth() >= (getInitialSugar() / divideFactorHasEnoughSugar));
	 */
	public int divideFactorHasEnoughSugar = 1;

	/**
	 * getRandomSex: random >> sexLimit -> bigger Probability for getting female
	 */
	public int sexLimit = 50;

	/**
	 * Min. min. Fertility Age
	 */
	public int minFertilityAgeMin = 12;

	/**
	 * Max. min. Fertility Age
	 */
	public int minFertilityAgeMax = 15;

	/**
	 * Min. max. Fertility Age for female
	 */
	public int minFertilityAgeFemale = 40;

	/**
	 * Min. max. Fertility Age for male
	 */
	public int minFertilityAgeMale = 50;

	/**
	 * Max. Fertility Age for female
	 */
	public int maxFertilityAgeFemale = 50;

	/**
	 * Max. Fertility Age for male
	 */
	public int maxFertilityAgeMale = 60;

	/**
	 * Min. Wealth for the 1st Generation
	 */
	public int minWealth1stGen = 50;

	/**
	 * Max. Wealth for the 1st Generation
	 */
	public int maxWealth1stGen = 100;

	/**
	 * Min. max. Age
	 */
	public int maxAgeMin = 65;

	/**
	 * Max. max. Age
	 */
	public int maxAgeMax = 80;

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
	public boolean searchActiveForPartner() {
		return searchActiveForPartner;
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
	public boolean extendedVonNeumannNeighborhood() {
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
