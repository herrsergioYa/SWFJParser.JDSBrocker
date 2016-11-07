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
import java.io.Serializable;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import swftasks.ParallelFactory;
import swftasks.TaskComplicity;
import swftasks.TaskInput;
import swftasks.complicity.factories.DeterministicTaskComplicityFactory;
import swftasks.complicity.factories.ParallelRigidTaskComplicityFactory;
import swftasks.input.factories.AbstractTaskInputFactory;
import swftasks.input.factories.DeterministicTaskInputFactory;
import swftasks.input.factories.ParallelTaskInputFactory;
import swftasks.input.factories.SimpleTaskInputFactory;

/**
 *
 * @author HerrSergio
 */
public class SWFJParser {

   public static final String fname = "UniLu-Gaia-2014-2";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        // TODO code application logic here

        double len = 10_000;
        int count = 100_000;
        boolean cdf = true;

        LinkedHashMap<String, DistributionFactory> distrFactories = new LinkedHashMap<>();
        distrFactories.put("M", new MarkovianFactory());
        distrFactories.put("Gm", new GammaMomentumFactory());
        distrFactories.put("Gl", new GammaLikelihoodFactory());
       // distrFactories.put("H3", new LikelihoodFactory(new Approx.HyperexponentialLikelihoodFunction(3, 7)));
        //distrFactories.put("HG2", new LikelihoodFactory(new Approx.HyperGammaLikelihoodFunction(2, 7))); //4

        LinkedHashMap<String, int[]> widths = new LinkedHashMap<>();
        widths.put("1", new int[]{Integer.MAX_VALUE});
        widths.put("", Analyzer.widths());

        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            LinkedHashMap<String, TaskInputFactoryFactory> inputFactories = new LinkedHashMap<>();
            HashMap<String, DataFactory> inputData = new HashMap<>();
            inputFactories.put("haz", (d, w) -> new ParallelTaskInputFactory(service, new HazardTaskInputFactory(true, d, w)));
            inputData.put("haz", (f, n, x) -> f.getIntervals(n, x));
            inputFactories.put("smp", (d, w) -> new ParallelTaskInputFactory(service, new SimpleTaskInputFactory(d, w)));
            inputData.put("smp", (f, n, x) -> f.getWeights(true, n, x)[1]);
            inputFactories.put("shaz", (d, w) -> new ParallelTaskInputFactory(service, new SemiHazardTaskInputFactory(d, w)));
            inputData.put("shaz", (f, n, x) -> f.getWeights(f.getHazard(true), f.getShift(), f.getIncomeTimes(n, x), f.getLastEvent(), f.isAligned(),  
                f.getStep(), n, x));
            
            LinkedHashMap<String, TaskInputFactory> inputs = new LinkedHashMap<>();
            IdentityHashMap<TaskInputFactory, int[]> inputWidths = new IdentityHashMap<>();
            IdentityHashMap<TaskInputFactory, DataFactory> inputDatas = new IdentityHashMap<>();

            SWFFile fFile = null;
            try (Reader reader = new FileReader(fname + ".swf")) {
                //"1.swf")) {
                fFile = SWFFile.load(reader);
            }

            for (Map.Entry<String, DistributionFactory> entry1 : distrFactories.entrySet()) {
                for (Map.Entry<String, int[]> entry2 : widths.entrySet()) {
                    for (Map.Entry<String, TaskInputFactoryFactory> entry3 : inputFactories.entrySet()) {
                        String key = entry1.getKey() + entry3.getKey() + entry2.getKey();
                        TaskInputFactory factory = entry3.getValue().get(entry1.getValue(), entry2.getValue());
                        inputs.put(key, factory);
                        if (factory instanceof ParallelFactory) {
                            ((ParallelFactory) factory).beginGet(fFile);
                        }
                        inputWidths.put(factory, entry2.getValue());
                        inputDatas.put(factory, inputData.get(entry3.getKey()));
                    }
                }
            }

            LinkedHashMap<String, TaskComplicityFactoryFactory> complicitiesFactories = new LinkedHashMap<>();
            HashMap<String, DataFactory> complicitiesData = new HashMap<>();
            complicitiesFactories.put("len", (d, w) -> new ParallelRigidTaskComplicityFactory(service, 
                    new LengthTaskComplicityFactory(d, w)));
            complicitiesData.put("len", (f, m, z) -> f.getLengths(m, z));
            complicitiesFactories.put("sqr", (d, w) -> new ParallelRigidTaskComplicityFactory(service, 
                    new SquareTaskComplicityFactory(d, w)));
            complicitiesData.put("sqr", (f, m, z) -> f.getSquares(m, z));

            LinkedHashMap<String, TaskComplicityFactory> compilities = new LinkedHashMap<>();
            IdentityHashMap<TaskComplicityFactory, int[]> complicitiesWidths = new IdentityHashMap<>();
            IdentityHashMap<TaskComplicityFactory, DataFactory> complicitiesDatas = new IdentityHashMap<>();

