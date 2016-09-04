/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mymath;

import java.util.Collection;

/**
 *
 * @author HerrSergio
 */
public class Statistic {

    private Statistic() {

    }

    public static double getMomentum(double[] datas, double order, double center, boolean abs) {
        if (datas.length < 1) {
            throw new IllegalArgumentException();
        }
        double sum = 0.0;
        if (abs) {
            for (double number : datas) {
                sum += Math.pow(Math.abs(number - center), order);
            }
        } else {

            for (double number : datas) {
                sum += Math.pow(number - center, order);
            }
        }
        return sum / datas.length;
    }

    public static double getMean(double[] datas) {
        return getMomentum(datas, 1, 0, false);
    }

    public static double getVariance(double[] datas) {
        return getMomentum(datas, 2, getMean(datas), false);
    }

    public static double getUnbiasedVariance(double[] datas) {
        if (datas.length < 2) {
            return 0.0;
        }
        return getVariance(datas) * datas.length / (datas.length - 1.0);
    }

  

   
}
