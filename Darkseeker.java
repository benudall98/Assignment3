import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a Darkseeker.
 * Darkseekers age, move, eat Humans, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Darkseeker extends Animal
{
    // Characteristics shared by all Darkseekers (class variables).

    // The age at which a Darkseeker can start to breed.
    private static final int BREEDING_AGE = 20;
    // The age to which a Darkseeker can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a Darkseeker breeding.
    private static final double BREEDING_PROBABILITY = 0.8;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single Human. In effect, this is the
    // number of steps a Darkseeker can go before it has to eat again.
    private static final int Human_FOOD_VALUE = 15;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    private static final double FEMALE_PROBABILITY = 0.5; 

    // Individual characteristics (instance fields).
    // The Darkseeker's age.
    private int age;
    // The Darkseeker's food level, which is increased by eating Humans.
    private int foodLevel;

    /**
     * Create a Darkseeker. A Darkseeker can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the Darkseeker will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Darkseeker(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(Human_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = Human_FOOD_VALUE;
        }
        if(rand.nextDouble() <= FEMALE_PROBABILITY) { 
            super.isFemale  = true; 
        } else { 
            super.isFemale = false;                        
        } 
    }

    /**
     * This is what the Darkseeker does most of the time: it hunts for
     * Humans. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newDarkseekers A list to return newly born Darkseekers.
     */
    public void act(List<Animal> newDarkseekers, boolean isDay)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()&&!isDay) {
            giveBirth(newDarkseekers);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            move();
        }
    }

    /**
     * Increase the age. This could result in the Darkseeker's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this Darkseeker more hungry. This could result in the Darkseeker's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for Humans adjacent to the current location.
     * Only the first live Human is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Human) {
                Human Human = (Human) animal;
                if(Human.isAlive()) { 
                    Human.setDead();
                    foodLevel += Human_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this Darkseeker is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newDarkseekers A list to return newly born Darkseekers.
     */
    private void giveBirth(List<Animal> newDarkseekers)
    {
        // New Darkseekers are born into adjacent locations.
        // Get a list of adjacent free locations.
        List<Location> free = getField().getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Darkseeker young = new Darkseeker(false, getField(), loc);
            newDarkseekers.add(young);
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A Darkseeker can breed if it has reached the breeding age.
     */
    private boolean canBreed() { 
        List<Location> adjacent = getField().adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = getField().getObjectAt(where);
            if(animal instanceof Darkseeker) {
                Darkseeker darkseeker = (Darkseeker) animal;
                if(age >= BREEDING_AGE && (darkseeker.getIsFemale() && !this.getIsFemale()) || (!darkseeker.getIsFemale() && this.getIsFemale()) ) {
                    return true; 
                }
            }
        }
        return false;
    }
}
