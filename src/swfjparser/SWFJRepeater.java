/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swfjparser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import mymath.DistributionFactory;
import serialization.IOHelper;
import static swfjparser.Analyzer.compareComplicity;
import static swfjparser.Analyzer.compareInputs;
import static swfjparser.SWFJParser.fname;
import swftasks.ParallelFactory;
import swftasks.Task;
import swftasks.TaskComplicity;
import swftasks.TaskComplicityFactory;
import swftasks.TaskInput;
import swftasks.TaskInputFactory;
import swftasks.complicity.factories.DeterministicTaskComplicityFactory;
import swftasks.complicity.factories.LengthTaskComplicityFactory;
import swftasks.complicity.factories.ParallelRigidTaskComplicityFactory;
import swftasks.complicity.factories.SquareTaskComplicityFactory;
import swftasks.input.factories.DeterministicTaskInputFactory;
import swftasks.input.factories.HazardTaskInputFactory;
import swftasks.input.factories.ParallelTaskInputFactory;
import swftasks.input.factories.SemiHazardTaskInputFactory;
import swftasks.input.factories.SimpleTaskInputFactory;
import swftasks.streams.DefaultTaskStream;
import swftasks.streams.factories.DefaultTaskStreamFactory;

/**
 *
 * @author HerrSergio
 */
public class SWFJRepeater {

    public static final double cycle = 3600 * 24 * 7;
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        LinkedHashMap<String, int[]> widths = new LinkedHashMap<>();
        widths.put("1", new int[]{Integer.MAX_VALUE});
        widths.put("", Analyzer.widths());

        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            LinkedHashMap<String, TaskInputFactoryFactory> inputFactories = new LinkedHashMap<>();
            inputFactories.put("haz", (d, w) -> new ParallelTaskInputFactory(service, new DeterministicTaskInputFactory(true, cycle, w)));
            inputFactories.put("smp", (d, w) -> new ParallelTaskInputFactory(service, new DeterministicTaskInputFactory(false, cycle, w)));
            inputFactories.put("shaz", (d, w) -> new ParallelTaskInputFactory(service, new DeterministicTaskInputFactory(null, cycle, w)));

            LinkedHashMap<String, TaskInputFactory> inputs = new LinkedHashMap<>();
            IdentityHashMap<TaskInputFactory, int[]> inputWidths = new IdentityHashMap<>();

            SWFFile fFile = null;
            try (Reader reader = new FileReader(fname + ".swf")) {
                //"1.swf")) {
                fFile = SWFFile.load(reader);
            }

            for (Map.Entry<String, int[]> entry2 : widths.entrySet()) {
                for (Map.Entry<String, TaskInputFactoryFactory> entry3 : inputFactories.entrySet()) {
                    String key = "Di" + entry3.getKey() + entry2.getKey();
                    TaskInputFactory factory = entry3.getValue().get(null, entry2.getValue());
                    inputs.put(key, factory);
                    if (factory instanceof ParallelFactory) {
                        ((ParallelFactory) factory).beginGet(fFile);
                    }
                    inputWidths.put(factory, entry2.getValue());
                }
            }

            LinkedHashMap<String, TaskComplicityFactoryFactory> complicitiesFactories = new LinkedHashMap<>();
            complicitiesFactories.put("len", (d, w) -> new ParallelRigidTaskComplicityFactory(service,
                    new DeterministicTaskComplicityFactory(false, w)));
            complicitiesFactories.put("sqr", (d, w) -> new ParallelRigidTaskComplicityFactory(service,
                    new DeterministicTaskComplicityFactory(true, w)));

            LinkedHashMap<String, TaskComplicityFactory> compilities = new LinkedHashMap<>();
            IdentityHashMap<TaskComplicityFactory, int[]> complicitiesWidths = new IdentityHashMap<>();

            for (Map.Entry<String, int[]> entry2 : widths.entrySet()) {
                for (Map.Entry<String, TaskComplicityFactoryFactory> entry3 : complicitiesFactories.entrySet()) {
                    String key = "Dc" + entry3.getKey() + entry2.getKey();
                    TaskComplicityFactory factory = entry3.getValue().get(null, entry2.getValue());
                    compilities.put(key, factory);
                    if (factory instanceof ParallelFactory) {
                        ((ParallelFactory) factory).beginGet(fFile);
                    }
                    complicitiesWidths.put(factory, entry2.getValue());
                }
            }

            for (Map.Entry<String, TaskInputFactory> entry : inputs.entrySet()) {
                String fileName = fname + "." + entry.getKey();
                TaskInput taskInput = entry.getValue().get(fFile);
                IOHelper.serialize(taskInput, fileName);
                System.out.println(fileName);
            }

            for (Map.Entry<String, TaskComplicityFactory> entry : compilities.entrySet()) {
                String fileName = fname + "." + entry.getKey();
                TaskComplicity taskComplicity = entry.getValue().get(fFile);
                IOHelper.serialize(taskComplicity, fileName);
                System.out.println(fileName);
            }
            
        
            DefaultTaskStream ts = new DefaultTaskStream(
                    (TaskInput)IOHelper.deserialize(fname + "." + "Dihaz"),
                    (TaskComplicity)IOHelper.deserialize(fname + "." + "Dcsqr")
            );
            
            Random random = new Random(0);

            double cnt = 100_000;
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
        } finally {
            service.shutdown();
        }
        
        
    }
}
