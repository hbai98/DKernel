package internal.UI;

import internal.Tasks.InputCheckTask;
import net.miginfocom.swing.MigLayout;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedEvent;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedListener;
import org.cytoscape.model.events.NetworkAddedEvent;
import org.cytoscape.model.events.NetworkAddedListener;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

import static internal.UI.InputsAndServices.*;


// Define a CytoPanel class
public class ControlPanel implements CytoPanelComponent, NetworkAddedListener, NetworkAboutToBeDestroyedListener {
    private static final int COMBOMAXLENGTH = 100;
    private final TaskManager taskManager;
    // data structures
    // UI components
    private JPanel rootPanel;
    //    private JPanel modeCardPanel;
    private JComboBox<CyNetwork> networkJComboBox;
    private JButton analyzeButton;
    private JTextField lossFactor;
    private JPanel paramsPanel;

    public ControlPanel() {
        this.taskManager = InputsAndServices.taskManager;
        // initialize UI settings
        init();
        // add listeners for buttons
        addListeners();
    }


    private void addListeners() {
        // analyseButton settings
        analyzeButton.addActionListener(actionEvent -> {
            setUserInput();
            TaskIterator it = new TaskIterator();
            InputCheckTask inputCheckTask = new InputCheckTask();
            it.append(inputCheckTask);
            taskManager.execute(it);
        });

    }

    private void setUserInput() {
        // shift all parameters from UI to a specific class to export users's information
        // networks
        // check if there's no networks input
        InputsAndServices.network = (CyNetwork) networkJComboBox.getSelectedItem();
        loss = Double.parseDouble(lossFactor.getText());
        //Get the selected nodes
        selected = CyTableUtil.getNodesInState(network,"selected",true);
    }

    public void init() {
        // get all available networks in the app panel
        networkJComboBox = new JComboBox<>();
        // limit the maxSize to display
        setMax(networkJComboBox);

        for (CyNetwork network : networkManager.getNetworkSet()) {
            networkJComboBox.addItem(network);
        }
        // check if there're no networks
        if (networkJComboBox.getItemCount() != 0) {
            networkJComboBox.setSelectedIndex(0);
        }
        // graphs panel components initialization
        JPanel graphsPanel = new JPanel(new MigLayout("wrap 2", "grow", "grow"));
        graphsPanel.setBorder(new TitledBorder("Networks"));
        //parameters panel
        paramsPanel = new JPanel(new MigLayout("wrap 2", "grow", "grow"));
        paramsPanel.setBorder(new TitledBorder("Parameters"));
        JLabel lossLabel = new JLabel("loss :");
        lossFactor = new JTextField("1");
        // analysis button
        analyzeButton = new JButton("Propagate");
        // add to graphsPanel
        graphsPanel.add(new JLabel("select:"));
        graphsPanel.add(networkJComboBox);
        // paramsPanel
        paramsPanel.add(lossLabel);
        paramsPanel.add(lossFactor);
        // rootPanel
        rootPanel = new JPanel(new MigLayout("wrap 1", "[grow]", "[grow]"));
        rootPanel.add(graphsPanel, "grow");
        rootPanel.add(paramsPanel, "grow");
        rootPanel.add(analyzeButton, "center");
    }

    private void setMax(JComboBox<?> combo) {
        combo.setMaximumSize(new Dimension(COMBOMAXLENGTH, combo.getMinimumSize().height));
    }


    //**********CYTOSCAPE CytoPanelComponent Interface**************
    @Override
    public Component getComponent() {
        return rootPanel;
    }

    @Override
    public CytoPanelName getCytoPanelName() {
        return CytoPanelName.WEST;
    }

    @Override
    public String getTitle() {
        return "BNMatch";
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    //**********CYTOSCAPE networks status listener interface**************
    @Override
    public void handleEvent(NetworkAboutToBeDestroyedEvent networkAboutToBeDestroyedEvent) {
        networkJComboBox.removeItem(networkAboutToBeDestroyedEvent.getNetwork());
    }

    @Override
    public void handleEvent(NetworkAddedEvent networkAddedEvent) {
        networkJComboBox.addItem(networkAddedEvent.getNetwork());
    }


}
