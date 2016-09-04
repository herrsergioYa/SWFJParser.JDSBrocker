/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mymath;

/**
 *
 * @author HerrSergio
 */
public class Gamma {
    private Gamma() {
        
    }
    
    private static final double p[] = {-1.71618513886549492533811e+0,
        2.47656508055759199108314e+1,
        -3.79804256470945635097577e+2,
        6.29331155312818442661052e+2,
        8.66966202790413211295064e+2,
        -3.14512729688483675254357e+4,
        -3.61444134186911729807069e+4,
        6.64561438202405440627855e+4};

    private static final double q[] = {-3.08402300119738975254353e+1,
        3.15350626979604161529144e+2,
        -1.01515636749021914166146e+3,
        -3.10777167157231109440444e+3,
        2.25381184209801510330112e+4,
        4.75584627752788110767815e+3,
        -1.34659959864969306392456e+5,
        -1.15132259675553483497211e+5};

    public static double gamma(double val) {
        if (val < 0) {
            return -Math.PI / Math.sin(Math.PI * val) / gamma(-val) / val;
        }

        if (val == 0.0) {
            return 1.0 / val;
        }

        if (val >= Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        if (val <= Double.NEGATIVE_INFINITY) {
            return Double.NaN;
        }

        double mult = 1.0;

        while (val > 2) {
            val -= 1.0;
            mult *= val;
        }

        if (val < 1) {
            mult /= val;
            val += 1;
        }

        double a = 0.0;
        double b = 1.0;

        double z = val - 1.0;

        for (int i = 0; i < 8; i++) {
            a = (a + p[i]) * z;
            b = b * z + q[i];
        }

        return (a / b + 1.0) * mult;

    }

    public static double loggamma(double val) {
        if (val < 0) {
            return Math.log(Math.PI / Math.abs(Math.sin(Math.PI * val))) - loggamma(-val) - Math.log(-val);
        }

        if (val == 0.0) {
            return 1.0 / val;
        }

        if (val >= Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        if (val <= Double.NEGATIVE_INFINITY) {
            return Double.NaN;
        }

        double addititon = 0.0;

        while (val > 2) {
            val -= 1.0;
            addititon += Math.log(val);
        }

        if (val < 1) {
            addititon -= Math.log(val);
            val += 1.0;
        }

        double a = 0.0;
        double b = 1.0;

        double z = val - 1.0;

        for (int i = 0; i < 8; i++) {
            a = (a + p[i]) * z;
            b = b * z + q[i];
        }

        return Math.log(a / b + 1.0) + addititon;

    }
    
    public static double digamma(double x) {
        
        if(x < 0) {
            return digamma(1.0 - x) - Math.PI * Math.cos(Math.PI * x) / Math.sin(Math.PI * x);
        }
        
        double sub = 0.0;

        while (x < 7) {
            sub += 1 / x;
            x += 1;
        }

        double x1 = 1 / x;
        double x2 = x1 * x1;
        double x4 = x2 * x2;
        double x6 = x4 * x2;
        double x8 = x4 * x4;
        double x10 = x8 * x2;
        double x12 = x8 * x4;
        double x14 = x8 * x6;

        return Math.log(x) - 1.0 / 2.0 * x1 - 1.0 / 12 * x2 + 1.0 / 120 * x4
                - 1.0 / 252 * x6 + 1.0 / 240 * x8 - 5.0 / 660 * x10 + 691.0 / 32760 * x12 - 1.0 / 12 * x14
                - sub;

    }
}
