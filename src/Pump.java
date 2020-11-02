public class Pump {
    private double insulinAvailable;
    private double insulin;

    public Pump() {
        this.insulinAvailable = Config.STARTING_INSULIN_STORAGE;
    }

    // Receive a dosage and check if the reservoir has enough insulin
    public double[] receiveCommand(double insulin) {
        java.lang.System.out.println("Pump received a command from Controller");
        if ((insulinAvailable - insulin) >= 0) {
            this.insulin = insulin;
            return new double[]{0, insulinAvailable-insulin};
        } else {
            return new double[]{1, insulinAvailable};
        }
    }

    // Inject the insulin dosage
    public void injectInsulin(Blood blood) {
        if (insulin > 0) {
            insulinAvailable -= insulin;
            java.lang.System.out.println("Pump injected " + insulin + "ml of insulin");
            blood.amendBloodSugar(insulin);
        }
    }

    // Fill the reservoir
    public void fillReserve() {
        insulinAvailable = Config.STARTING_INSULIN_STORAGE;
    }

    public double getInsulinAvailable() {
        return insulinAvailable;
    }

}
