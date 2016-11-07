/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.input.factories;

import mymath.DistributionFactory;
import swfjparser.GWFFile;
import swfjparser.SWFFile;
import swftasks.TaskInput;
import swftasks.input.DeterministicTaskInput;
import swftasks.input.HazardTaskInput;

/**
 *
 * @author HerrSergio
 */
public class DeterministicTaskInputFactory extends AbstractTaskInputFactory {

    private double cycle;
    private Boolean hazard;
    
    public DeterministicTaskInputFactory(Boolean hazard, double cycle, int... width) {
        super(null, width);
        this.cycle = cycle;
        this.hazard = hazard;
    }
    
    @Override
    protected TaskInput extract(GWFFile file, int minWidth, int maxWidth, DistributionFactory factory) {
        double d[][] = file.getIncome(minWidth, maxWidth);
        double m = file.getLastEvent();
        if(cycle > 0) {
            double mm = Math.ceil(m / cycle) * cycle;
            double ad = mm - m + file.getShift();
            if(!file.isAligned() && ad >= cycle) {
                m = mm - cycle;
            } else {
                m = mm;
            }
        }
        boolean norm = true;
        if(hazard == null || hazard) {
            double h[] = hazard == null ? file.getHazard(true) : file.getHazard(norm, minWidth, maxWidth);
            double weight = 0.0, last = 0.0;
            for(int i = 0; i < d.length; i++) {
                weight += SWFFile.getWeight(h, file.getStep(), last, d[i][0]);
                last = d[i][0];
                d[i][0] = weight;
            }
            weight += SWFFile.getWeight(h, file.getStep(), last, m);
            return new HazardTaskInput(
                    new DeterministicTaskInput(d, weight), 
                    h, 
                    file.getStep());
        }
        return new DeterministicTaskInput(d, m);
    }
    
}
