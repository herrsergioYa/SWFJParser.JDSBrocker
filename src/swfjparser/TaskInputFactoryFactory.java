/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swfjparser;

import mymath.DistributionFactory;
import swftasks.TaskInputFactory;

/**
 *
 * @author HerrSergio
 */
public interface TaskInputFactoryFactory {
    TaskInputFactory get(DistributionFactory distributionFactory, int[] widths); 
}
