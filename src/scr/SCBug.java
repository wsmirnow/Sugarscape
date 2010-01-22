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
 * File: scr/SCBug.java
 */
package scr;

import java.util.Vector;
import eawag.grid.Bug;
import eawag.grid.Depiction;

/**
 * Bug
 * 
 * @author Denis Meyer
 */
public class SCBug extends Bug {

	private static final SCHelper helper = new SCHelper();
	private static int deathCount = 0;
	private static int deathAge = 0;
	private static int d_poor = 0;
	private static int d_AgePoor = 0;
	private static int d_rich = 0;
	private static int d_AgeRich = 0;

	/************************************************/
	// Private Variables
	/************************************************/

	// Current Metabolism
	private int currMB;

	// Current Step for consuming 1 Sugar
	private int currGetSugarStep;

	// Vector of Descendants
	private Vector<SCBug> descendants;

	// Vector of Parents
	private Vector<SCBug> parents;

	// The Number of Descendants
	private int numberOfDescendants;

	// Generation [1, n]
	private int generation;

	// Initial Sugar (Birth)
	private int initialSugar;

	// Current Age in TimeSteps
	private int currAge;

	// Max. Age
	private int maxAge;

	// Current Wealth in Amount of Sugar
	private int currWealth;

	// Sex, true == male - false == female
	private boolean sex;

	// Min. Fertility Age
	private int fertilityAgeMin;

	// Max. Fertility Age
	private int fertilityAgeMax;

	// Metabolism
	private int metabolism;

	// Vision Radius
	private int visionRadius;

	// Vision Radius Reproduce
	private int visionRadiusReproduce;

	// Flag if 1st Generation
	static boolean init = true;

	/************************************************/
	// Constructors
	/************************************************/

	/**
	 * Default Constructor (only for the 1st Generation)
	 */
	public SCBug() {
		if (helper.setAgeOf1stGenerationRandomly()) {
			setValues(0, 0, getRandomSex(), 1, null, null);
		} else {
			setValues(0, 0, getRandomSex(), 1, null, null);
		}
	}

	/**
	 * Constructor
	 * 
	 * @param currAge
	 *            Current Age
	 * @param currWealth
	 *            Current Wealth
	 * @param sex
	 *            Sex
	 * @param generation
	 *            Generation
	 * @param parent1
	 *            Parent 1
	 * @param parent2
	 *            Parent 2
	 */
	public SCBug(int currAge, int currWealth, boolean sex, int generation,
			SCBug parent1, SCBug parent2) {
		setValues(currAge, currWealth, sex, generation, parent1, parent2);
	}

	/************************************************/
	// Other Functions
	/************************************************/

	/**
	 * Action-Function
	 */
	public void action() {
		if (init) {
			if (this.getGrid() instanceof SCGrid) {
				SCGrid grid = (SCGrid) this.getGrid();
				for (int x = 0; x < grid._xsize; x++) {
					for (int y = 0; y < grid._ysize; y++) {
						if (grid.getBug(x, y, 1) instanceof SCBug) {
							SCBug bug = (SCBug) grid.getBug(x, y, 1);
							if (bug.getGeneration() == 1) {
								bug.currAge = helper
										.getRandomIntWithinLimits(
												helper
														.getRandomAgeOf1stGenerationLowerLimit(),
												helper
														.getRandomAgeOf1stGenerationUpperLimit());
							}
						}
					}
				}
			}
			init = false;
		}
		// Too old to live?
		if ((this.currAge > this.maxAge) || (this.currWealth <= 0)) {
			// This Bug has to die, it is too old or has no Sugar!
			die();
			// Keep on living
		} else {
			this.currAge++;

			// Get Sugar from the SCSugarBug every getSugarStep Timesteps
			if (this.currGetSugarStep >= helper.getGetSugarStep()) {
				if (this.getGrid() instanceof SCGrid) {
					SCGrid grid = (SCGrid) this.getGrid();
					if (grid.getBug(this._x, this._y, 0) instanceof SCSugarBug) {
						this.addSugar(((SCSugarBug) grid.getBug(this._x,
								this._y, 0)).getSugar(helper
								.getSugarMiningRatio()));
					}
				}
				this.currGetSugarStep = 0;
			} else {
				this.currGetSugarStep++;
			}

			SCGrid grid = (SCGrid) getGrid();
			if (grid != null)
				grid.addAvarageWealth(getCurrWealth());

			// Reproduce if fertile
			if (this.isFertile() && this.hasEnoughSugar()) {
				SCBug descendant = reproduce();
				if ((descendant != null) && (this.descendants != null)) {
					this.descendants.add(descendant);
					numberOfDescendants++;

					// enrich rich Bugs
					if (helper.advantagesForTheRich()) {
						SCGrid myGrid = (SCGrid) getGrid();
						if (myGrid != null) {
							int avarageWealth = myGrid.getAvarageWealth();
							avarageWealth = avarageWealth > 0 ? avarageWealth : getCurrWealth();
							double factor = ((double)getCurrWealth() * 2) / (double)avarageWealth;
							// if factor > 1 => increase matabolism, vision radius and vision radius reproduce
							descendant.setMetabolismByFactor(factor);
							descendant.setVisionRadiusByFactor(factor);
							descendant.setVisionRadiusReproduceByFactor(factor);
						}
					}
				}
			}

			// Consume Sugar every metabolism Timesteps
			if (this.currMB >= this.getMetabolism()) {
				this.removeSugar(helper.getSugarConsumingRatio());
				if (this.currWealth < 0) {
					this.currWealth = 0;
				}
				this.currMB = 0;
			} else {
				this.currMB++;
			}

			// Move around
			move();
		}
	}

