package Cytoscape.plugin.DKernel.internal.Tasks;

import Algorithms.Graph.Dynamic.Diffusion_Kernel.DK;
import DS.Matrix.StatisticsMatrix;
import DS.Network.UndirectedGraph;
import Cytoscape.plugin.DKernel.internal.util.AlgData;
import Cytoscape.plugin.DKernel.internal.util.InputsAndServices;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DKernelTask extends AbstractTask {
    @Override
    public void run(TaskMonitor taskMonitor) {
        taskMonitor.setStatusMessage("DKernel running...");
        // set up selected sub-network
        UndirectedGraph<String, DefaultEdge> selectedG = new UndirectedGraph<>(DefaultEdge.class);
        InputsAndServices.selected.forEach(n->{
            String name = InputsAndServices.network.getRow(n).get(CyNetwork.NAME, String.class);// get nodes' name
            selectedG.addVertex(name);
        });
        // save name node map for later use

        // run DK
        DK<String,DefaultEdge> dk = new DK<>(selectedG,InputsAndServices.algNet,InputsAndServices.loss);
        dk.run();
        // get collum matrix which denotes scores that the nodes get
        StatisticsMatrix mat = dk.getResult();
        // save it for further rendering process
        AlgData.scores = mat.data();

        // get access to netowrk
        CyNetwork net = InputsAndServices.network;
        // create a new column in the table to record scores
        CyTable nodeTable = net.getDefaultNodeTable();


        // create hashmap to link node name and score
        double[] scores = AlgData.scores;
        HashMap<String,Double> nodeScoreMap = new HashMap<>();
        AtomicInteger i = new AtomicInteger();
        InputsAndServices.algNet.vertexSet().forEach(v->{
            nodeScoreMap.put(v,scores[i.get()]);
            i.incrementAndGet();
        });

        //TODO if it expands to multi-kernel propagation then, scores column may need to coordinate with the previous
        nodeTable.deleteColumn(InputsAndServices.SCORES_COLName);
        nodeTable.createColumn(InputsAndServices.SCORES_COLName,Double.class,false,0.);

        for (CyNode node: net.getNodeList()) {
            CyRow row = net.getRow(node);
            String name = row.get(CyNetwork.NAME, String.class);
            row.set(InputsAndServices.SCORES_COLName,nodeScoreMap.get(name));
        }
    }
}
