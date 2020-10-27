public class Pump {
    private double insulinAvailable;
    private double insulin;
    private boolean needleEquipt;
    private boolean operationStatus;

    public Pump() {
        this.insulinAvailable = Config.STARTING_INSULIN_STORAGE;
        this.needleEquipt = Config.NEEDLE_EQUIPPED;
    }

    public double getInsulinAvailable() {
        return insulinAvailable;
    }

    public void setOperationStatus(boolean operationStatus) {
    }

    public String receiveCommand(double insulin) {
        java.lang.System.out.println("Pump received a command from Controller");
        if (insulin < insulinAvailable) {
            this.insulin = insulin;
            return "ACK";
        } else {
            return "RJC";
        }
    }

    public void injectInsulin(Blood blood) {
        if (insulin > 0) {
            java.lang.System.out.println("Pump injected " + insulin + "ml of insulin");
            blood.amendBloodSugar(insulin);
        }
    }



}
