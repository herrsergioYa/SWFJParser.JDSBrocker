/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable.amdal;

import distributions.GammaDistribution;
import java.util.Random;
import swftasks.Task;
import swftasks.moldable.AbstractMoldableTaskComplicity;
import swftasks.RigidTaskComplicity;

/**
 *
 * @author HerrSergio
 */
@Deprecated
public class AmdalMoldableTaskComplicity extends AbstractMoldableTaskComplicity {

    private double seq = -1.0;
    
    public AmdalMoldableTaskComplicity(RigidTaskComplicity taskComplicity, int minWdith, int maxWidth, double seq) {
        super(taskComplicity, minWdith, maxWidth);
        this.seq = seq;
    }

    public AmdalMoldableTaskComplicity(RigidTaskComplicity taskComplicity, double seq) {
        super(taskComplicity);
        this.seq = seq;
    }

    public AmdalMoldableTaskComplicity(RigidTaskComplicity taskComplicity, int minWdith, int maxWidth) {
        super(taskComplicity, minWdith, maxWidth);
    }

    public AmdalMoldableTaskComplicity(RigidTaskComplicity taskComplicity) {
        super(taskComplicity);
    }
    

    @Override
    public AmdalMoldableTask get(double incomeTime, int category, Random random) {
        double length = getLength(random, category);
        double seq = this.seq;
        if (seq < 0 || seq > 1) {
            seq = Math.abs(random.nextDouble() - random.nextDouble());
        } else {
            seq = GammaDistribution.getBeta(random, seq);
        }
        return new AmdalMoldableTask(incomeTime, getMinWidth(), getMaxWidth(), length, category, seq);
    }

}
