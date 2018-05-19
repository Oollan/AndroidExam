package com.example.oollan.androidexam.utils;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.github.florent37.viewanimator.ViewAnimator;

public final class AnimationUtils {

    private AnimationUtils() {
    }

    public static void fadeInFav(ImageView imageView) {
        ViewAnimator.animate(imageView)
                .dp().width(10f, 110f)
                .interpolator(new DecelerateInterpolator())
                .duration(200)
                .thenAnimate(imageView)
                .dp().width(110f, 30f)
                .interpolator(new AccelerateInterpolator())
                .duration(200)
                .start();
    }

    public static void fadeOutFav(ImageView imageView) {
        ViewAnimator.animate(imageView)
                .dp().width(30f, 0f)
                .interpolator(new AccelerateInterpolator())
                .duration(200)
                .start();
    }

    public static void fadeInHeader(ImageView imageView) {
        ViewAnimator.animate(imageView)
                .dp().translationY(-300, 0)
                .singleInterpolator(new OvershootInterpolator())
                .duration(400)
                .start();
    }
}