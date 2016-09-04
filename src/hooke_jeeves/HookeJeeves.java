/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hooke_jeeves;

import java.util.Arrays;

/**
 *
 * @author HerrSergio
 */
public class HookeJeeves {
    private HookeJeeves() {
        
    }
    
    public static double[] minimize(Function function, double[] first, double[] step, int count) {
        if(function.argsCount() != first.length || first.length != step.length)
            throw new IllegalArgumentException();
        
        double newValue[] = new double[2]; 
        
        for (;;) {

            double[] second = checkAround(function, first, step, newValue);
            double value = newValue[1];

            if (value < newValue[0]) {
                for (;;) {
                    double[] third = goFurther(first, second);
                    double[] forth = checkAround(function, third, step, newValue);
                    if (newValue[1] < value) {
                        first = second;
                        second = forth;
                        value = newValue[1];
                    } else {
                        first = second;
                        break;
                    }
                }
            } else if(count > 0) {
                reduceStep(step);
                count --;
            } else {
                break;
            }
        }
        return first;
    }
    
    private static double[] checkAround(Function function, double[] point, double[] step, double[] outValue) {
        double value = function.get(point);
        double[] current = point;
        if(outValue != null)
            outValue[0] = value;
        for(int i = 0; i < function.argsCount(); i++) {
            double buf = point[i];
            
            point[i] += step[i];
            double newValue = function.get(point);
            if(newValue < value) {
                value = newValue;
                current = Arrays.copyOf(point, point.length);
            }
            
            point[i] = buf - step[i];
            newValue = function.get(point);
            if(newValue < value) {
                value = newValue;
                current = Arrays.copyOf(point, point.length);
            }
            
            point[i] = buf;
        }
        if(outValue != null)
            outValue[1] = value;
        return current;
    }
    
    private static void reduceStep(double[] step) {
        for(int i = 0; i < step.length; i++)
            step[i] /= 2;
    }
    
    private static double[] goFurther(double[] first, double[] second) {
        double[] third = Arrays.copyOf(second, second.length);
        for(int i = 0; i < first.length; i++) {
            third[i] += second[i] - first[i];
        }
        return third;
    }
}
