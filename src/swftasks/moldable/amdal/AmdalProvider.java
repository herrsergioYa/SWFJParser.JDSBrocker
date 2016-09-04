/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable.amdal;

import distributions.GammaDistribution;
import java.util.Random;
import swftasks.RigidTask;
import swftasks.Task;
import swftasks.moldable.MoldabilityProvider;

/**
 *
 * @author HerrSergio
 */
public class AmdalProvider implements MoldabilityProvider {

    private double seq = -1.0;

    public AmdalProvider(double seq) {
        this.seq = seq;
    }

    public AmdalProvider() {
    }   
    
    
    @Override
    public Task getTask(RigidTask task, int minWidth, int maxWidth, Random random) {
        double seq = this.seq;
        if (seq < 0 || seq > 1) {
            seq = Math.abs(random.nextDouble() - random.nextDouble());
        } else {
            seq = GammaDistribution.getBeta(random, seq);
        }
        return new AmdalMoldableTask(task.getIncomeTime(), minWidth, maxWidth, task.getLength(), task.getWidth(), seq);
    }
    
}
