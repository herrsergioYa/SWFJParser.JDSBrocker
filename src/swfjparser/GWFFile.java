/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swfjparser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author HerrSergio
 */
public abstract class GWFFile {

    public static double getWeight(double[] hazard, double step, double from, double to) {
        if (from > to) {
            return getWeight(hazard, step, to, from);
        }
        int k = (int) Math.floor(from / step);
        int l = (int) Math.floor(to / step);
        double value = 0.0;
        while (k < l) {
            int i = k++;
            double buf = k * step;
            value += (buf - from) * hazard[i % hazard.length];
            from = buf;
        }
        value += (to - from) * hazard[k % hazard.length];
        return value;
    }

    public static double getTime(double[] hazard, double step, double from, double value) {
        int k = (int) Math.floor(from / step);
        while (true) {
            double buf = (k + 1) * step;
            double sub = (buf - from) * hazard[k % hazard.length];
            if (sub > value) {
                return value / sub * (buf - from) + from;
            }
            value -= sub;
            from = buf;
            k++;
        }
    }

    public static double[] getWeights(double[] hazard, double from, double[] incomeTimes, double to, boolean aligned, double step, int minWidth, int maxWidth) {
        double[] values = new double[incomeTimes.length];
        int i = 0;
        double time = 0.0;
        for (double d : incomeTimes) {
            if (d > to || d < time || d < from) {
                throw new IllegalArgumentException();
            }
            values[i++] = getWeight(hazard, step, time, d);
            time = d;
        }
        if (values.length != 0) {
            if(aligned) {
                double period = step * hazard.length;
                double buf = Math.ceil(to / period) * period;
                values[0] += getWeight(hazard, step, time, buf);
            } else {
                values[0] += getWeight(hazard, step, time, to);
                values[0] -= getWeight(hazard, step, 0.0, from);
            }
        }
        return values;
    }

    public static double[] getWeights(double[] hazard, double from, double[] incomeTimes, double to, boolean aligned, double step) {
        return getWeights(hazard, from, incomeTimes, to, aligned, step, 0, Integer.MAX_VALUE);
    }

    public GWFFile() {
    }

    public abstract double getTime(int row);

    public double getIncomeTime(int row) {
        return getTime(row) + getShift();
    }
    
    public double getIncomeTime(int row, double shift) {
        return getTime(row) + shift;
    }

    public abstract int getWidth(int row);

    public abstract double getLength(int row);

    public abstract double getSquare(int row);

    public abstract int getRowCount();

    public double[] getIntervals(double shift, double to, boolean aligned, double period, int minWidth, int maxWidth) {
        double time = 0.0;
        double[] values = new double[getRowCount()];
        int j = 0;
        for (int i = 0; i < getRowCount(); i++) {
            double buf = getIncomeTime(i, shift);
            int width = getWidth(i);
            if(buf < time || buf > to)
                throw new IllegalArgumentException();
            if(width < minWidth || width > maxWidth)
                continue;
            values[j++] = buf - time;
            time = buf;
        }
        if (j > 0) {
            if(aligned) {
                double buf = Math.ceil(to / period) * period - time;
                values[0] += buf;
            } else {
                values[0] += to - time;
                values[0] -= shift;
            }
        }
        return Arrays.copyOf(values, j);
    }

    public double[] getIntervals(int minWidth, int maxWidth) {
        return getIntervals(getShift(), getLastEvent(), isAligned(), getPeriod(), minWidth, maxWidth);
    }

    public double[] getIntervals(double shift, double to, boolean aligned, double period) {
        return getIntervals(shift, to, aligned, period, 0, Integer.MAX_VALUE);
    }

    public double[] getIntervals() {
        return getIntervals(0, Integer.MAX_VALUE);
    }

    public double[] getIncomeTimes(double shift, int minWidth, int maxWidth) {
        double[] values = new double[getRowCount()];
        int j = 0;
        for (int i = 0; i < getRowCount(); i++) {
            double buf = getIncomeTime(i, shift);
            int width = getWidth(i);
            if(width < minWidth || width > maxWidth)
                continue;
            values[j++] = buf;
        }
        return Arrays.copyOf(values, j);
    }

