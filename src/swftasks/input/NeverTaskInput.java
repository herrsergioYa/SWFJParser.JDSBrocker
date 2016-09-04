/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.input;

import swftasks.TaskInput;
import java.util.Random;

/**
 *
 * @author HerrSergio
 */
public class NeverTaskInput implements TaskInput {

    @Override
    public double getTime() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public int getCategory() {
        return 1;
    }

    @Override
    public boolean next(Random random) {
        return has();
    }

    @Override
    public boolean has() {
        return false;
    }
    
}
