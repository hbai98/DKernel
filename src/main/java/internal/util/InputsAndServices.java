package internal.util;

import DS.Matrix.SimMat;
import DS.Network.UndirectedGraph;
import org.cytoscape.application.CyUserLog;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.undo.UndoSupport;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public final class InputsAndServices {

    // Cytoscape services
    public static CyServiceRegistrar registrar;
    public static CyNetworkManager networkManager;
    public static CyNetworkFactory networkFactory;
    public static CyNetworkNaming naming;
    public static TaskManager taskManager;
    public static UndoSupport undoSupport;
    public static CyNetworkViewManager networkViewManager;
    public static CyNetworkViewFactory networkViewFactory;
    public static CyLayoutAlgorithmManager layoutAlgorithmManager;
    public static CyEventHelper eventHelper;
    public static VisualMappingFunctionFactory mapFactoryService;
    public static final Logger logger = LoggerFactory.getLogger(CyUserLog.NAME);
    public static double loss;
    public static List<CyNode> selected;
    public static CyNetwork network;
    public static UndirectedGraph<String, DefaultEdge> algNet;
    public static Color color;

    public static void initServices() {
        if(registrar!=null){
            networkFactory = registrar.getService(CyNetworkFactory.class);
            networkManager = registrar.getService(CyNetworkManager.class);
            naming = registrar.getService(CyNetworkNaming.class);
            taskManager = registrar.getService(TaskManager.class);
            undoSupport = registrar.getService(UndoSupport.class);
            networkViewManager = registrar.getService(CyNetworkViewManager.class);
            networkViewFactory = registrar.getService(CyNetworkViewFactory.class);
            layoutAlgorithmManager = registrar.getService(CyLayoutAlgorithmManager.class);
            eventHelper = registrar.getService(CyEventHelper.class);
            mapFactoryService = registrar.getService(VisualMappingFunctionFactory.class,"(mapping.type=continuous)");
        }
    }
}
