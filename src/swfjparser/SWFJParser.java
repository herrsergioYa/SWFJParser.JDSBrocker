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
import swftasks.complicity.factories.DeterministicTaskComplicityFactory;
import swftasks.complicity.factories.ParallelRigidTaskComplicityFactory;
import swftasks.input.factories.DeterministicTaskInputFactory;
import swftasks.input.factories.ParallelTaskInputFactory;
import swftasks.input.factories.SimpleTaskInputFactory;

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

        DistributionFactory distrFactories[] = {
            new MarkovianFactory(), //0
            new GammaMomentumFactory(), //1
            new GammaLikelihoodFactory(), //2
            new LikelihoodFactory(new Approx.HyperexponentialLikelihoodFunction(3, 7)), //3
            new LikelihoodFactory(new Approx.HyperGammaLikelihoodFunction(2, 7)) //4
        };
        String distrsFctoryNames[] = {
            "M", "Gm", "Gl", "H3", "HG2"
        };

        boolean haz = true, sqr = haz;
        int idist = 0;
        String dist = distrsFctoryNames[idist];
        String fname = "CTC-SP2-1995-2";
        String iname = fname + "." + dist + (haz ? "haz" : "smp");
        String oname = fname + "." + dist + (sqr ? "sqr" : "len");

        iname = fname + ".Di" + (haz ? "haz" : "smp");;
        oname = fname + ".Dc" + (sqr ? "sqr" : "len");

        SWFFile fFile = null;
        try (Reader reader = new FileReader(fname + ".swf")) {
            //"1.swf")) {
            fFile = SWFFile.load(reader);
        }

        int[] widths
                = //{Integer.MAX_VALUE};
                Analyzer.widths();// {1, 2, 3, 4, 7, 8, 15, 16, 31, 32, 63, 64, 127, 128, 255, 256, 511, 512, 1023, 1024};

        DistributionFactory factory = distrFactories[idist];

        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            TaskInputFactory tif = new ParallelTaskInputFactory(service,
                    /*!haz ?
                new SimpleTaskInputFactory(factory, widths) :
                new HazardTaskInputFactory(factory, widths)*/
                    new DeterministicTaskInputFactory(haz, 3600 * 24 * 7, widths)
            );
            TaskComplicityFactory tcf = new ParallelRigidTaskComplicityFactory(service,
                    /*!sqr ?
                new LengthTaskComplicityFactory(factory, widths) :
                new SquareTaskComplicityFactory(factory, widths)*/
                    new DeterministicTaskComplicityFactory(sqr, widths)
            );
            Random random = new Random(0);

            DefaultTaskStreamFactory tsf = new DefaultTaskStreamFactory(tif, tcf);

            FastTaskStreamFactory<DefaultTaskStream> ftsf = new DefaultFastTaskStreamFactory<>(tsf.get(fFile));

            DefaultTaskStream dts = ftsf.get();

            IOHelper.serialize(dts.getInput(), iname);
            IOHelper.serialize(dts.getComplicity(), oname);

            /*for (int i = 0, last = 0; i < widths.length; last = widths[i++]) {
                double len = 1_000_000;
                int count = 100_000;
                int minWidth = last + 1, maxWidth = widths[i];
                boolean cdf = true;
                double[] d = !haz
                        ? fFile.getIntervals(minWidth, maxWidth)
                        : fFile.getWeights(true, minWidth, maxWidth)[1];
                if (d.length == 0) {
                    continue;
                }
                double[][] h = compareInputs(d, dts.getInput(), minWidth, maxWidth, len, count, true);
                IOHelper.write(Analyzer.toCsv(h), iname + "_" + minWidth + "_" + maxWidth + ".csv");
                double[] d2 = sqr
                        ? fFile.getSquares(minWidth, maxWidth)
                        : fFile.getLengths(minWidth, maxWidth);
                if (d2.length == 0) {
                    continue;
                }
                double[][] h2 = compareComplicity(d2, dts.getComplicity(), minWidth, maxWidth, len, count, cdf);
                IOHelper.write(Analyzer.toCsv(h2), oname + "_" + minWidth + "_" + maxWidth + ".csv");
            }*/

            //service.shutdown();

            DefaultTaskStream ts = ftsf.get();
            
            double cnt = 1_000_000;
            CustomSWFFile cswff = new CustomSWFFile();
            for (int i = 0; i < cnt; i++) {
                ts.next(random);
                Task task = ts.get();
                cswff.add(task.toRigidTask(random));
                //System.out.println(task.getIncomeTime() + " - " + task.getMinWidth() + "..." + task.getMaxWidth() + " => " + task.getLength(task.getMinWidth()) + "..." + task.getLength(task.getMaxWidth()));
                //ts.next(random);
                //task = ts.get();
            }

            IOHelper.write(cswff.toString(), "generated.swf");
            //System.err.println(task.getIncomeTime() / (cnt + 1));
        } finally {
            service.shutdown();
        }
    }

}
