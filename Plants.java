import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Plants
{
    // Whether the creature is alive or not.
    private boolean alive;
    // The living-creature's field.
    private Field field;
    // The living-creature's position in the field.
    private Location location;
    // The age at which a Human can start to breed.
    private static final int REPRODUCING_AGE = 5;
    // The age to which a Human can live.
    private static final int MAX_AGE = 60;
    // The likelihood of a Human breeding.
    private static final double BREEDING_PROBABILITY = 0.20;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // The Human's age.
    private int age;
    /**
     * Create a new living-creature at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plants(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
    }
    
   
    /**
     * Check whether the creature is alive or not.
     * @return true if the creature is still alive.
     */
    private boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the living-creature is no longer alive.
     * It is removed from the field.
     */
    private void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the living-creature's location.
     * @return The living-creature's location.
     */
    private Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the living-creature at the new location in the given field.
     * @param newLocation The living-creature's new location.
     */
    private void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the living-creature's field.
     * @return The living-creature's field.
     */
    private Field getField()
    {
        return field;
    }
    
}
