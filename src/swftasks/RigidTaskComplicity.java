/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author HerrSergio
 */
public interface RigidTaskComplicity extends Serializable, TaskComplicity {
    double get(Random random, int width, boolean square); 
    
    default RigidTask get(double incomeTime, int category, Random random) {
        return new RigidTask(incomeTime, get(random, category, true), category);
    }
    
}
