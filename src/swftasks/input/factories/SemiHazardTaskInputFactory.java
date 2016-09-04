/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.input.factories;

import java.util.Arrays;
import mymath.DistributionFactory;
import swfjparser.GWFFile;
import swftasks.input.HazardTaskInput;
import swftasks.input.SimpleTaskInput;
import swftasks.TaskInput;

/**
 *
 * @author HerrSergio
 */
public class SemiHazardTaskInputFactory extends AbstractTaskInputFactory {

    private double[] hazard;

    public SemiHazardTaskInputFactory(DistributionFactory factory, int... width) {
        super(factory, width);
    }   
    
    @Override
    protected void begin(GWFFile file) {
        super.begin(file); 
        hazard = file.getHazard(true);
    }

    @Override
    protected void end(GWFFile file) {
        super.end(file); 
        hazard = null;
    }
    
       
    @Override
    protected TaskInput extract(GWFFile file, int minWidth, int maxWidth, DistributionFactory factory) {
        double[] d = GWFFile.getWeights(hazard, file.getShift(), file.getIncomeTimes(minWidth, maxWidth), file.getLastEvent(), file.isAligned(),  
                file.getStep(), minWidth, maxWidth);
        //System.err.println(Arrays.stream(hazard).average());
        if (d.length != 0) {
            return new HazardTaskInput(
                    new SimpleTaskInput(file.getWidths(minWidth, maxWidth), factory.get(d)),
                    hazard,
                    file.getStep()
            );
        } else {
            return null;
        }
    }
    
    
}
