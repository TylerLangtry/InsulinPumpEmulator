public class Pump {
    private double insulinAvailable;
    private double insulin;
    private boolean needleEquipt;
    private boolean operationStatus;

    public Pump() {
        this.insulinAvailable = 100.00;
        this.needleEquipt = true;
        this.operationStatus = true;
    }

    public double getInsulinAvailable() {
        return insulinAvailable;
    }

    public boolean isNeedleEquipt() {
        return needleEquipt;
    }

    public void setOperationStatus(boolean operationStatus) {
        this.operationStatus = operationStatus;
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
