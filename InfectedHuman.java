
import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a InfectedHuman.
 * InfectedHumans age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class InfectedHuman extends Animal
{
    // Characteristics shared by all InfectedHumans (class variables).

    // The age at which a InfectedHuman can start to breed.
    private static final double INFECTION_PROBABILITY = 0.5;
    // The age to which a InfectedHuman can live.
    private static final int MAX_AGE = 100;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).

    // The InfectedHuman's age.
    private int age;
    //The number of steps a InfectedHuman can take before dying
    private static final int Human_FOOD_VALUE = 150;
    //The probability that a new InfectedHuman is female
    private static final double FEMALE_PROBABILITY = 0.5; 
    //The food level 
    private int foodLevel;

    /**
     * Create a new InfectedHuman. A InfectedHuman may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the InfectedHuman will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public InfectedHuman(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(Human_FOOD_VALUE);
        }
        if(rand.nextDouble() <= FEMALE_PROBABILITY) { 
            super.isFemale  = true; 
        } else { 
            super.isFemale = false;                        
        } 

    }

    /**
     * This is what the InfectedHuman does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newInfectedHumans A list to return newly InfectedHumans.
     */
    public void act(List<Animal> newInfectedHumans, boolean isDay)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            // returns the location of the non-infected human
            // infect that human (delete human in that location and create infectedHuman
            // move 'me' - the infected one - to other free location
            Location newLocation = findHuman(); 
            if(newLocation != null){
                foodLevel += Human_FOOD_VALUE;
                Human nonInfected = (Human) getField().getObjectAt(newLocation);
                nonInfected.setDead();
                InfectedHuman newInfected = new InfectedHuman(false, getField(), newLocation);
                addInfected(newInfectedHumans);
            }
            move();
        }
    }

    /**
     * Look for Humans adjacent to the current location.
     * Only the first live Human is turned into an InfectedHuman.
     * @return Where a human was found, or null if it wasn't.
     */
    private Location findHuman()
    {
        {
            List<Location> adjacent = getField().adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while(it.hasNext()) {
                Location where = it.next();
                Object animal = getField().getObjectAt(where);
                if(animal instanceof Human) {
                    return where;
                }
            }
            return null;
        }
    }

    /**
     * Increase the age.
     * This could result in the InfectedHuman's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this InfectedHuman more hungry. This could result in the InfectedHuman's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * @param the list of newInfectedHumans being created
     * Add the newly created InfectedHuman objects to the animals list
     */
    private void addInfected(List<Animal> newInfectedHumans)
    {
        // New Humans are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int infections = infect();
        for(int b = 0; b < infections && free.size() > 0; b++) {
            Location loc = free.remove(0);
            InfectedHuman young = new InfectedHuman(false, field, loc);
            newInfectedHumans.add(young);
        }
    }

    /**
     * @return the acutal number of humans that will be infected by the infected human
     */
    private int infect()
    {
        // know how many non infected humans exist nearby
        int numberOfHuman = numberOfNonInfected();
        int infections = 0;
        for(int i = 0; i < numberOfHuman; i++){
            if(canInfect() && rand.nextDouble() <= INFECTION_PROBABILITY) {
                infections++;
            }
        }
        return infections;
    }

    /**
     * @return true or false if the animal in an adjacent cell is human
     */
    private boolean canInfect() { 

        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Human) {
                Human human = (Human) animal;
                return true;
            }
        }
        return false;
    }

    /**
     * @return Number of humans in adjacent cells who will come into contact with this infected human
     */
    private int numberOfNonInfected(){
        int n = 0;
        int row = getLocation().getRow();
        int col = getLocation().getCol();
        for(int roffset = -1; roffset <= 1; roffset++) {
            int nextRow = row + roffset;
            if(nextRow >= 0 && nextRow < getField().getDepth()) {
                for(int coffset = -1; coffset <= 1; coffset++) {
                    int nextCol = col + coffset;
                    // Exclude invalid locations and the original location.
                    if(nextCol >= 0 && nextCol < getField().getWidth() && (roffset != 0 || coffset != 0)) {
                        Object object = getField().getObjectAt(nextRow,nextCol);
                        if(object instanceof Human){
                            n++;
                        }
                    }
                }
            }
        }
        return n;
    }
}
