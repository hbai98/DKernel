package Cytoscape.plugin.DKernel.internal.Tasks;

import Cytoscape.plugin.DKernel.internal.util.InputsAndServices;
import Cytoscape.plugin.DKernel.internal.util.CytoUtils;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class InputCheckTask extends AbstractTask {
    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        // check if there's no networks input
        if (InputsAndServices.network == null) {
            taskMonitor.setTitle("Load Error! Please load your graph.");
            taskMonitor.showMessage(TaskMonitor.Level.ERROR,"Input error.");
            InputsAndServices.logger.error("Network should be loaded first.");
            return;
        }
        if(InputsAndServices.selected.size() == 0){
            taskMonitor.setTitle("Load Error! Please select your the subgraph.");
            taskMonitor.showMessage(TaskMonitor.Level.ERROR,"Input error.");
            InputsAndServices.logger.error("Select the subnetwork to propagate.");
            return;
        }
        CyNetwork network = InputsAndServices.network;
        InputsAndServices.algNet = CytoUtils.convert(network);
    }
}
