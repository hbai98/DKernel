package internal.Tasks;

import internal.Canvas.ColorShade;
import internal.util.AlgData;
import internal.util.CytoUtils;
import internal.util.InputsAndServices;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class RenderingTask extends AbstractTask {
    @Override
    public void run(TaskMonitor taskMonitor) {
        // get original network view
        CyNetwork network = InputsAndServices.network;
        CyNetworkView view = CytoUtils.getCyNetworkView(network,
                InputsAndServices.networkViewManager,
                InputsAndServices.networkViewFactory);
        // render the view
        // 1. generate color shades of red
        double[] scores = AlgData.scores;
        int v = getVar(scores).length;// compute the variety of shades based on scores
        List<Color> colors = ColorShade.generateShadesMap(InputsAndServices.color,v);
        // 2. render each node with its score-related color
        //
    }

    // compute the variety of shades based on scores
    private int[] getVar(final double[] scores) {
        int id = 0;
        // map for score -> shade index
        HashMap<Double,Integer> map = new HashMap<>();
        // get Max and min
        double[] scores_ = Arrays.copyOf(scores,scores.length);// copy scores
        // sort in the ascending order
        Arrays.sort(scores_);
        double max = scores_[scores_.length-1];
        double min = scores_[0];
        //get the average difference
        double aveDf = (max-min)/ (scores.length-1);
        // record the distance more than the average
        int set = 0;
        for (int i = 0; i < scores_.length; i++) {
            if(i!= scores.length-1){
                double dif = Math.abs(scores_[i+1] - scores_[i]);
                map.put(scores_[i],set);
                if(dif > aveDf){
                    set++;
                }
            }
           else{
               map.put(scores_[scores_.length-1],set);
            }
        }


        return set;
    }
}
