package bd2k.heart2heart;

import android.app.Activity;
import android.media.MediaPlayer;

/**
 * Created by Brian on 5/8/2015.
 */
public class SoundThread extends Thread {

    RunnerState myState;
    MediaPlayer player;
    Activity mainActivity;

    int lastSoundPlayed;

    public SoundThread(RunnerState myState, Activity mainActivity)
    {
        this.myState = myState;
        this.mainActivity = mainActivity;

        //starting sound!
        playSound(R.raw.sync);

    }

    public void run() {
        while (true) {
            try{sleep(3000);}catch(Exception e){}
            playSoundIfNeeded();//playSound(R.raw.sync);
        }
    }

    public void playSoundIfNeeded()
    {
        if(System.currentTimeMillis() - myState.lastServerResponse > 3000)
            return;

        final float maxSeparation = 15;//maximum distance between runners before audio plays
        if(myState.relativeDistance > maxSeparation)
        {
            myState.sync = false;
            playSound(R.raw.too_fast);
        }
        else if(myState.relativeDistance < -1*maxSeparation)
        {
            myState.sync = false;
            playSound(R.raw.too_slow);
        }
        else if(!myState.sync && Math.abs(myState.relativeDistance) < maxSeparation)
        {
            myState.sync = true;
            playSound(R.raw.sync);
        }
    }

    public void playSound(int sound)
    {
        if(sound != lastSoundPlayed)
            player = MediaPlayer.create(mainActivity, sound);

        lastSoundPlayed = sound;
        player.start();
    }
}