	/**
	 * Show Bug at the Grid by sex
	 */
	public void updateDepiction() {
		String depictName = "DepictionBug" + (getSex() ? "Female" : "Male");
		super.setDepiction((Depiction) (getBase().findBySerno(depictName)));
	}

	/**
	 * Moves the Agent: If no more Sugar at the current Place Move to the Place
	 * with the highest Amount of Sugar OR Search active for a Partner
	 */
	private void move() {
		if (this.getGrid() instanceof SCGrid) {
			SCGrid grid = (SCGrid) this.getGrid();
			if (grid.getBug(this._x, this._y, 0) instanceof SCSugarBug) {
				boolean found = false;
				// Search Active for a Partner
				if (this.isFertile()
						&& helper.searchActiveForPartner()
						&& (((SCSugarBug) grid.getBug(this._x, this._y, 0))
								.getCurrentAmountOfSugar() <= helper
								.searchActiveForPartnerSugarMiningLimit())) {
					SCBug bug = null;
					Vector<SCBug> neighbours = new Vector<SCBug>();
					neighbours = this.getNeighbours();
					// If there are Neighbors
					if ((neighbours != null) && !neighbours.isEmpty()) {
						for (int i = 0; (i < neighbours.size()) && !found; i++) {
							SCBug neighbor = neighbours.get(i);
							// If a Neighbor fits as a Partner
							if ((neighbor.getSex() != this.getSex())
									&& neighbor.isFertile()
									&& neighbor.hasEnoughSugar()) {
								// Get free Places around the Partner
								Vector<SCBug> freePlaces = new Vector<SCBug>();
								freePlaces = neighbours.get(i)
										.getFreePlacesAround();
								// If free Places around the potential
								// Partner exist
								if ((freePlaces != null)
										&& !freePlaces.isEmpty()) {
									// Get closest Point to the potential
									// Partner
									double erg = Integer.MAX_VALUE;
									for (int j = 0; j < freePlaces.size(); j++) {
										SCBug tmp = freePlaces.get(j);
										if ((tmp != null) && isInSight(tmp)) {
											double tmpErg = bugDistance(
													neighbor, tmp)
													+ bugDistance(tmp, this);
											if (tmpErg < erg) {
												bug = tmp;
												erg = tmpErg;
											}
										}
									}
									// If the free Place is in Sight of this
									// Bug
									if ((bug != null) && isInSight(bug)) {
										// Move this Bug to the found free Place
										this.moveBug(bug._x, bug._y, 1);
										found = true;
									}
								}
							}
						}
					}
				}
				// If no Partner with free Space around found or
				// "Search Active for a Partner" is not active
				if ((!found && helper.searchActiveForPartner() && isFertile())
						|| (!found && (((SCSugarBug) grid.getBug(this._x,
								this._y, 0)).getCurrentAmountOfSugar() <= 0))) {
					SCSugarBug sugarBug = null;
					// Get Place(s) with the highest Amount of Sugar
					Vector<SCSugarBug> vec = new Vector<SCSugarBug>();
					vec = this.getHighestAmountOfSugar();
					if ((vec != null) && !vec.isEmpty()) {
						// Move randomly to one of the Places with the highest
						// amount of Sugar
						int rand = helper.getRandomIntWithinLimits(0, vec
								.size() - 1);
						sugarBug = vec.get(rand);
						if (sugarBug != null) {
							this.moveBug(sugarBug._x, sugarBug._y, 1);
						}
					}
				}
			}
		}
	}

