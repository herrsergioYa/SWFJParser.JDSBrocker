/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swfjparser;

/**
 *
 * @author HerrSergio
 */
public interface DataFactory {
    double[] get(GWFFile file, int minWidth, int maxWidth);
}
