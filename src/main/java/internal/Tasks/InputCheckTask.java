package internal.Tasks;

import internal.UI.InputsAndServices;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class InputCheckTask extends AbstractTask {
    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        // check if there's no networks input
        if (InputsAndServices.network == null) {
            taskMonitor.setTitle("Load Error! Please select your graphs.");
            taskMonitor.showMessage(TaskMonitor.Level.ERROR,"Input error.");
            InputsAndServices.logger.error("Network should be loaded first.");
            return;
        }
        

    }
}
