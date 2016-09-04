/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks;

import java.io.Serializable;
import swfjparser.GWFFile;

/**
 *
 * @author HerrSergio
 */
public interface TaskStreamFactory extends Serializable {
    TaskStream get(GWFFile file);
}