            for (Map.Entry<String, DistributionFactory> entry1 : distrFactories.entrySet()) {
                for (Map.Entry<String, int[]> entry2 : widths.entrySet()) {
                    for (Map.Entry<String, TaskComplicityFactoryFactory> entry3 : complicitiesFactories.entrySet()) {
                        String key = entry1.getKey() + entry3.getKey() + entry2.getKey();
                        TaskComplicityFactory factory = entry3.getValue().get(entry1.getValue(), entry2.getValue());
                        compilities.put(key, factory);
                        if (factory instanceof ParallelFactory) {
                            ((ParallelFactory) factory).beginGet(fFile);
                        }
                        complicitiesWidths.put(factory, entry2.getValue());
                        //final Future<double[]> future = 
                        complicitiesDatas.put(factory, complicitiesData.get(entry3.getKey()));
                    }
                }
            }

            for (Map.Entry<String, TaskInputFactory> entry : inputs.entrySet()) {
                String fileName = fname + "." + entry.getKey();
                TaskInput taskInput = entry.getValue().get(fFile);
                IOHelper.serialize(taskInput, fileName);
                System.out.println(fileName);
                int[] width = inputWidths.get(entry.getValue());
                for (int i = 0, last = 0; i < width.length; last = width[i++]) {
                    int minWidth = last + 1, maxWidth = width[i];
                    double[] d = inputDatas.get(entry.getValue()).get(fFile, minWidth, maxWidth);
                    if (d.length == 0) {
                        continue;
                    }
                    double[][] h = compareInputs(d, taskInput, minWidth, maxWidth, len, count, true);
                    String csv = fileName + "_" + minWidth + "_" + maxWidth + ".csv"; 
                    IOHelper.write(Analyzer.toCsv(h), csv);
                    System.out.println(csv);
                }
            }

            for (Map.Entry<String, TaskComplicityFactory> entry : compilities.entrySet()) {
                String fileName = fname + "." + entry.getKey();
                TaskComplicity taskComplicity = entry.getValue().get(fFile);
                IOHelper.serialize(taskComplicity, fileName);
                System.out.println(fileName);
                int[] width = complicitiesWidths.get(entry.getValue());
                for (int i = 0, last = 0; i < width.length; last = width[i++]) {
                    int minWidth = last + 1, maxWidth = width[i];

                    double[] d2 = complicitiesDatas.get(entry.getValue()).get(fFile, minWidth, maxWidth);
                    if (d2.length == 0) {
                        continue;
                    }
                    double[][] h2 = compareComplicity(d2, taskComplicity, minWidth, maxWidth, len, count, cdf);
                    String csv = fileName + "_" + minWidth + "_" + maxWidth + ".csv";
                    IOHelper.write(Analyzer.toCsv(h2), csv);
                    System.out.println(csv);
                }
            }
            /*  try {
            TaskInputFactory tif = new ParallelTaskInputFactory(service,
                    !haz
                            ? new SimpleTaskInputFactory(factory, widths)
                            : new HazardTaskInputFactory(factory, widths)
            //new DeterministicTaskInputFactory(haz, 3600 * 24 * 7, widths)
            );
            TaskComplicityFactory tcf = new ParallelRigidTaskComplicityFactory(service,
                    !sqr
                            ? new LengthTaskComplicityFactory(factory, widths)
                            : new SquareTaskComplicityFactory(factory, widths)
            //new DeterministicTaskComplicityFactory(sqr, widths)
            );
            Random random = new Random(0);

            DefaultTaskStreamFactory tsf = new DefaultTaskStreamFactory(tif, tcf);

            FastTaskStreamFactory<DefaultTaskStream> ftsf = new DefaultFastTaskStreamFactory<>(tsf.get(fFile));

            DefaultTaskStream dts = ftsf.get();

            IOHelper.serialize(dts.getInput(), iname);
            IOHelper.serialize(dts.getComplicity(), oname);

            for (int i = 0, last = 0; i < widths.length; last = widths[i++]) {
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
            }

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
            //System.err.println(task.getIncomeTime() / (cnt + 1));*/
        } finally {
            service.shutdown();
        }
    }

    /*private static class SWFInput implements SWFJob {

        private String distName;
        private DistributionFactory distributionFactory;
        private String typeName;
        private TaskInputFactory inputFactory;

        public SWFInput(String distName, DistributionFactory distributionFactory, String typeName, TaskInputFactory inputFactory) {
            this.distName = distName;
            this.distributionFactory = distributionFactory;
            this.typeName = typeName;
            this.inputFactory = inputFactory;
        }

        @Override
        public SWFResult call() throws Exception {

        }

        @Override
        public void preCall() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }*/
}
