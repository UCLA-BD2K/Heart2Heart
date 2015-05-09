package bd2k.heart2heart;

/**
 * Created by Brian on 5/8/2015.
 */
public class RunnerState {

    public static String runnerID = "";
    public static float personalSpeed = 0;
    public static float personalDistance = 0;

    public static float partnerSpeed = 0;
    public static float partnerDistance = 0;

    public static float relativeDistance = 0;
    public static boolean sync = true;

    public static long lastServerResponse = 0;

    public static boolean running = false;

    public static Communicator sender;

    public static float mileTime(float speedInMPS)
    {
        return new Float(1 / (speedInMPS * 0.0372822715));
    }
}
