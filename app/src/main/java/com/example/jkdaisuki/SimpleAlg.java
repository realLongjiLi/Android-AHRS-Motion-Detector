package com.example.jkdaisuki;

public class SimpleAlg {
    private static double xa = 0, ya = 0, za = 0;
    private static double xv = 0, yv = 0, zv = 0;
    private static double xp = 0, yp = 0, zp = 0;
    private static final double dt = 0.1;

    public static double[] getPosition(double l1, double l2, double l3) {
        xa = l1;
        ya = l2;
        za = l3;

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
}
