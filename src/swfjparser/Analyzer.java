/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swfjparser;

import distributions.Distribution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import swftasks.TaskComplicity;
import swftasks.TaskInput;
import swftasks.complicity.ComplicityDistribution;
import swftasks.complicity.DefaultRigidTaskComplicity;
import swftasks.input.CombinedTaskInput;
import swftasks.input.HazardTaskInput;
import swftasks.input.LimitedTaskInput;
import swftasks.input.NeverTaskInput;
import swftasks.input.SimpleTaskInput;

/**
 *
 * @author HerrSergio
 */
public class Analyzer {

    private Analyzer() {
    }

    public static double[] getDF(double[] datas, double len, int count, boolean cdf) {
        double step = len / count;
        double[] df = new double[count + 1];
        for (int i = 0; i < datas.length; i++) {
            int j = (int) (datas[i] / step);
            if (j < df.length) {
                df[j]++;
            }
        }
        if (cdf) {
            double buf = 0;
            for (int i = 0; i < df.length; i++) {
                buf += df[i];
                df[i] -= buf;
                df[i] = -df[i];
            }
        }
        for (int i = 0; i < df.length; i++) {
            df[i] /= datas.length;
        }
        return df;
    }
    
    public static double[][] getDFs(double[][] datas, double len, int count, boolean cdf) {
        return Arrays.stream(datas).map(data -> getDF(data, len, count, cdf)).toArray(double[][]::new);
    }
    
    public static double[][] getLegend(double[][] datas, double len, int count) {
        double[][] result = new double[datas.length + 1][];
        double step = len / count; 
        result[0] = new double[count + 1];
        for(int i = 0; i <= count; i++)
            result[0][i] = step * i;
        for(int i = 0; i < datas.length; i++)
            result[i + 1] = datas[i];
        return result;
    }
    
    public static double[] getDF(Distribution distr, double len, int count, boolean cdf) {
        double step = len / count;
        double[] d = new double[count + 1];
        for(int i = 0; i <= count; i++) {
            d[i] = distr.getDF(step * i, cdf);
        }
        return d;
    }
    
    public static String toCsv(double[][] d, String delim, String point) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < d[0].length; i++)
            for(int j = 0; j < d.length; j++) {
                String s = Double.toString(d[j][i]);
                s = s.replaceAll("\\.", point);
                builder.append(s);
                if(j != d.length - 1)
                    builder.append(delim);
                else
                    builder.append(System.lineSeparator());
            }
        return builder.toString();
    }
    
    public static String toCsv(double[][] d) {
        return toCsv(d, ";", ",");
    }
    
    public static double[][] compare(double[] d, double len, int count, boolean cdf, Distribution... distrs) {
        double[][] h = new double[1 + distrs.length][];
        h[0] = Analyzer.getDF(d, len, count, cdf);
        for(int i = 0; i < distrs.length; i++)
            h[1 + i] = Analyzer.getDF(distrs[i], len, count, cdf);
        h = Analyzer.getLegend(h, len, count);
        return h;
    }
    
    public static SimpleTaskInput getSimpleTaskInput(Collection<SimpleTaskInput> inputs, int minWidth, int maxWidth) {
        //maxWidth ++;
        //minWidth ++;
        for(SimpleTaskInput input : inputs)
            if(input.getWidth().length-1 >= minWidth && input.getWidth().length-1 <= maxWidth)
                return input;
        return null;
    }
    
    public static List<SimpleTaskInput> getSimpleTaskInputs(Collection<HazardTaskInput> inputs) {
        return inputs.stream().map(h -> (SimpleTaskInput)h.getInput()).collect(Collectors.toList());
    }
    
    public static List<HazardTaskInput> getHazardTaskInputs(TaskInput input) {
        if(input instanceof CombinedTaskInput) {
            return new ArrayList<>((List<HazardTaskInput>)(Object)(((CombinedTaskInput) input).getTaskInputs()));
        } else {
            return Arrays.asList((HazardTaskInput)input);
        }        
    }
    
    public static List<SimpleTaskInput> getSimpleFromHazardTaskInputs(TaskInput input) {
        return getSimpleTaskInputs(getHazardTaskInputs(input));
                
    }
    
    public static SimpleTaskInput getSimpleFromHazardTaskInputs(TaskInput input, int minWidth, int maxWidth) {
        return getSimpleTaskInput(getSimpleFromHazardTaskInputs(input), minWidth, maxWidth);
    }
    
    public static List<SimpleTaskInput> getSimpleTaskInputs(TaskInput input) {
        if(input instanceof CombinedTaskInput) {
            return new ArrayList<>((List<SimpleTaskInput>)(Object)(((CombinedTaskInput) input).getTaskInputs()));
        } else {
            return Arrays.asList((SimpleTaskInput)input);
        }        
    }
    
    public static SimpleTaskInput getSimpleTaskInputs(TaskInput input, int minWidth, int maxWidth) {
        return getSimpleTaskInput(getSimpleTaskInputs(input), minWidth, maxWidth);
    }
    
    public static List<SimpleTaskInput> getBothTaskInputs(TaskInput input) {
        ArrayList<SimpleTaskInput> arr = new ArrayList<>();
        if(input instanceof CombinedTaskInput) {
            for(TaskInput ti : ((CombinedTaskInput) input).getTaskInputs())
                arr.addAll(getBothTaskInputs(ti));
        } else if(input instanceof HazardTaskInput) {
             arr.addAll(getBothTaskInputs(((HazardTaskInput) input).getInput()));
        } else if(input instanceof LimitedTaskInput) {
            arr.addAll(getBothTaskInputs(((LimitedTaskInput) input).getTaskInput()));
        } else if(input instanceof SimpleTaskInput) {
            arr.add((SimpleTaskInput) input);
        } else if(input instanceof NeverTaskInput) {
            
        } else {
            throw new IllegalArgumentException();
        }
        return arr;
    }
    
    public static SimpleTaskInput getBothTaskInputs(TaskInput input, int minWidth, int maxWidth) {
        return getSimpleTaskInput(getBothTaskInputs(input), minWidth, maxWidth);
    }
    
    public static Map<Integer, ComplicityDistribution> getRigidTaskComplicityDistribution(TaskComplicity complicity) {
        return ((DefaultRigidTaskComplicity)complicity).getDistributions();
    }
    
    public static double[][] compareInputs(double[] d, TaskInput input,
            int minWidth, int maxWidth, double len, int count, boolean cdf) {
        Distribution distr = getBothTaskInputs(input, minWidth, maxWidth).getDistribution();
        return  Analyzer.compare(d, len, count, cdf, distr);
    }
    
    public static double[][] compareComplicity(double[] d, TaskComplicity complicity,
            int minWidth, int maxWidth, double len, int count, boolean cdf) {
        Distribution distr = getRigidTaskComplicityDistribution(complicity).get(maxWidth).getDistribution();
        return Analyzer.compare(d, len, count, cdf, distr);
    }
     
    public static int[] widths() {
        int[] r = new int[61];
        for(int i = 1; i < 31; i++) {
            r[(i - 1) << 1] = (1 << i) - 1;
            r[((i - 1) << 1) | 1] = 1 << i;
        }
        r[60] = Integer.MAX_VALUE;
        return r;
    } 
}
