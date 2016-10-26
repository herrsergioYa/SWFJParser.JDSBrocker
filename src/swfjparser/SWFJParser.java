/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swfjparser;

import distributions.Distribution;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import mymath.Approx;
import mymath.DistributionFactory;
import mymath.GammaLikelihoodFactory;
import mymath.GammaMomentumFactory;
import mymath.LikelihoodFactory;
import mymath.MarkovianFactory;
import serialization.IOHelper;
import swftasks.FastTaskStreamFactory;
import swftasks.RigidTaskComplicityFactory;
import swftasks.complicity.factories.LengthTaskComplicityFactory;
import swftasks.RigidTask;
import swftasks.Task;
import swftasks.TaskComplicityFactory;
import swftasks.input.factories.SemiHazardTaskInputFactory;
import swftasks.TaskInputFactory;
import swftasks.complicity.DefaultRigidTaskComplicity;
import swftasks.complicity.factories.SquareTaskComplicityFactory;
import swftasks.input.CombinedTaskInput;
import swftasks.input.HazardTaskInput;
import swftasks.input.SimpleTaskInput;
import swftasks.input.factories.HazardTaskInputFactory;
import swftasks.moldable.DefaultMoldableTaskFactory;
import swftasks.moldable.amdal.AmdalMoldableTaskComplicityFactory;
import swftasks.moldable.amdal.AmdalProvider;
import swftasks.moldable.ideal.IdealMoldableTaskComplicityFactory;
import swftasks.moldable.ideal.IdealProvider;
import swftasks.streams.DefaultFastTaskStreamFactory;
import swftasks.streams.DefaultTaskStream;
import swftasks.streams.factories.DefaultTaskStreamFactory;
import static swfjparser.Analyzer.*;
import swftasks.complicity.factories.ParallelRigidTaskComplicityFactory;
import swftasks.input.factories.ParallelTaskInputFactory;

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
        try (Reader reader = new FileReader("CTC-SP2-1995-2.swf")) {
            //"1.swf")) {
            fFile = SWFFile.load(reader);
        }

        int[] widths = {1, 2, 3, 4, 7, 8, 15, 16, 31, 32, 63, 64, 127, 128, 255, 256, 511, 512, 1023, 1024};

        DistributionFactory factory
                = //new GammaMomentumFactory();
                // new MarkovianFactory();
                //new GammaLikelihoodFactory();
          new LikelihoodFactory(new Approx.HyperGammaLikelihoodFunction(2, 7));

        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        
        TaskInputFactory tif = new ParallelTaskInputFactory(service, 
                        new HazardTaskInputFactory(factory, widths)
        );
        TaskComplicityFactory tcf= new ParallelRigidTaskComplicityFactory(service, 
                        new SquareTaskComplicityFactory(factory, widths)
                );
        Random random = new Random(0);

        DefaultTaskStreamFactory tsf = new DefaultTaskStreamFactory(tif, tcf);

        FastTaskStreamFactory<DefaultTaskStream> ftsf = new DefaultFastTaskStreamFactory<>(tsf.get(fFile));

       
        DefaultTaskStream dts = ftsf.get();

        double len = 100000;
        int count = 100;
        int minWidth = 5, maxWidth = 7;
        boolean cdf = true;
        double[] d = fFile.getWeights(true, minWidth, maxWidth)[1];
        double[][] h = compareInputs(d, dts.getInput(), minWidth, maxWidth, len, count, true);
        IOHelper.write(Analyzer.toCsv(h), "1.csv");
        double[] d2 = fFile.getSquares(minWidth, maxWidth);
        double[][] h2 = compareComplicity(d2, dts.getComplicity(), minWidth, maxWidth, len, count, cdf);
        IOHelper.write(Analyzer.toCsv(h2), "2.csv");

        DefaultTaskStream ts = ftsf.get();
        ts.next(random);
        Task task = ts.get();
        double cnt = 1_000_000;
        CustomSWFFile cswff = new CustomSWFFile();
        for (int i = 0; i < cnt; i++) {
            cswff.add(task.toRigidTask(random));
            //System.out.println(task.getIncomeTime() + " - " + task.getMinWidth() + "..." + task.getMaxWidth() + " => " + task.getLength(task.getMinWidth()) + "..." + task.getLength(task.getMaxWidth()));
            ts.next(random);
            task = ts.get();
        }

        IOHelper.write(cswff.toString(), "generated.swf");
        System.err.println(task.getIncomeTime() / (cnt + 1));
    }

}
