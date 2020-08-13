package internal.Tasks;

import internal.Canvas.ColorShade;
import internal.util.AlgData;
import internal.util.CytoUtils;
import internal.util.InputsAndServices;
import jdk.internal.util.xml.impl.Input;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.jgrapht.alg.util.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        Pair<Integer, Map<Double, Integer>> res = getVar(scores);
        Map<Double,Integer> scoreColorMap = res.getSecond();// compute the variety of shades based on scores, get score -> shade index
        int numb = res.getFirst();
        List<Color> colors = ColorShade.generateShadesMap(InputsAndServices.color,numb);
        // reverse list for colors -> thin to thick
        Collections.reverse(colors);
        // 2. create hashmap to link node name and score
        HashMap<String,Double> nodeScoreMap = new HashMap<>();
        AtomicInteger i = new AtomicInteger();
        InputsAndServices.algNet.vertexSet().forEach(v->{
            nodeScoreMap.put(v,scores[i.get()]);
            i.incrementAndGet();
        });
        // 3. render each node with its score-related color
        view.getNodeViews().forEach(v->{
            CyNode node = v.getModel();
            String name = network.getRow(node).get(CyNetwork.NAME, String.class);
            double score = nodeScoreMap.get(name);
            int index = scoreColorMap.get(score);
            Color color = colors.get(index);
            // paint
            view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR,color);
        });
    }

    // compute the variety of shades based on scores
    private Pair<Integer,Map<Double, Integer>> getVar(final double[] scores) {
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
        return new Pair<>(set,map);
    }
}