	/************************************************/
	// Boolean Functions
	/************************************************/

	/**
	 * Returns if SCBug is fertile
	 * 
	 * @return if SCBug is fertile
	 */
	public boolean isFertile() {
		int currAge = getCurrAge();
		return ((currAge >= getFertilityAgeMin()) && (currAge <= getFertilityAgeMax()));
	}

	/**
	 * Returns if the SCBug has enough Money (to reproduce)
	 * 
	 * @return if the SCBug has enough Money (to reproduce)
	 */
	public boolean hasEnoughSugar() {
		int divideFactor = helper.getDivideFactorHasEnoughSugar();
		if (divideFactor > 0) {
			return (getCurrWealth() >= (getInitialSugar() / divideFactor));
		}
		return (getCurrWealth() >= getInitialSugar());
	}

	/**
	 * Returns if Bug b is in the VisionRadius of this Bug
	 * 
	 * @param b
	 *            Bug
	 * @return true if Bug b is in the VisionRadius of this Bug, false else
	 */
	private boolean isInSight(SCBug b) {
		boolean x = (b._x > (this._x - this.getVisionRadius()))
				&& (b._x < (this._x + this.getVisionRadius()));
		boolean y = (b._y > (this._y - this.getVisionRadius()))
				&& (b._y < (this._y + this.getVisionRadius()));
		return x && y;
	}

	/************************************************/
	// Getter-Functions
	/************************************************/
	
	/**
	 * Returns a Vector with the Coordinates of the Von-Neumann-Neighborhood
	 * around this Bug
	 * 
	 * @return a Vector with the Coordinates of the Von-Neumann-Neighborhood
	 *         around this Bug
	 */
	public Vector<int[]> getExtendedVonNeumannCoordinatesAround() {
		Vector<int[]> vec = new Vector<int[]>();
		if (this.getGrid() instanceof SCGrid) {
			SCGrid grid = (SCGrid) this.getGrid();
			// down + top
			for (int i = (this._x - this.getVisionRadius()); i <= (this._x + this
					.getVisionRadius()); i++) {
				if ((i != this._x) && (i < grid.getXSize())) {
					int arr[] = new int[2];
					arr[0] = i;
					arr[1] = this._y;
					vec.add(arr);
				}
			}
			// left + right
			for (int i = (this._y - this.getVisionRadius()); i <= (this._y + this
					.getVisionRadius()); i++) {
				if ((i != this._y) && (i < grid.getYSize())) {
					int arr[] = new int[2];
					arr[0] = this._x;
					arr[1] = i;
					vec.add(arr);
				}
			}
		}

		return vec;
	}

	/**
	 * Returns the Vision Radius; Richer->bigger Vision Radius
	 * 
	 * @return the Vision Radius
	 */
	public int getVisionRadiusReproduce() {
		return this.visionRadiusReproduce;
	}

	/**
	 * Returns the Vision Radius; Richer->bigger Vision Radius
	 * 
	 * @return the Vision Radius
	 */
	public int getVisionRadius() {
		return this.visionRadius;
	}

	/**
	 * Returns the Metabolism; Richer->less sugar consume
	 * 
	 * @return the Metabolism
	 */
	public int getMetabolism() {
		return this.metabolism;
	}

	/**
	 * Returns the initial Sugar (Birth)
	 * 
	 * @return the initial Sugar
	 */
	public int getInitialSugar() {
		return this.initialSugar;
	}

	/**
	 * Returns the Parents
	 * 
	 * @return the Parents
	 */
	public Vector<SCBug> getParents() {
		return this.parents;
	}

	/**
	 * Returns the Generation
	 * 
	 * @return Generation
	 */
	public int getGeneration() {
		return this.generation;
	}

