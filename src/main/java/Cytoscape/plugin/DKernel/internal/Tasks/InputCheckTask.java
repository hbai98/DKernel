package Cytoscape.plugin.DKernel.internal.Tasks;

import Cytoscape.plugin.DKernel.internal.util.InputAndServices;
import Cytoscape.plugin.DKernel.internal.util.CytoUtils;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class InputCheckTask extends AbstractTask {
    @Override
    public void run(TaskMonitor taskMonitor) {
        // check if there's no networks input
        if (InputAndServices.network == null) {
            taskMonitor.setTitle("Load Error! Please load your graph.");
            taskMonitor.showMessage(TaskMonitor.Level.ERROR,"Input error.");
            InputAndServices.logger.error("Network should be loaded first.");
            return;
        }
        // no file or nodes are selected
        if(InputAndServices.selected.size() == 0 || InputAndServices.subnet == null){
            taskMonitor.setTitle("Load Error! Please select the subgraph.");
            taskMonitor.showMessage(TaskMonitor.Level.ERROR,"Input error.");
            InputAndServices.logger.error("Select the subnetwork to propagate.");
            return;
        }
        CyNetwork network = InputAndServices.network;
        InputAndServices.algNet = CytoUtils.convert(network);
    }
}
