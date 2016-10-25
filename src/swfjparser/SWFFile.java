/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swfjparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author HerrSergio
 */
public class SWFFile extends GWFFile {
    private ArrayList<double[]> data;
    
    protected SWFFile(ArrayList<double[]> data) {
        this.data = data;
    }
    
    private static final int minSize = 5; 
    
    public static SWFFile load(Reader reader) throws IOException {
        try(BufferedReader bufferedReader = new BufferedReader(reader)) {
            ArrayList<double[]> data = new ArrayList<>();
            for(String row = bufferedReader.readLine(); row != null; row = bufferedReader.readLine()) {
                row = row.trim();
                if(row.length() == 0 || row.charAt(0) == ';')
                    continue;
                String[] cells = row.split("\\s+");
                double[] datas = Arrays.stream(cells).mapToDouble(e -> Double.parseDouble(e)).toArray();
                if(datas.length < minSize || !isOk(datas))
                    continue;
                data.add(datas);
            }
            return new SWFFile(data);
        }
    }
    
    public SWFFile filter() {
        return new SWFFile(new ArrayList<>(data.stream().filter(e -> isOk(e)).collect(Collectors.toList())));
    }
    
    protected static boolean isOk(double[] d) {
        return d[1] >= 0 && d[3] >= 0 && d[4] > 0;
    }
    
    protected double get(int row, int column) {
        return data.get(row)[column];
    }
    
    @Override
    public double getTime(int row) {
        return get(row, 1);
    }
    
        
    @Override
    public int getWidth(int row) {
        return (int)Math.round(get(row, 4));
    }
    
    @Override
    public double getLength(int row) {
        return get(row, 3);
    }
    
    public double getSquare(int row) {
        return getLength(row) * getWidth(row);
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }
    
}
