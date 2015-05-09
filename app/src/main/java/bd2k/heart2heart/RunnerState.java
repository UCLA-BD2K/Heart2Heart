package bd2k.heart2heart;

/**
 * Created by Brian on 5/8/2015.
 */
public class RunnerState {

    public float personalSpeed = 0;
    public float personalDistance = 0;

    public float partnerSpeed = 0;
    public float partnerDistance = 0;

    public float relativeDistance = 0;
    public boolean sync = true;

    public long lastServerResponse = 0;

    public static float mileTime(float speedInMPS)
    {
        return new Float(1 / (speedInMPS * 0.0372822715));
    }
}
