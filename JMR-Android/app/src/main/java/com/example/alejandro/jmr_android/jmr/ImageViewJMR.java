package com.example.alejandro.jmr_android.jmr;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.example.alejandro.jmr_android.R;

/**
 * Created by alejandro on 21/08/2017.
 */

public class ImageViewJMR extends ImageView {

    boolean pressed;

    public ImageViewJMR(Context context) {
        super(context);
        pressed = false;
    }

    public boolean isPressed(){
        return pressed;
    }

    public void setPressed(boolean pressed){
        this.pressed = pressed;
    }
}
