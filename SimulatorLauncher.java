public class SimulatorLauncher {

    private SimulatorLauncher() {
    }

    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        simulator.runLongSimulation();
    }
}