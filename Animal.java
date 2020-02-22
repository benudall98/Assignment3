import java.util.List;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal
{
    // Whether the creature is alive or not.
    private boolean alive;
    // The living-creature's field.
    private Field field;
    // The living-creature's position in the field.
    private Location location;
    
    protected boolean isFemale; 
    
    /**
     * Create a new living-creature at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Make this living-creature act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born living-creatures.
     */
    abstract public void act(List<Animal> newLivingCreature, boolean isDay);

    /**
     * Check whether the creature is alive or not.
     * @return true if the creature is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the living-creature is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
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
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the living-creature at the new location in the given field.
     * @param newLocation The living-creature's new location.
     */
    protected void setLocation(Location newLocation)
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
    protected Field getField()
    {
        return field;
    }
    
    protected boolean getIsFemale() { 
        return isFemale; 
    }
}