	/**
	 * Returns all (living or dead) Descendants
	 * 
	 * @return all (living or dead) Descendants
	 */
	public int getAllDescendants() {
		return this.numberOfDescendants;
	}

	/**
	 * Returns all living Descendants
	 * 
	 * @return all living Descendants
	 */
	public int getAllLivingDescendants() {
		if (this.descendants != null) {
			return this.descendants.size();
		}
		return 0;
	}

	/**
	 * Returns the current Age
	 * 
	 * @return the current Age
	 */
	public int getCurrAge() {
		return this.currAge;
	}

	/**
	 * Returns the current Wealth
	 * 
	 * @return the current Wealth
	 */
	public int getCurrWealth() {
		return this.currWealth;
	}

	/**
	 * Returns the Sex
	 * 
	 * @return the Sex
	 */
	public boolean getSex() {
		return this.sex;
	}

	/**
	 * Returns the min. Fertility Age
	 * 
	 * @return the min. Fertility Age
	 */
	public int getFertilityAgeMin() {
		return this.fertilityAgeMin;
	}

	/**
	 * Returns the max. Fertility Age
	 * 
	 * @return the max. Fertility Age
	 */
	public int getFertilityAgeMax() {
		return this.fertilityAgeMax;
	}

	/**
	 * Returns the max. Age
	 * 
	 * @return the max. Age
	 */
	public int getMaxAge() {
		return this.maxAge;
	}

	/**
	 * Returns the number of died Agents
	 * 
	 * @return number of died Agents
	 */
	public int getDeadAgents() {
		return deathCount;
	}

	/**
	 * Returns the Age of Death
	 * 
	 * @return the Age of Death
	 */
	public int getAgeOfDeath() {
		return deathAge;
	}
	
	/**
	 * Returns number of poor dead Agents
	 * 
	 * @return d_poor
	 */
	public int getD_Poor(){
		return d_poor;
	}
	
	/**
	 * Returns Age of death from poor agents
	 * 
	 * @return d_AgePoor
	 */
	public int getD_AgePoor(){
		return d_AgePoor;
	}
	
	/**
	 * Returns number of rich dead Agents
	 * 
	 * @return d_rich
	 */
	public int getD_Rich(){
		return d_rich;
	}
	
	/**
	 * Returns Age of death from rich agents
	 * 
	 * @return d_AgeRich
	 */
	public int getD_AgeRich(){
		return d_AgeRich;
	}

	/**
	 * Returns free Places around this Bug
	 * 
	 * @return free Places around this Bug
	 */
	public Vector<SCBug> getFreePlacesAround() {
		Vector<SCBug> vec = new Vector<SCBug>();
		if (this.getGrid() instanceof SCGrid) {
			SCGrid grid = (SCGrid) this.getGrid();
			if (helper.getExtendedVonNeumannNeighborhood()) {
				Vector<int[]> coords = new Vector<int[]>();
				coords = getExtendedVonNeumannCoordinatesAround();
				if ((coords != null) && !coords.isEmpty()) {
					for (int i = 0; i < coords.size(); i++) {
						int xCoord = coords.get(i)[0];
						int yCoord = coords.get(i)[1];
						if ((xCoord < grid.getXSize())
								&& (yCoord < grid.getYSize())) {
							if (grid.getBug(xCoord, yCoord, 1) == null) {
								SCBug newBug = new SCBug();
								newBug._x = xCoord;
								newBug._y = yCoord;
								vec.add(newBug);
							}
						}
					}
				}
			} else {
				for (int i = (this._x - this.getVisionRadius()); i < (this._x + this
						.getVisionRadius()); i++) {
					for (int j = (this._y - this.getVisionRadius()); j < (this._y + this
							.getVisionRadius()); j++) {
						if ((i < grid.getXSize()) && (j < grid.getYSize())) {
							if (grid.getBug(i, j, 1) == null) {
								SCBug newBug = new SCBug();
								newBug._x = i;
								newBug._y = j;
								vec.add(newBug);
							}
						}
					}
				}
			}
		}
		return vec;
	}

