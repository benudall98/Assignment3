import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing Humans and Darkseekeres.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a Darkseeker will be created in any given grid position.
    private static final double Darkseeker_CREATION_PROBABILITY = 0.09;
    // The probability that a Human will be created in any given grid position.
    private static final double Human_CREATION_PROBABILITY = 0.04;    
    private static final double Dog_CREATION_PROBABILITY = 0.03;   
    private static final double MutantDog_CREATION_PROBABILITY = 0.06;
    private static final double Disease_CREATION_PROBABILITY = 0.005;
    // List of animals in the field.
    private List<Animal> animals;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;

    private int stepCopy;
    private boolean isDay;


    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        animals = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Human.class, Color.GREEN);
        view.setColor(Darkseeker.class, Color.RED);
        view.setColor(Dog.class, Color.YELLOW); 
        view.setColor(MutantDog.class, Color.BLACK); 
        view.setColor(InfectedHuman.class, Color.cyan);

        // Setup a valid starting point.
        reset();
    }
    

    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            stepCopy = step;
            stepCopy = stepCopy%24;
            if(stepCopy<=12)
            {
                isDay=true;
            }
            else
            {
                isDay=false;
            }
            simulateOneStep();
             delay(200);   // uncomment this to run more slowly
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * Darkseeker and Human.
     */
    public void simulateOneStep()
    {
        step++;
        stepCopy++;
        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<>();        
        // Let all Humans act.
        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals, isDay);
            if(! animal.isAlive()) {
                it.remove();
            }
        }

        // Add the newly born Darkseekeres and Humans to the main lists.
        animals.addAll(newAnimals);

        view.showStatus(step, field, isDay);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        populate();

        // Show the starting state in the view.
        view.showStatus(step, field, isDay);
    }

    /**
     * Randomly populate the field with Darkseekeres and Humans.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= Darkseeker_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Darkseeker darkseeker = new 
                    Darkseeker(true, field, location);
                    animals.add(darkseeker);
                }
                else if(rand.nextDouble() <= Human_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Human Human = new Human(true, field, location);
                    animals.add(Human);
                } else if(rand.nextDouble() <= Dog_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Dog dog = new Dog(true, field, location);
                    animals.add(dog);
                } else if(rand.nextDouble() <= MutantDog_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    MutantDog mutantDog = new MutantDog(true, field, location);
                    animals.add(mutantDog);
                    // else leave the location empty.
                } else if(rand.nextDouble()<= Disease_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    InfectedHuman infectedHuman = new InfectedHuman(true, field, location);
                    animals.add(infectedHuman); 
                }
            }
        }
    }

    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }

    }
    
    private boolean getIsDay()
    {
        return isDay;
    }
}
