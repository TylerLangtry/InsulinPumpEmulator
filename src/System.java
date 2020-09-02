import java.io.PrintWriter;

public class System {

    public static void main(String[] args) {
        Blood blood = new Blood();
        Controller controller = new Controller();
        FailFast failFast = new FailFast();
        Pump pump = new Pump();
        Sensor sensor = new Sensor();


        sensor.measureBloodData(blood);
        controller.receiveBloodData(sensor.sendBloodData());
        pump.receiveCommand(controller.sendInsulinInjection());
        pump.injectInsulin(blood);
        sensor.measureBloodData(blood);
    }
}
