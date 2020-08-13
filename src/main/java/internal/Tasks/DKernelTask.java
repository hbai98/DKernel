package internal.Tasks;

import Algorithms.Graph.Dynamic.Diffusion_Kernel.DK;
import DS.Matrix.StatisticsMatrix;
import DS.Network.UndirectedGraph;
import internal.util.AlgData;
import internal.util.InputsAndServices;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.jgrapht.graph.DefaultEdge;

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
        // run DK
        DK<String,DefaultEdge> dk = new DK<>(selectedG,InputsAndServices.algNet,InputsAndServices.loss);
        dk.run();
        // get collum matrix which denotes scores that the nodes get
        StatisticsMatrix mat = dk.getResult();
        // save it for further rendering process
        AlgData.scores = mat.data();

    }
}
