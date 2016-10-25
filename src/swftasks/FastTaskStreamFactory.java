/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks;

import swftasks.TaskStream;

/**
 *
 * @author HerrSergio
 */
public interface FastTaskStreamFactory<T extends TaskStream>  {
    T get();
}
