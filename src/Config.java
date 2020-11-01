import java.sql.Connection;

public class Config {
    public static int MEASUREMENT_INTERVAL = 5;
    public static int INJECTION_INTERVAL = MEASUREMENT_INTERVAL*6;

    public static double STARTING_SUGAR = 100; //100
    public static double STARTING_INSULIN = 0;
    public static int EATING_CYCLE = 30;
    public static double FOOD_SUGAR = 150;
    public static double CORRECTION_FACTOR = 50;

    public static double MAX_INJECTION = 5;
    public static double MIN_INJECTION = 0.1;
    public static double MAX_CUMULATIVE_DOSE = 20;
    public static double STARTING_CUMULATIVE_DOSE = 0;
    public static int STARTING_BATTERY = 100;
    public static double STARTING_TREND = 0;
    public static double MAX_BLOOD_SUGAR = 250;
    public static double INJECTION_THRESHOLD = 150;
    public static double MIN_BLOOD_SUGAR = 70;
    public static double TARGET_BLOOD_SUGAR = 100;
    public static double TREND_STRICTNESS = 20;

    public static double STARTING_INSULIN_STORAGE = 7;
}
