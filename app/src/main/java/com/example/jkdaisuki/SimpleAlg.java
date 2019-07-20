package com.example.jkdaisuki;

public class SimpleAlg {
    private static double xa = 0, ya = 0, za = 0;
    private static double xv = 0, yv = 0, zv = 0;
    private static double xp = 0, yp = 0, zp = 0;
    private static final double dt = 0.1;
    private static final double threshold = 0.2;

    public static double[] getPosition(double l1, double l2, double l3) {
        if (l1 > threshold) {
            xa = l1;
        } else {
            xv = 0;
        }

        if (l2 > threshold) {
            ya = l2;
        } else {
            yv = 0;
        }

        if (l3 > threshold) {
            za = l3;
        } else {
            zv = 0;
        }

        xv = xv + xa * dt;
        yv = yv + ya * dt;
        zv = zv + za * dt;

        xp = xp + xv * dt;
        yp = yp + yv * dt;
        zp = zp + zv * dt;

        double[] temp = {xp, yp, zp};
        return temp;
    }

    public static double[] getVelocity() {
        double[] temp = {xv, yv, zv};
        return temp;
    }

    public static float[] exponentialSmoothing(float[] input, float[] output, float alpha ) {
        if ( output == null )
            return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + alpha * (input[i] - output[i]);
        }
        return output;
    }
}
