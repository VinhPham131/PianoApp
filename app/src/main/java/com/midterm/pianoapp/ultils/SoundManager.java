package com.midterm.pianoapp.ultils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.util.SparseIntArray;

import android.os.Handler;

import com.midterm.pianoapp.R;

import java.util.logging.LogRecord;

public class SoundManager {
    private SoundPool mSoundPool;
    private SparseIntArray mSoundPoolMap;
    private boolean mMute = false;
    private Context context;

    private static final int MAX_STREAMS = 10;
    private static final int STOP_DELAY_MILLIS = 10000;
    private Handler mHandler;

    //Singleton pattern
    private static SoundManager instance = null;

    public SoundManager () {
        this.context = context;
        mSoundPool = new SoundPool(MAX_STREAMS,
                AudioManager.STREAM_MUSIC,
                0);

        mSoundPoolMap = new SparseIntArray();
        mHandler = new Handler();
    }
    //Singleton pattern
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    //Set up sound
    public void initStreamTypeMedia(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
    //Add sound
    public void addSound(int soundID) {
        mSoundPoolMap.put(soundID, mSoundPool.load(context, soundID, 1));

    }
    //Play sound
    public void playSound(int soundID) {
    Log.d("SoundManager", "playSound method called with soundID: " + soundID);
    if(mMute) {
        return;
    }
    boolean hasSound = mSoundPoolMap.indexOfKey(soundID) >= 0;

    if(!hasSound) {
        Log.d("SoundManager", "Sound with soundID: " + soundID + " not found in SoundPoolMap.");
        return;
    }
    final int soundId = mSoundPool.play(mSoundPoolMap.get(soundID), 1, 1, 1, 0, 1f);
    if (soundId == 0) {
        Log.d("SoundManager", "SoundPool.play returned 0, sound could not be played.");
    } else {
        Log.d("SoundManager", "Sound with soundID: " + soundID + " is playing. Result: " + soundId);
    }
    scheduleSoundStop(soundId);
}
    //Schedule sound
    public void scheduleSoundStop(final int soundID) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSoundPool.stop(soundID);
            }
        }, STOP_DELAY_MILLIS);
    }


    public void init(Context context) {
        this.context = context;
        instance.initStreamTypeMedia((Activity) context);
        instance.addSound(R.raw.c3);
        instance.addSound(R.raw.c4);
        instance.addSound(R.raw.d3);
        instance.addSound(R.raw.d4);
        instance.addSound(R.raw.e3);
        instance.addSound(R.raw.e4);
        instance.addSound(R.raw.f3);
        instance.addSound(R.raw.f4);
        instance.addSound(R.raw.a3);
        instance.addSound(R.raw.a4);
        instance.addSound(R.raw.b3);
        instance.addSound(R.raw.db3);
        instance.addSound(R.raw.db4);
        instance.addSound(R.raw.eb3);
        instance.addSound(R.raw.eb4);
        instance.addSound(R.raw.gb3);
        instance.addSound(R.raw.gb4);
        instance.addSound(R.raw.ab3);
        instance.addSound(R.raw.ab4);
        instance.addSound(R.raw.bb3);
        instance.addSound(R.raw.bb4);



    }

}
