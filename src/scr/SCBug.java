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

	/************************************************/
	// Constructors
	/************************************************/

	/**
	 * Default Constructor (only for the 1st Generation)
	 */
	public SCBug() {
		setValues(0, 0, getRandomSex(), 1, null, null);
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
		// Increase the Age
		if ((this.currAge >= this.maxAge) || (this.currWealth <= 0)) {
			// This Bug has to die, it is too old or has no Sugar!
			die();
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

			// Reproduce if fertile
			if (this.isFertile() && this.hasEnoughSugar()) {
				SCBug descendant = reproduce();
				if ((descendant != null) && (this.descendants != null)) {
					this.descendants.add(descendant);
					numberOfDescendants++;
				}
			}

			// Consume Sugar every metabolism Timesteps
			if (this.currMB >= helper.getMetabolism()) {
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
	 * Moves the Agent: If no more Sugar at the current Place 1. Move to the
	 * Place with the highest Amount of Sugar OR 2. Search active for a Partner
	 */
	private void move() {
		if (this.getGrid() instanceof SCGrid) {
			SCGrid grid = (SCGrid) this.getGrid();
			if (grid.getBug(this._x, this._y, 0) instanceof SCSugarBug) {
				if (((SCSugarBug) grid.getBug(this._x, this._y, 0))
						.getCurrentAmountOfSugar() <= 0) {
					boolean found = false;
					// Search Active for a Partner
					if (helper.searchActiveForPartner()) {
						SCBug bug = null;
						Vector<SCBug> neighbours = new Vector<SCBug>();
						neighbours = this.getNeighbours();
						// If there are Neighbours
						if ((neighbours != null) && !neighbours.isEmpty()) {
							for (int i = 0; (i < neighbours.size()) && !found; i++) {
								// If a Neighbour fits as a Partner
								if ((neighbours.get(i).getSex() != this
										.getSex())
										&& neighbours.get(i).isFertile()
										&& neighbours.get(i).hasEnoughSugar()) {
									Vector<SCBug> freePlaces = new Vector<SCBug>();
									freePlaces = neighbours.get(i)
											.getFreePlacesAround();
									// If free Places around the potential
									// Partner found
									if ((freePlaces != null)
											&& !freePlaces.isEmpty()) {
										// Move to a random Position around
										// the potential Partner
										int rand = helper
												.getRandomIntWithinLimits(0,
														freePlaces.size() - 1);
										bug = freePlaces.get(rand);
										// If the free Place is in Sight of this
										// Bug
										if ((bug != null) && isInSight(bug)) {
											// Move
											this.moveBug(bug._x, bug._y, 1);
										}
										found = true;
									}
								}
							}
						}
					}
					// If no Partner with free Space around found or
					// "Search Active for a Partner" is not active
					if (!found) {
						SCSugarBug sugarBug = null;
						// Move Bug to the Field with the highest Amount o
						// Sugar
						Vector<SCSugarBug> vec = new Vector<SCSugarBug>();
						vec = getHighestAmountOfSugar(helper.getVisionRadius());
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

	/**
	 * Show Bug at the Grid by sex
	 */
	public void updateDepiction() {
		String depictName = "DepictionBug" + (getSex() ? "Female" : "Male");
		super.setDepiction((Depiction) (getBase().findBySerno(depictName)));
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
		return ((getCurrAge() >= getFertilityAgeMin()) && (getCurrAge() <= getFertilityAgeMax()));
	}

	/**
	 * Returns if the SCBug has enough Money
	 * 
	 * @return if the SCBug has enough Money
	 */
	public boolean hasEnoughSugar() {
		int divideFactor = helper.getDivideFactorHasEnoughSugar();
		if (divideFactor > 0) {
			return (getCurrWealth() >= (getInitialSugar() / divideFactor));
		}
		return (getCurrWealth() >= getInitialSugar());
	}

	/************************************************/
	// Getter-Functions
	/************************************************/

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
	 * Returns the sum of dyingages
	 * 
	 * @return sum of dyingages
	 */
	public int getDyingAge() {
		return deathAge;
	}

	/**
	 * Returns the SCSugarBug with the highest Amount of Sugar within a
	 * Blickfeld
	 * 
	 * @param bf
	 *            Blickfeld
	 * @return a Vector of SCSugarBugs with the highest Amount of Sugar within
	 *         the Blickfeld
	 */
	private Vector<SCSugarBug> getHighestAmountOfSugar(int bf) {
		Vector<SCSugarBug> vec = new Vector<SCSugarBug>();
		int currHighestValue = -1;
		if (this.getGrid() instanceof SCGrid) {
			SCGrid grid = (SCGrid) this.getGrid();
			for (int i = (this._x - helper.getVisionRadius()); i < (this._x + helper
					.getVisionRadius()); i++) {
				for (int j = (this._y - helper.getVisionRadius()); j < (this._y + helper
						.getVisionRadius()); j++) {
					if ((i < grid.getXSize()) && (j < grid.getYSize())) {
						if (grid.getBug(i, j, 0) instanceof SCSugarBug) {
							SCSugarBug tmp = (SCSugarBug) grid.getBug(i, j, 0);
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

		return vec;
	}

	/**
	 * Returns the Neighbours
	 * 
	 * @return the Neighbours in a Vector of SCBugs
	 */
	private Vector<SCBug> getNeighbours() {
		Vector<SCBug> vec = new Vector<SCBug>();
		if (this.getGrid() instanceof SCGrid) {
			SCGrid grid = (SCGrid) this.getGrid();
			for (int i = (this._x - helper.getVisionRadius()); i < (this._x + helper
					.getVisionRadius()); i++) {
				for (int j = (this._y - helper.getVisionRadius()); j < (this._y + helper
						.getVisionRadius()); j++) {
					if ((i < grid.getXSize()) && (j < grid.getYSize())) {
						if (grid.getBug(i, j, 1) instanceof SCBug) {
							vec.add((SCBug) grid.getBug(i, j, 1));
						}
					}
				}
			}
		}
		return vec;
	}

	/**
	 * Returns free Places around the Bug
	 * 
	 * @return free Places around the Bug
	 */
	public Vector<SCBug> getFreePlacesAround() {
		Vector<SCBug> vec = new Vector<SCBug>();
		if (this.getGrid() instanceof SCGrid) {
			SCGrid grid = (SCGrid) this.getGrid();
			for (int i = (this._x - helper.getVisionRadius()); i < (this._x + helper
					.getVisionRadius()); i++) {
				for (int j = (this._y - helper.getVisionRadius()); j < (this._y + helper
						.getVisionRadius()); j++) {
					if ((i < grid.getXSize()) && (j < grid.getYSize())) {
						if (grid.getBug(i, j, 1) == null) {
							SCBug newBug = new SCBug();
							newBug.moveBug(i, j, 1);
							vec.add(newBug);
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
	 * Set Bugs sex by adding Bugs to the Grid with a GUI
	 * 
	 * @param depict
	 *            Depiction
	 */
	public void setDepiction(Depiction depict) {

		if (!depict.serno.startsWith("DepictionBug"))
			return;

		if (depict.serno.endsWith("Female"))
			sex = true;
		else if (depict.serno.endsWith("Male"))
			sex = false;
		else
			sex = getRandomSex();

		super.setDepiction(depict);
	}

	/**
	 * Returns a SCBug with where the free Coordinates are
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
	 * @return a SCBug with where the free Coordinates are
	 */
	public SCBug setDescendantToFreeCoordinatesAround(int initialSugar,
			int generation, SCBug parent1, SCBug parent2) {
		SCBug b = null;
		if (this.getGrid() instanceof SCGrid) {
			SCGrid grid = (SCGrid) this.getGrid();
			for (int i = (this._x - helper.getVisionRadius()); i < (this._x + helper
					.getVisionRadius()); i++) {
				for (int j = (this._y - helper.getVisionRadius()); j < (this._y + helper
						.getVisionRadius()); j++) {
					if ((i < grid.getXSize()) && (j < grid.getYSize())) {
						if ((grid.getBug(i, j, 1) == null)
						/* && !(grid.getBug(i, j, 1) instanceof SCBug) */) {
							b = new SCBug(0, initialSugar, getRandomSex(),
									generation, parent1, parent2);
							b.moveBug(i, j, 1);
							b.join(grid);
							b.updateDepiction();
							return b;
						}
					}
				}
			}
		}

		return b;
	}

	/**
	 * Sets the Generation
	 * 
	 * @param generation
	 *            Generation
	 */
	public void setGeneration(int generation) {
		if (generation > 0) {
			this.generation = generation;
		}
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
		this.initialSugar = this.currWealth;
		this.sex = sex;
		this.fertilityAgeMin = getRandomFertilityAgeMin();
		this.fertilityAgeMax = getRandomFertilityAgeMax();
		this.maxAge = getRandomMaxAge();
	}

	/************************************************/
	// Helper-Functions
	/************************************************/

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
	 * Returns if Bug b is in the VisionRadius of this Bug
	 * 
	 * @param b
	 *            Bug
	 * @return true if Bug b is in the VisionRadius of this Bug, false else
	 */
	private boolean isInSight(SCBug b) {
		boolean x = (b._x > (this._x - helper.getVisionRadius()))
				&& (b._x < (this._x + helper.getVisionRadius()));
		boolean y = (b._y > (this._y - helper.getVisionRadius()))
				&& (b._y < (this._y + helper.getVisionRadius()));
		return x && y;
	}

	/**
	 * Reproduces if fertile
	 * 
	 * @return the Descendant
	 */
	private SCBug reproduce() {
		SCBug freePlaceBug = null;
		if (this.getGrid() instanceof SCGrid) {
			SCGrid grid = (SCGrid) this.getGrid();
			boolean rdy = false;
			for (int i = (this._x - helper.getVisionRadiusReproduce()); (i < (this._x + helper
					.getVisionRadiusReproduce()))
					&& !rdy; i++) {
				for (int j = (this._y - helper.getVisionRadiusReproduce()); (j < (this._y + helper
						.getVisionRadiusReproduce()))
						&& !rdy; j++) {
					if ((i < grid.getXSize()) && (j < grid.getYSize())) {
						if (grid.getBug(i, j, 1) instanceof SCBug) {
							SCBug tmp = (SCBug) grid.getBug(i, j, 1);
							if ((tmp.getSex() != this.getSex())
									&& tmp.isFertile() && tmp.hasEnoughSugar()) {
								// Get Neighbours from this Bug
								Vector<SCBug> vec = new Vector<SCBug>();
								vec = this.getNeighbours();
								if ((vec != null) && (vec.size() > 0)) {
									// Initial Sugar (Half of the Amount of
									// Sugar of the Parents by its Birth)
									int thisCurHalf = this.getInitialSugar() / 2;
									int tmpCurHalf = tmp.getInitialSugar() / 2;
									int gen = Math.max(this.generation,
											tmp.generation);
									for (int vi = 0; (vi < vec.size()) && !rdy; vi++) {
										if (isInSight(vec.get(vi))) {
											freePlaceBug = vec
													.get(vi)
													.setDescendantToFreeCoordinatesAround(
															thisCurHalf
																	+ tmpCurHalf,
															gen + 1, this, tmp);
											if (freePlaceBug != null) {
												// Remove this Amount of Sugar
												// from the Parents
												this.removeSugar(thisCurHalf);
												tmp.removeSugar(tmpCurHalf);
												rdy = true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return freePlaceBug;
	}

	/**
	 * Die-Function (Inheritance of the Sugar etc.)
	 */
	private void die() {
		// Inheritate
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
		// Leave the Field
		this.leave();
	}
}