	/**
	 * Returns the SCSugarBug with the highest Amount of Sugar within a Vision
	 * Radius
	 * 
	 * @return a Vector of SCSugarBugs with the highest Amount of Sugar within
	 *         the Vision Radius
	 */
	private Vector<SCSugarBug> getHighestAmountOfSugar() {
		Vector<SCSugarBug> vec = new Vector<SCSugarBug>();
		int currHighestValue = -1;
		if (this.getGrid() instanceof SCGrid) {
			SCGrid grid = (SCGrid) this.getGrid();
			if (helper.getExtendedVonNeumannNeighborhood()) {
				Vector<int[]> coords = new Vector<int[]>();
				coords = getExtendedVonNeumannCoordinatesAround();
				if ((coords != null) && !coords.isEmpty()) {
					for (int i = 0; i < coords.size(); i++) {
						int xCoord = coords.get(i)[0];
						int yCoord = coords.get(i)[1];
						if ((xCoord < grid.getXSize())
								&& (yCoord < grid.getYSize())) {
							if (grid.getBug(xCoord, yCoord, 0) instanceof SCSugarBug) {
								SCSugarBug tmp = (SCSugarBug) grid.getBug(
										xCoord, yCoord, 0);
								int tmpVal = tmp.getCurrentAmountOfSugar();
								if (tmpVal > currHighestValue) {
									currHighestValue = tmpVal;
									vec.clear();
									vec.add(tmp);
								} else if (tmpVal == currHighestValue) {
									vec.add(tmp);
								}
							}
						}
					}
				}
			} else {
				for (int i = (this._x - this.getVisionRadius()); i < (this._x + this
						.getVisionRadius()); i++) {
					for (int j = (this._y - this.getVisionRadius()); j < (this._y + this
							.getVisionRadius()); j++) {
						if ((i < grid.getXSize()) && (j < grid.getYSize())) {
							if (grid.getBug(i, j, 0) instanceof SCSugarBug) {
								SCSugarBug tmp = (SCSugarBug) grid.getBug(i, j,
										0);
								int tmpVal = tmp.getCurrentAmountOfSugar();
								if (tmpVal > currHighestValue) {
									currHighestValue = tmpVal;
									vec.clear();
									vec.add(tmp);
								} else if (tmpVal == currHighestValue) {
									vec.add(tmp);
								}
							}
						}
					}
				}
			}
		}

		return vec;
	}

	/**
	 * Returns the Neighbours of this Bug
	 * 
	 * @return the Neighbours of this Bug
	 */
	private Vector<SCBug> getNeighbours() {
		Vector<SCBug> vec = new Vector<SCBug>();
		if (this.getGrid() instanceof SCGrid) {
			SCGrid grid = (SCGrid) this.getGrid();
			if (helper.getExtendedVonNeumannNeighborhood()) {
				Vector<int[]> coords = new Vector<int[]>();
				coords = getExtendedVonNeumannCoordinatesAround();
				if ((coords != null) && !coords.isEmpty()) {
					for (int i = 0; i < coords.size(); i++) {
						int xCoord = coords.get(i)[0];
						int yCoord = coords.get(i)[1];
						if ((xCoord < grid.getXSize())
								&& (yCoord < grid.getYSize())) {
							if (grid.getBug(xCoord, yCoord, 1) instanceof SCBug) {
								vec.add((SCBug) grid.getBug(xCoord, yCoord, 1));
							}
						}
					}
				}
			} else {
				for (int i = (this._x - this.getVisionRadius()); i < (this._x + this
						.getVisionRadius()); i++) {
					for (int j = (this._y - this.getVisionRadius()); j < (this._y + this
							.getVisionRadius()); j++) {
						if ((i < grid.getXSize()) && (j < grid.getYSize())) {
							if (grid.getBug(i, j, 1) instanceof SCBug) {
								vec.add((SCBug) grid.getBug(i, j, 1));
							}
						}
					}
				}
			}
		}
		return vec;
	}

	/**
	 * Returns a Sex
	 * 
	 * @return a Sex
	 */
	private boolean getRandomSex() {
		return (helper.getRandomInt(100) >= helper.getSexLimit());
	}

	/**
	 * Returns a random max. Age
	 * 
	 * @return a random max. Age
	 */
	private int getRandomMaxAge() {
		int tmp = helper.getRandomIntWithinLimits(helper.getMaxAgeMin(), helper
				.getMaxAgeMax());
		if (tmp != helper.getError()) {
			return tmp;
		}
		return helper.getMaxAgeMin();
	}

