package Cytoscape.plugin.DKernel.internal.UI;

import Cytoscape.plugin.DKernel.internal.Tasks.DKernelTask;
import Cytoscape.plugin.DKernel.internal.Tasks.InputCheckTask;
import Cytoscape.plugin.DKernel.internal.Tasks.RenderingTask;
import Cytoscape.plugin.DKernel.internal.util.InputAndServices;
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

import static Cytoscape.plugin.DKernel.internal.util.InputAndServices.*;


// Define a CytoPanel class
public class ControlPanel implements CytoPanelComponent, NetworkAddedListener, NetworkAboutToBeDestroyedListener {
    private static final int COMBOMAXLENGTH = 150;
    private final TaskManager taskManager;
    // data structures
    // UI components
    private JPanel rootPanel;
    //    private JPanel modeCardPanel;
    private JComboBox<CyNetwork> networkJComboBox;
    private JButton analyzeButton;
    private JTextField lossFactor;
    private JPanel paramsPanel;
    private ColorChooserButton colorChooser;
    private JFileChooser networkFileChooser;
    private JButton networkFileBrowseButton;
    private JLabel networkFileLabel;

    public ControlPanel() {
        this.taskManager = InputAndServices.taskManager;
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
            DKernelTask dkernelTask = new DKernelTask();
            RenderingTask renderingTask = new RenderingTask();

            it.append(inputCheckTask);
            it.append(dkernelTask);
            it.append(renderingTask);

            taskManager.execute(it);
        });
        // subnetwork browser button
        networkFileBrowseButton.addActionListener(actionEvent->{
            int status = networkFileChooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                File selectedFile = networkFileChooser.getSelectedFile();
                networkFileLabel.setText(selectedFile.getName());
            }
        });

    }

    private void setUserInput() {
        // shift all parameters from UI to a specific class to export users' information
        // check if there's no networks input
        InputAndServices.network = (CyNetwork) networkJComboBox.getSelectedItem();
        loss = Double.parseDouble(lossFactor.getText());
        InputAndServices.color = colorChooser.getSelectedColor();
        InputAndServices.subnet = networkFileChooser.getSelectedFile();
        //Get the selected nodes
        InputAndServices.selected = CyTableUtil.getNodesInState(network,"selected",true);
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
        JPanel graphsPanel = new JPanel(new MigLayout("wrap 1", "grow", "grow"));
        graphsPanel.setBorder(new TitledBorder("Networks"));
        networkFileLabel = new JLabel("file to select nodes");
        networkFileBrowseButton = new JButton("Browse");
        networkFileChooser = new JFileChooser();
        FileNameExtensionFilter txtFileFilter = new FileNameExtensionFilter("TEXT FIle for nodes you want to propagate in the network",
                "txt");
        networkFileChooser.addChoosableFileFilter(txtFileFilter);
        networkFileChooser.setFileFilter(txtFileFilter);
        networkFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        //parameters panel
        paramsPanel = new JPanel(new MigLayout("wrap 2", "grow", "grow"));
        paramsPanel.setBorder(new TitledBorder("Parameters"));
        JLabel lossLabel = new JLabel("loss :");
        lossFactor = new JTextField("1.00");
        JLabel color = new JLabel("Select a color :");
        colorChooser = new ColorChooserButton(Color.RED);
        InputAndServices.color = Color.RED;
        colorChooser.addColorChangedListener(newColor -> {
            colorChooser.setSelectedColor(newColor);
        });
        // analysis button
        analyzeButton = new JButton("Propagate");
        // add to graphsPanel
        graphsPanel.add(new JLabel("Select the target network:"));
        graphsPanel.add(networkJComboBox);
        graphsPanel.add(networkFileLabel);
        graphsPanel.add(networkFileBrowseButton);

        // paramsPanel
        paramsPanel.add(lossLabel);
        paramsPanel.add(lossFactor);
        paramsPanel.add(color);
        paramsPanel.add(colorChooser);
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
        return "DKernel";
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
