package com.example.jkdaisuki;

public class SimpleAlg {
    private static double xa = 0, ya = 0, za = 0;
    private static double xv = 0, yv = 0, zv = 0;
    private static double xp = 0, yp = 0, zp = 0;

    private static double lxa = 0, lya = 0, lza = 0;
    private static double lxv = 0, lyv = 0, lzv = 0;
    private static double lxp = 0, lyp = 0, lzp = 0;

    private static final double dt = 0.005;
    private static final double threshold = 0.01;

    private static double abs(double num) {
        if (num <= 0) {
            return -num;
        } else {
            return num;
        }
    }

    private static double filter(double input, double limit) {
        return abs(input) > limit ? input : 0;
    }

    public static double[] getPosition(double l1, double l2, double l3) {
//        if (abs(l1) > threshold) {
//            xa = expSmoothing((filter(l1,0.1)), lxa, 0.9);
//            xv = expSmoothing(filter(lxv + (xa + lxa) * dt / 2, 0.02),lxv,0.9);
//        } else {
//            xv = 0;
//            xa = 0;
//        }
//
//        if (abs(l2) > threshold) {
//            ya = ya = expSmoothing((filter(l2,0.1)), lya, 0.9);
//            yv = expSmoothing(filter(lyv + (ya + lya) * dt / 2, 0.02),lyv,0.9);
//        } else {
//            yv = 0;
//            ya = 0;
//        }
//
//        if (abs(l3) > threshold) {
//            za = za = expSmoothing((filter(l3,0.1)), lza, 0.9);
//            zv = expSmoothing(filter(lzv + (za + lza) * dt / 2, 0.1),lzv,0.9);
//        } else {
//            zv = 0;
//            za = 0;
//        }

        xa = l1;
        ya = l2;
        za = l3;

        xv = lxv + xa * dt;
        yv = lyv + ya * dt;
        zv = lzv + za * dt;

//        xp = expSmoothing(xp + (xv + lxv) * dt / 2,lxp,0.95);
//        yp = expSmoothing(yp + (yv + lyv) * dt / 2,lyp,0.95);
//        zp = expSmoothing(zp + (zv + lzv) * dt / 2,lzp,0.95);

        xp = lxp + xv * dt;
        yp = lyp + yv * dt;
        zp = lzp + zv * dt;

        lxa = xa;
        lya = ya;
        lza = za;

        lxv = xv;
        lyv = yv;
        lzv = zv;

        lxp = xp;
        lyp = yp;
        lzp = zp;

        double[] temp = {xp, yp, zp};
        return temp;
    }

    public static double[] getVelocity() {
        double[] temp = {xv, yv, zv};
        return temp;
    }

    public static double expSmoothing(double input, double old, double alpha) {
        return old + alpha * (input - old);
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