	/**
	 * Returns a random 1st Generation Wealth
	 * 
	 * @return a random 1st Generation Wealth
	 */
	private int getRandom1stGenWealth() {
		int tmp = helper.getRandomIntWithinLimits(helper.getMinWealth1stGen(),
				helper.getMaxWealth1stGen());
		if (tmp != helper.getError()) {
			return tmp;
		}
		return helper.getMaxWealth1stGen();
	}

	/**
	 * Returns a min. Fertility Age
	 * 
	 * @return a min. Fertility Age if successful, minFertilityAge else
	 */
	private int getRandomFertilityAgeMin() {
		int tmp = helper.getRandomIntWithinLimits(helper
				.getMinFertilityAgeMin(), helper.getMinFertilityAgeMax());
		if (tmp != helper.getError()) {
			return tmp;
		}
		return helper.getMinFertilityAgeMax();
	}

	/**
	 * Returns a max. Fertility Age
	 * 
	 * @return a max. Fertility Age if successful, maxFertilityAge else
	 */
	private int getRandomFertilityAgeMax() {
		int tmp = helper.getError();
		// If male
		if (sex) {
			tmp = helper.getRandomIntWithinLimits(helper
					.getMinFertilityAgeMale(), helper.getMaxFertilityAgeMale());
			// If female
		} else {
			tmp = helper.getRandomIntWithinLimits(helper
					.getMinFertilityAgeFemale(), helper
					.getMaxFertilityAgeFemale());
		}
		// Normally
		if (tmp != helper.getError()) {
			return tmp;
		}
		if (sex) {
			return helper.getMaxFertilityAgeMale();
		}
		return helper.getMaxFertilityAgeFemale();
	}

	/************************************************/
	// Setter-Functions
	/************************************************/

	/**
	 * Adds an Amount of Sugar
	 * 
	 * @param amount
	 *            Amount of Sugar to add
	 */
	public void addSugar(int amount) {
		if (amount > 0) {
			this.currWealth += amount;
		}
	}

	/**
	 * Removes an Amount of Sugar
	 * 
	 * @param amount
	 *            Amount of Sugar to remove
	 */
	public void removeSugar(int amount) {
		if (amount > 0) {
			if ((this.currWealth -= amount) < 0) {
				this.currWealth = 0;
			}
		}
	}

	/**
	 * Sets the Bugs Sex by Bugs added to the Grid via the GUI
	 * 
	 * @param depict
	 *            Depiction
	 */
	public void setDepiction(Depiction depict) {

		if (!depict.serno.startsWith("DepictionBug")) {
			return;
		}

		if (depict.serno.endsWith("Female")) {
			sex = false;
		} else if (depict.serno.endsWith("Male")) {
			sex = true;
		} else {
			sex = getRandomSex();
		}

		super.setDepiction(depict);
	}

	/**
	 * Sets a new Bug around this Bug
	 * 
	 * @param initialSugar
	 *            Initial Sugar
	 * @param generation
	 *            Generation
	 * @param parent1
	 *            Parent 1
	 * @param parent2
	 *            Parent 2
	 * 
	 * @return the set SCBug if successfully or null
	 */
	public SCBug setDescendantToFreeCoordinatesAround(int initialSugar,
			int generation, SCBug parent1, SCBug parent2) {
		SCBug setBug = null;
		if (this.getGrid() instanceof SCGrid) {
			SCGrid grid = (SCGrid) this.getGrid();
			SCBug b = null;
			Vector<SCBug> bugs = new Vector<SCBug>();
			if (helper.getExtendedVonNeumannNeighborhood()) {
				Vector<int[]> coords = new Vector<int[]>();
				coords = getExtendedVonNeumannCoordinatesAround();
				if ((coords != null) && !coords.isEmpty()) {
					for (int i = 0; i < coords.size(); i++) {
						int xCoord = coords.get(i)[0];
						int yCoord = coords.get(i)[1];
						if ((xCoord < grid.getXSize())
								&& (yCoord < grid.getYSize())) {
							if ((grid.getBug(xCoord, yCoord, 1) == null)) {
								b = new SCBug(0, initialSugar, getRandomSex(),
										generation, parent1, parent2);
								b._x = xCoord;
								b._y = yCoord;
								bugs.add(b);
							}
						}
					}
				}
			} else {
				for (int i = (this._x - this.getVisionRadius()); i < (this._x + this
						.getVisionRadius()); i++) {
					for (int j = (this._y - this.getVisionRadius()); j < (this._y + this
							.getVisionRadius()); j++) {
						if ((i < grid.getXSize()) && (j < grid.getYSize())) {
							if ((grid.getBug(i, j, 1) == null)) {
								b = new SCBug(0, initialSugar, getRandomSex(),
										generation, parent1, parent2);
								b._x = i;
								b._y = j;
								bugs.add(b);
							}
						}
					}
				}
			}
			if ((bugs != null) && !bugs.isEmpty()) {
				boolean set = false;
				for (int i = 0; (i < bugs.size()) && !set; i++) {
					int rand = helper.getRandomIntWithinLimits(0,
							bugs.size() - 1);
					setBug = bugs.get(rand);
					if (parent1.isInSight(setBug) || parent2.isInSight(setBug)) {
						set = true;
						setBug.moveBug(setBug._x, setBug._y, 1);
						setBug.join(grid);
						setBug.updateDepiction();
						return setBug;
					} else {
						bugs.remove(rand);
					}
				}
			}
		}

		return setBug;
	}

