import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a Human.
 * Humans age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Human extends Animal
{
    // Characteristics shared by all Humans (class variables).

    // The age at which a Human can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a Human can live.
    private static final int MAX_AGE = 60;
    // The likelihood of a Human breeding.
    private static final double BREEDING_PROBABILITY = 0.20;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 6;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // Individual characteristics (instance fields).
    private static final double FEMALE_PROBABILITY = 0.5; 
    // The Human's age.
    private int age;

    /**
     * Create a new Human. A Human may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Human will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Human(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
        if(rand.nextDouble() <= FEMALE_PROBABILITY) { 
           super.isFemale  = true; 
        } else { 
            super.isFemale = false;                        
        } 
    }
    
    /**
     * This is what the Human does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newHumans A list to return newly born Humans.
     */
    public void act(List<Animal> newHumans, boolean isDay)
    {
        incrementAge();
        if(isAlive()&&(isDay)) {
            giveBirth(newHumans);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age.
     * This could result in the Human's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this Human is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newHumans A list to return newly born Humans.
     */
    private void giveBirth(List<Animal> newHumans)
    {
        // New Humans are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Human young = new Human(false, field, loc);
            newHumans.add(young);
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
     * A Human can breed if it has reached the breeding age.
     * @return true if the Human can breed, false otherwise.
     */
    private boolean canBreed() { 
    
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Human) {
                Human human = (Human) animal;
        
                if(age >= BREEDING_AGE && (human.getIsFemale() && !this.getIsFemale()) || (!human.getIsFemale() && this.getIsFemale()) ) {
                return true; 
        }
    }
    }
                return false;
        
        
    }
   
    private void setLocationDisease()
    {
        
    }
    
    private void checkDisease() {
        Location currentLocation = this.getLocation();
        
    }
}
