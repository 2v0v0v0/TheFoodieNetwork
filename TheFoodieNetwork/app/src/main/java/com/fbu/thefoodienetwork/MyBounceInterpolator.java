package com.fbu.thefoodienetwork;

public class MyBounceInterpolator implements android.view.animation.Interpolator{
    private double amplitude;
    private double frequency;

    public MyBounceInterpolator(double amplitude, double frequency) {
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    @Override
    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ this.amplitude) *
                Math.cos(this.frequency * time) + 1);
    }
}
