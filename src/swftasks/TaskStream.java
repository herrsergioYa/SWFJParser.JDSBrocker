/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks;

import java.io.Serializable;
import java.util.Random;
import swftasks.Task;

/**
 *
 * @author HerrSergio
 */
public interface TaskStream extends Serializable {
    Task get();
    void next(Random random);
}
