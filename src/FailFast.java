public class FailFast {

    public String checkACK(String message) {
        if (message.equals("ACK")) {
            return "ACK";
        } else {
            return "ERR";
        }
    }

    public String receiveControllerRequest(double insulin) {
        return "ACK";
    }

    private String sendAlert() {
        return "ALERT";
    }
}
