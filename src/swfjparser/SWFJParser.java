/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swfjparser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;
import mymath.Approx;
import mymath.DistributionFactory;
import mymath.GammaLikelihoodFactory;
import mymath.LikelihoodFactory;
import mymath.MarkovianFactory;
import serialization.IOHelper;
import swftasks.RigidTaskComplicityFactory;
import swftasks.complicity.factories.LengthTaskComplicityFactory;
import swftasks.RigidTask;
import swftasks.Task;
import swftasks.TaskComplicityFactory;
import swftasks.input.factories.SemiHazardTaskInputFactory;
import swftasks.TaskInputFactory;
import swftasks.complicity.factories.SquareTaskComplicityFactory;
import swftasks.moldable.DefaultMoldableTaskFactory;
import swftasks.moldable.amdal.AmdalMoldableTaskComplicityFactory;
import swftasks.moldable.amdal.AmdalProvider;
import swftasks.moldable.ideal.IdealMoldableTaskComplicityFactory;
import swftasks.moldable.ideal.IdealProvider;
import swftasks.streams.DefaultTaskStream;
import swftasks.streams.factories.DefaultTaskStreamFactory;

/**
 *
 * @author HerrSergio
 */
public class SWFJParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        // TODO code application logic here
   
                

        SWFFile fFile = null;
        try(Reader reader = new FileReader("CTC-SP2-1995-2.swf")) {
                //"1.swf")) {
            fFile = SWFFile.load(reader);
        }
     
        DistributionFactory factory = //new GammaMomentumFactory();
                new MarkovianFactory();
                new GammaLikelihoodFactory();
                new LikelihoodFactory(new Approx.HyperGammaLikelihoodFunction(2));
                
        TaskInputFactory tif = new SemiHazardTaskInputFactory(factory, 4);
        TaskComplicityFactory tcf
                = //new SquareTaskComplicityFactory(factory, 32);
                new DefaultMoldableTaskFactory(new SquareTaskComplicityFactory(factory, 32), 1, 128,
                        new IdealProvider()
                );
//new IdealMoldableTaskComplicityFactory(new LengthTaskComplicityFactory(factory, 32), 1, 128);
      
    /********    BinarySerializer.serialize(tif.get(fFile), "input.bin");
        BinarySerializer.serialize(tcf.get(fFile), "comp.bin");**********/
      
      Random random = new Random(0);
     
      DefaultTaskStreamFactory tsf = new DefaultTaskStreamFactory(tif, tcf);
      DefaultTaskStream ts = tsf.get(fFile);
      ts.next(random);
      Task task = ts.get();
      double cnt = 1_000_000;
      CustomSWFFile cswff = new CustomSWFFile();
      for(int i = 0; i < cnt; i++) {
          cswff.add(task.toRigidTask(random));
          System.out.println(task.getIncomeTime() + " - " + task.getMinWidth() + "..." + task.getMaxWidth() + " => " + task.getLength(task.getMinWidth()) + "..." + task.getLength(task.getMaxWidth()));
          ts.next(random);
          task = ts.get();
      }
      
        //IOHelper.write(cswff.toString(), "m.swf");
      
      
      System.err.println(task.getIncomeTime() / (cnt + 1));
      
      //*************************8
      //BinarySerializer.write(cswff.toString(), "my.swf");
      
      
        
    }
    
}