	/**
	 * Sets the Values
	 * 
	 * @param currAge
	 * @param currWealth
	 * @param sex
	 */
	private void setValues(int currAge, int currWealth, boolean sex,
			int generation, SCBug parent1, SCBug parent2) {
		initialize();
		this.generation = generation;
		if (currAge >= 0) {
			this.currAge = currAge;
		} else {
			this.currAge = 0;
		}
		if (this.generation == 1) {
			this.currWealth = getRandom1stGenWealth();
		} else {
			if (currWealth >= 0) {
				this.currWealth = currWealth;
			} else {
				this.currWealth = 0;
			}
		}
		this.parents.add(parent1);
		this.parents.add(parent2);
		this.visionRadiusReproduce = this.initialSugar = this.currWealth;
		this.sex = sex;
		this.fertilityAgeMin = getRandomFertilityAgeMin();
		this.fertilityAgeMax = getRandomFertilityAgeMax();
		this.maxAge = getRandomMaxAge();
		// Set dependent Values
		this.visionRadius = helper.getVisionRadius();
		this.visionRadiusReproduce = helper.getVisionRadiusReproduce();
		this.metabolism = helper.getMetabolism();
	}

	/************************************************/
	// Helper-Functions
	/************************************************/

	/**
	 * Returns the Distance between 2 Bugs
	 * 
	 * @param b1
	 * @param b2
	 * @return the Distance between 2 Bugs
	 */
	private double bugDistance(SCBug b1, SCBug b2) {
		int dx = b1._x - b2._x;
		int dy = b1._y - b2._y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Sets the initial Values
	 */
	private void initialize() {
		this.currMB = 0;
		this.currGetSugarStep = 0;
		this.descendants = new Vector<SCBug>();
		this.parents = new Vector<SCBug>(2);
		this.numberOfDescendants = 0;
	}

	/**
	 * Moves the new Bug (Helper Functions - reproduce)
	 * 
	 * @param i
	 *            x-Coordinate
	 * @param j
	 *            y-Coordinate
	 * @return created Bug if successfully, null else
	 */
	private SCBug moveNewBug(int i, int j) {
		SCBug freePlaceBug = null;
		if (this.getGrid() instanceof SCGrid) {
			SCGrid grid = (SCGrid) this.getGrid();
			if ((i < grid.getXSize()) && (j < grid.getYSize())
					&& ((i != this._x) && (j != this._y))) {
				if (grid.getBug(i, j, 1) instanceof SCBug) {
					SCBug tmp = (SCBug) grid.getBug(i, j, 1);
					if ((tmp.getSex() != this.getSex()) && tmp.isFertile()
							&& tmp.hasEnoughSugar()) {
						// Initial Sugar (Half of the Amount of
						// Sugar of the Parents by its Birth)
						int thisCurHalf = this.getInitialSugar() / 2;
						int tmpCurHalf = tmp.getInitialSugar() / 2;
						int gen = Math.max(this.generation, tmp.generation);
						freePlaceBug = this
								.setDescendantToFreeCoordinatesAround(
										thisCurHalf + tmpCurHalf, gen + 1,
										this, tmp);
						if (freePlaceBug != null) {
							// Remove this Amount of
							// Sugar
							// from the Parents
							this.removeSugar(thisCurHalf);
							tmp.removeSugar(tmpCurHalf);
						} else {
							freePlaceBug = tmp
									.setDescendantToFreeCoordinatesAround(
											thisCurHalf + tmpCurHalf, gen + 1,
											this, tmp);
							if (freePlaceBug != null) {
								// Remove this Amount of
								// Sugar
								// from the Parents
								this.removeSugar(thisCurHalf);
								tmp.removeSugar(tmpCurHalf);
							}
						}
					}
				}
			}
		}
		return freePlaceBug;
	}

	/**
	 * Reproduces if fertile
	 * 
	 * @return the Descendant
	 */
	private SCBug reproduce() {
		SCBug freePlaceBug = null;
		boolean rdy = false;
		// if (helper.getExtendedVonNeumannNeighborhood()) {
		// Vector<int[]> coords = new Vector<int[]>();
		// coords = getExtendedVonNeumannCoordinatesAround();
		// if ((coords == null) || !coords.isEmpty()) {
		// for (int i = 0; (i < coords.size()) && !rdy; i++) {
		// System.out.println("\n" + coords.size() + "\nx: " +
		// coords.get(i)[0] + ", y: " + coords.get(i)[1] + "\n");
		// if (!((freePlaceBug = moveNewBug(coords.get(i)[0], coords
		// .get(i)[1])) == null)) {
		// rdy = true;
		// }
		// }
		// }
		// } else {
		for (int i = (this._x - this.getVisionRadiusReproduce()); (i < (this._x + this
				.getVisionRadiusReproduce()))
				&& !rdy; i++) {
			for (int j = (this._y - this.getVisionRadiusReproduce()); (j < (this._y + this
					.getVisionRadiusReproduce()))
					&& !rdy; j++) {
				if (!((freePlaceBug = moveNewBug(i, j)) == null)) {
					rdy = true;
				}
			}
		}
		// }
		return freePlaceBug;
	}
	
	/**
	 * increase Metabolism if Factor > 1
	 * 
	 * @param Factor
	 */
	public void setMetabolismByFactor(double Factor) {
		int metabolismTmp = (int)((double)this.metabolism * Factor);
		if (metabolismTmp > 0 /**&& this.metabolism < metabolismTmp*/)
			this.metabolism = metabolismTmp;
		
	}
	
	/**
	 * increase Vision Radius if factor > 1
	 * 
	 * @param Factor
	 */
	public void setVisionRadiusByFactor(double Factor) {
		int visionRadiusTmp = (int)((double)this.visionRadius * Factor);
		if (visionRadiusTmp > 0 /**&& this.visionRadius < visionRadiusTmp*/)
			this.visionRadius = visionRadiusTmp;
	}
	
	/**
	 * increase Vision Radius Reproduce if Factor > 1
	 * 
	 * @param Factor
	 */
	public void setVisionRadiusReproduceByFactor(double Factor) {
		int visionRadiusReproduceTmp = (int)((double)this.visionRadiusReproduce * Factor);
		if (visionRadiusReproduceTmp > 0 /**&& this.visionRadiusReproduce < visionRadiusReproduceTmp*/)
			this.visionRadiusReproduce = visionRadiusReproduceTmp;
	}

	/**
	 * Die-Function (Pass on the Sugar etc.)
	 */
	private void die() {
		// Pass on
		if (getCurrWealth() > 0) {
			int amount = 0;
			if ((this.descendants != null) && (this.descendants.size() > 0)) {
				amount = (int) (getCurrWealth() / this.descendants.size());
				for (int i = 0; i < this.descendants.size(); i++) {
					// If Descendant is alive
					if (this.descendants.get(i) != null) {
						this.descendants.get(i).addSugar(amount);
						// Otherwise remove him from the List
					} else {
						this.descendants.remove(i);
					}
				}
			}
		}
		// Count
		deathCount++;
		deathAge += this.getCurrAge();
		if((this.getMetabolism() + this.getVisionRadius()) >= 
			(helper.getMetabolism() + helper.getVisionRadius() + 4)){
			d_rich++;
			d_AgeRich += this.getCurrAge();
		} else {
			d_poor++;
			d_AgePoor += this.getCurrAge();
		}
		// Leave the Field
		this.leave();
	}
}