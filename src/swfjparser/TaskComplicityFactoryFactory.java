/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swfjparser;

import mymath.DistributionFactory;
import swftasks.TaskComplicityFactory;

/**
 *
 * @author HerrSergio
 */
public interface TaskComplicityFactoryFactory {
    TaskComplicityFactory get(DistributionFactory distributionFactory, int[] widths); 
}
