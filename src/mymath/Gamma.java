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

    public static double gamma(double nu) {
        if (nu < 0) {
            return -Math.PI / Math.sin(Math.PI * nu) / gamma(-nu) / nu;
        }

        if (nu == 0.0) {
            return 1.0 / nu;
        }

        if (nu >= Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        if (nu <= Double.NEGATIVE_INFINITY) {
            return Double.NaN;
        }

        double mult = 1.0;

        while (nu > 2) {
            nu -= 1.0;
            mult *= nu;
        }

        if (nu < 1) {
            mult /= nu;
            nu += 1;
        }

        double a = 0.0;
        double b = 1.0;

        double z = nu - 1.0;

        for (int i = 0; i < 8; i++) {
            a = (a + p[i]) * z;
            b = b * z + q[i];
        }

        return (a / b + 1.0) * mult;

    }

    public static double loggamma(double nu) {
        if (nu < 0) {
            return Math.log(Math.PI / Math.abs(Math.sin(Math.PI * nu))) - loggamma(-nu) - Math.log(-nu);
        }

        if (nu == 0.0) {
            return 1.0 / nu;
        }

        if (nu >= Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        if (nu <= Double.NEGATIVE_INFINITY) {
            return Double.NaN;
        }

        double addititon = 0.0;

        while (nu > 2) {
            nu -= 1.0;
            addititon += Math.log(nu);
        }

        if (nu < 1) {
            addititon -= Math.log(nu);
            nu += 1.0;
        }

        double a = 0.0;
        double b = 1.0;

        double z = nu - 1.0;

        for (int i = 0; i < 8; i++) {
            a = (a + p[i]) * z;
            b = b * z + q[i];
        }

        return Math.log(a / b + 1.0) + addititon;

    }
    
    public static double digamma(double nu) {
        
        if(nu < 0) {
            return digamma(1.0 - nu) - Math.PI * Math.cos(Math.PI * nu) / Math.sin(Math.PI * nu);
        }
        
        double sub = 0.0;

        while (nu < 7) {
            sub += 1 / nu;
            nu += 1;
        }

        double x1 = 1 / nu;
        double x2 = x1 * x1;
        double x4 = x2 * x2;
        double x6 = x4 * x2;
        double x8 = x4 * x4;
        double x10 = x8 * x2;
        double x12 = x8 * x4;
        double x14 = x8 * x6;

        return Math.log(nu) - 1.0 / 2.0 * x1 - 1.0 / 12 * x2 + 1.0 / 120 * x4
                - 1.0 / 252 * x6 + 1.0 / 240 * x8 - 5.0 / 660 * x10 + 691.0 / 32760 * x12 - 1.0 / 12 * x14
                - sub;

    }
    
    public static double lower_gamma(double nu, double x, boolean norm) {
        if(nu <= 0)
            throw new IllegalArgumentException();
        double denom = nu, nom = 1.0;
        if(norm)
            denom *= gamma(nu);
        double sum = 0.0;
        for(int i = 0;;) {
            double buf = sum;
            sum += nom / denom;
            if(buf == sum)
                break;
            nom *= x;
            denom *= ++i + nu;
        }
        return Math.pow(x, nu) * Math.exp(-x) * sum;
    }
}
