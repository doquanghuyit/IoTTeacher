package iot.fti.fpt.rogobase.utils;

import android.util.Log;

import java.util.Locale;

public class MathPid {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final boolean LOG_ENABLE = true;
    private void cLog(String format, Object... args)
    {
        if (LOG_ENABLE)
        {
            Log.d(LOG_TAG, String.format(Locale.getDefault(), format, args));
        }
    }


    private double kP;
    private double kI;
    private double kD;
    private double maxI;
    private double minI;
    private double maxCtrl;
    private double minCtrl;
    private double setPoint;

    private double p;
    private double i;
    private double d;
    private double lastError;


    public MathPid(double kP, double kI, double kD, double maxI, double minI, double maxCtrl, double minCtrl, double setPoint) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.maxI = maxI;
        this.minI = minI;
        this.maxCtrl = maxCtrl;
        this.minCtrl = minCtrl;
        this.setPoint = setPoint;

        p = 0.0;
        i = 0.0;
        d = 0.0;
        lastError = 0.0;
    }

    public double loop(double processVar) {
        double error = setPoint - processVar;

        p = kP * error;

        i += (kI * error);
        if (i > maxI) {
            i = maxI;
        } else if (i < minI) {
            i = minI;
        }

        d = kD * (error - lastError);
        lastError = error;

        double ctrl = p + i + d;
        if (ctrl > maxCtrl) {
            ctrl = maxCtrl;
        } else if (ctrl < minCtrl) {
            ctrl = minCtrl;
        }

        cLog("P = %5.2f | I = %5.2f| D = %5.2f", p, i, d);

        return ctrl;
    }

//    double getkP() {
//        return kP;
//    }
//
//    void setkP(double kP) {
//        this.kP = kP;
//    }
//
//    double getkI() {
//        return kI;
//    }
//
//    void setkI(double kI) {
//        this.kI = kI;
//    }
//
//    double getkD() {
//        return kD;
//    }
//
//    void setkD(double kD) {
//        this.kD = kD;
//    }
//
//    double getMaxI() {
//        return maxI;
//    }
//
//    void setMaxI(double maxI) {
//        this.maxI = maxI;
//    }
//
//    double getMinI() {
//        return minI;
//    }
//
//    void setMinI(double minI) {
//        this.minI = minI;
//    }
//
//    double getMaxCtrl() {
//        return maxCtrl;
//    }
//
//    void setMaxCtrl(double maxCtrl) {
//        this.maxCtrl = maxCtrl;
//    }
//
//    double getMinCtrl() {
//        return minCtrl;
//    }
//
//    void setMinCtrl(double minCtrl) {
//        this.minCtrl = minCtrl;
//    }
//
//    double getSetPoint() {
//        return setPoint;
//    }

    public void setSetPoint(double setPoint) {
        this.setPoint = setPoint;
    }

//    double getP() {
//        return p;
//    }
//
//    void setP(double p) {
//        this.p = p;
//    }
//
//    double getI() {
//        return i;
//    }
//
//    void setI(double i) {
//        this.i = i;
//    }
//
//    double getD() {
//        return d;
//    }
//
//    void setD(double d) {
//        this.d = d;
//    }

    public double getLastError() {
        return lastError;
    }

//    void setLastError(double lastError) {
//        this.lastError = lastError;
//    }
}
