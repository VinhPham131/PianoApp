package com.midterm.pianoapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.midterm.pianoapp.R;
import com.midterm.pianoapp.model.Key;
import com.midterm.pianoapp.ultils.SoundManager;

import java.util.ArrayList;

public class PianoView extends View {
    public static final int NUMBER_OF_KEYS = 14;
    private ArrayList<Key> whites;
    private ArrayList<Key> blacks;

    //Set up width, height for key
    private int keyWidth, keyHeight;

    Paint blackPen, whitePen, yellowPen;

    //Call sound
    private SoundManager soundManager;


    public PianoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        whites = new ArrayList<Key>();
        blacks = new ArrayList<Key>();

        //Set white color pen
        whitePen = new Paint();
        whitePen.setColor(Color.WHITE);
        whitePen.setStyle(Paint.Style.FILL);

        //Set black color pen
        blackPen = new Paint();
        blackPen.setColor(Color.BLACK);
        blackPen.setStyle(Paint.Style.FILL);

        //Set yellow color pen
        yellowPen = new Paint();
        yellowPen.setColor(Color.YELLOW);
        yellowPen.setStyle(Paint.Style.FILL);

        //Call sound
        soundManager = SoundManager.getInstance();
        soundManager.init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Set up width, height for key
        keyWidth = w / NUMBER_OF_KEYS;
        keyHeight = h;

        int[] whiteSounds = {
                R.raw.c3, R.raw.d3, R.raw.e3, R.raw.f3, R.raw.g3, R.raw.a3, R.raw.b3,
                R.raw.c4, R.raw.d4, R.raw.e4, R.raw.f4, R.raw.g4, R.raw.a4, R.raw.b4
        };

        int[] blackSounds = {
                R.raw.db3, R.raw.eb3, R.raw.gb3, R.raw.ab3, R.raw.bb3,
                R.raw.db4, R.raw.eb4, R.raw.gb4, R.raw.ab4, R.raw.bb4
        };

        int blackCount = 15;
        //Create white keys array
        for(int i = 0; i < NUMBER_OF_KEYS; i++) {
            int left = i * keyWidth;
            int right = left + keyWidth;

            //Set size of Rectangle and add sound for key
            RectF rectF = new RectF(left, 0, right, h);
            whites.add(new Key(whiteSounds[i], rectF, false));

            //Create black keys array
            if(i != 0 && i != 3 && i != 7 && i != 10) {
                rectF = new RectF((float)(i-1) * keyWidth + 0.75f * keyWidth,
                        0,
                        (float)(i-1) * keyWidth + 1.25f * keyWidth,
                        0.6f * keyHeight);
                blacks.add(new Key(blackSounds[i], rectF, false));
            }
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        //Draw white keys
        for(Key k : whites) {
            canvas.drawRect(k.rect, k.down ? yellowPen : whitePen);
        }

        //Draw line to separate white keys
        for(int i = 1; i < NUMBER_OF_KEYS; i++) {
            canvas.drawLine(i * keyWidth, 0, i * keyWidth, keyHeight, blackPen);
        }
        //Draw black keys
        for(Key k : blacks) {
            canvas.drawRect(k.rect, k.down ? yellowPen : blackPen);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Set action for keys
        int action = event.getAction();
        boolean isDownAction = action == MotionEvent.ACTION_DOWN ||
                                action == MotionEvent.ACTION_MOVE;

        for (int touchIndex = 0; touchIndex<event.getPointerCount(); touchIndex++) {
            float x = event.getX(touchIndex);
            float y = event.getY(touchIndex);

            for (Key k : blacks) {
                if (k.rect.contains(x, y)) {
                    k.down = isDownAction;
                    if(isDownAction) {
                        soundManager.playSound(k.sound);
                    }
                    invalidate();
                    return true;
                }
            }

            for(Key k : whites) {
                if(k.rect.contains(x,y)) {
                    k.down = isDownAction;
                    if(isDownAction) {
                        soundManager.playSound(k.sound);
                    }
                }
            }
        }
        //Draw back
        invalidate();
        return true;
//        return super.onTouchEvent(event);
    }
}
