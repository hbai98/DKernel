package Cytoscape.plugin.DKernel.internal.util;

import DS.Network.UndirectedGraph;
import org.cytoscape.application.CyUserLog;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.undo.UndoSupport;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;

public final class InputsAndServices {

    // Cytoscape services
    public static CyServiceRegistrar registrar;
    public static CyNetworkManager networkManager;
    public static TaskManager taskManager;
    public static UndoSupport undoSupport;
    public static CyNetworkViewManager networkViewManager;
    public static CyNetworkViewFactory networkViewFactory;
    public static CyEventHelper eventHelper;
    public static VisualMappingFunctionFactory mapFactoryService;
    public static final Logger logger = LoggerFactory.getLogger(CyUserLog.NAME);
    public static double loss;
    public static List<CyNode> selected;
    public static CyNetwork network;
    public static UndirectedGraph<String, DefaultEdge> algNet;
    public static Color color;
    public static final String SCORES_COLName = "SCORES";

    public static void initServices() {
        if(registrar!=null){
            networkManager = registrar.getService(CyNetworkManager.class);
            networkViewManager = registrar.getService(CyNetworkViewManager.class);
            taskManager = registrar.getService(TaskManager.class);
            undoSupport = registrar.getService(UndoSupport.class);
            eventHelper = registrar.getService(CyEventHelper.class);
            mapFactoryService = registrar.getService(VisualMappingFunctionFactory.class,"(mapping.type=continuous)");
        }
    }
}