    public double[] getIncomeTimes(int minWidth, int maxWidth) {
        return getIncomeTimes(getShift(), minWidth, maxWidth);
    }

    public double[] getLengths(int minWidth, int maxWidth) {
        double[] values = new double[getRowCount()];
        int j = 0;
        for (int i = 0; i < getRowCount(); i++) {
            double buf = getLength(i);
            int width = getWidth(i);
            if(width < minWidth || width > maxWidth)
                continue;
            values[j++] = buf;
        }
        return Arrays.copyOf(values, j);
    }

    public double[] getSquares(int minWidth, int maxWidth) {
        double[] values = new double[getRowCount()];
        int j = 0;
        for (int i = 0; i < getRowCount(); i++) {
            double buf = getSquare(i);
            int width = getWidth(i);            
            if(width < minWidth || width > maxWidth)
                continue;
            values[j++] = buf;
        }
        return Arrays.copyOf(values, j);
    }

    public double[] getWidths(int minWidth, int maxWidth) {
        ArrayList<Double> values = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < getRowCount(); i++) {
            int width = getWidth(i);
            if(width < minWidth || width > maxWidth)
                continue;
            while (width >= values.size()) {
                values.add(0.0);
            }
            values.set(width, values.get(width) + 1.0);
            sum++;
        }
        for (int i = 0; i < values.size(); i++) {
            values.set(i, values.get(i) / sum);
        }
        return values.stream().mapToDouble((Double e) -> (double) e).toArray();
    }

    public double[] getWidths() {
        return getWidths(0, Integer.MAX_VALUE);
    }

    public double[] getIncomeTimes(double shift) {
        return getIncomeTimes(shift, 0, Integer.MAX_VALUE);
    }

    public double[] getIncomeTimes() {
        return getIncomeTimes(getShift());
    }

    public double[] getLengths() {
        return getLengths(0, Integer.MAX_VALUE);
    }

    public double[] getSquares() {
        return getSquares(0, Integer.MAX_VALUE);
    }

    /*public double[] getHazard(double shift, double period, int count,  boolean norm) {
        return getHazard(shift, period, count, norm, 0, Integer.MAX_VALUE);
    }

    public double[] getHazard(boolean norm) {
        return getHazard(getShift(), getPeriod(), getCount(), norm);
    }*/
    public double[] getHazard(double shift, double period, double to, boolean aligned, int count,
            boolean norm, int minWidth, int maxWidth) {
        return getHazard(shift, getIncomeTimes(shift, minWidth, maxWidth), to, aligned, period,
                count, norm, minWidth, maxWidth);
    }

    public double[] getHazard(double shift, double period, double to, boolean aligned, int count,
            boolean norm) {
        return getHazard(shift, period, to, aligned, count, norm, 0, Integer.MAX_VALUE);
    }

    public double[] getHazard(boolean norm, int minWidth, int maxWidth) {
        return getHazard(getShift(), getPeriod(), getLastEvent(), isAligned(),
                getCount(), norm, minWidth, maxWidth);
    }

    public double[] getHazard(boolean norm) {
        return getHazard(norm, 0, Integer.MAX_VALUE);
    }

    public static double[] getHazard(double from, double[] incomeTimes, double to, boolean aligned, double period, int count,
            boolean norm) {
        return getHazard(from, incomeTimes, to, aligned, period, count, norm, 0, Integer.MAX_VALUE);
    }

    public static double[] getHazard(double from, double[] incomeTimes, double to, boolean aligned, double period, int count,
            boolean norm, int minWidth, int maxWidth) {
        double[] hazard = new double[count];
        double step = period / count;
        for (double d : incomeTimes) {
            if (d < from || d > to) {
                throw new IllegalArgumentException();
            }
            int k = (int) Math.floor(d / step);
            hazard[k % count] += 1;
        }
        int k = (int) Math.floor(to / step);
        int periods = k / count;
        int remain = k % count;
        int sub = (int)Math.floor(from / step);
        double t = Math.ceil(to / period);
        double T = t * period;
        double mean = aligned ? 
                T / incomeTimes.length :
                (to - from) / incomeTimes.length;
        for (int i = 0; i < count; i++) {
            if(aligned) {
                hazard[i] /= step * t;
            } else {
                hazard[i] /= step;
                int l = periods;
                if (i <= remain) {
                    l++;
                }
                if (i < sub) {
                    l--;
                }
                hazard[i] /= Math.max(1, l);
            }
            if (norm) {
                hazard[i] *= mean;
            }
        }
        return hazard;
    }

    /*public double[] getHazard(double shift, double period, int count, boolean norm, int minWidth, int maxWidth) {
        double[] hazard = new double[count];
        if(getRowCount() == 0)
            return hazard;
        double time = shift, step = period / count;
        double[] incomeTimes = getIncomeTimes(shift, minWidth, maxWidth);
        for(double d : incomeTimes) {
            if(d < time)
                throw new IllegalArgumentException();
            time = d;
            int k = (int)Math.floor(time / step);
            hazard[k % count] += 1;
        }
        time = getTime(getRowCount() - 1) + shift;
        int k = (int)Math.floor(time / step);
        int periods = k / count;
        int remain = k % count;
        int sub = (int)Math.floor(shift / step);
        double mean = (time - shift) / incomeTimes.length;
        for(int i = 0; i < count; i++) {
            hazard[i] /= step;
            int l = periods;
            if(i <= remain)
                l ++;
            if(i < sub)
                l--;
            hazard[i] /= Math.max(1, l);
            if(norm) {
                hazard[i] *= mean;
            }
        }
        return hazard;
    }
    
    public double[] getHazard(boolean norm, int minWidth, int maxWidth) {
        return getHazard(getShift(), getPeriod(), getCount(), norm, minWidth, maxWidth);
    }*/
    public double getMaxTime() {
        double result = 0.0;
        for (int i = 0; i < getRowCount(); i++) {
            result = Math.max(result, getTime(i));
        }
        return result;
    }
    
    public double getMaxIncomeTime() {
        return getMaxTime() + getShift();
    }

    public double getMaxIncomeTime(double shift) {
        return getMaxTime() + shift;
    }
    
    private double minLogLength;

    public double getMinLogLength() {
        return minLogLength;
    }

    public void setMinLogLength(double minLogLength) {
        this.minLogLength = minLogLength;
    }    
    
    public double getLastEvent() {
        return Math.max(getMaxIncomeTime(), getMinLogLength());
    }

    public double[][] getWeights(double shift, double to, boolean aligned, double period, int count, boolean norm, int minWidth, int maxWidth) {
        double[][] result = new double[4][];
        result[2] = getIncomeTimes(shift, minWidth, maxWidth);
        result[3] = new double[]{period / count};
        result[0] = getHazard(shift, result[2],
                to, aligned, period, count, norm, minWidth, maxWidth);
        result[1] = getWeights(result[0], shift, result[2], to, aligned, result[3][0], minWidth, maxWidth);
        return result;
    }

    public double[][] getWeights(boolean norm, int minWidth, int maxWidth) {
        return getWeights(getShift(), getLastEvent(), isAligned(), getPeriod(), getCount(), norm, minWidth, maxWidth);
    }

    public double[][] getWeights(double shift, double to, boolean aligned, double period, int count, boolean norm) {
        return getWeights(shift, to, aligned, period, count, norm, 0, Integer.MAX_VALUE);
    }

    public double[][] getWeights(boolean norm) {
        return getWeights(norm, 0, Integer.MAX_VALUE);
    }

    private double period = 3600.0 * 24.0 * 7.0;
    private double shift = 0.0;
    private int count = 48 * 7;
    private boolean aligned;

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        if (period <= 0.0) {
            throw new IllegalArgumentException();
        }
        this.period = period;
    }

    public double getShift() {
        return shift;
    }

    public void setShift(double shift) {
        if (shift < 0.0) {
            throw new IllegalArgumentException();
        }
        this.shift = shift;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        if (count <= 0.0) {
            throw new IllegalArgumentException();
        }
        this.count = count;
    }

    public double getStep() {
        return this.period / this.count;
    }

    public boolean isAligned() {
        return aligned;
    }

    public void setAligned(boolean aligned) {
        this.aligned = aligned;
    }

    
}
