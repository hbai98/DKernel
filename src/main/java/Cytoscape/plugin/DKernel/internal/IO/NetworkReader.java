package Cytoscape.plugin.DKernel.internal.IO;

import Cytoscape.plugin.DKernel.internal.util.InputAndServices;
import IO.Reader.AbstractFileReader;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class NetworkReader extends AbstractFileReader {
    private final CyNetwork network;

    /**
     * Construct a reader to get selected nodes' names
     *
     * @param network the network on which to propagate
     */
    public NetworkReader(CyNetwork network) {
        this.network = network;
    }

    /**
     * Read a txt file which contains all nodes to propagate
     *
     * @param subnet external txt file, the format should be names separated by ','
     * @return a list that contains CyNodes from the file
     * @throws IOException the selected file is not valid
     */
    public List<CyNode> read(File subnet) throws IOException {
        // settings
        this.setInputFilePath(subnet.getAbsolutePath());
        setSplitter(",\\s+");
        Vector<String> sifLine = new Vector<>();
        String line;
        // result to return
        ArrayList<CyNode> res = new ArrayList<>();
        // read
        while ((line = this.reader.readLine()) != null) {
            String[] tokens = splitter.split(line);
            if (tokens.length == 0) continue;
            for (String token : tokens) {
                if (token.length() != 0) {
                    sifLine.add(token);
                }
            }
            parse(res, sifLine);
            // clean for each line
            sifLine.clear();
        }

        return res;
    }

    private void parse(ArrayList<CyNode> res, Vector<String> sifLine) throws IOException{
        HashMap<String, CyNode> tgtMap = new HashMap<>();
        // create a map for referring CyNodes by names
        network.getNodeList().forEach(n -> {
            String strNode = network.getRow(n).get(CyNetwork.NAME, String.class);
            tgtMap.put(strNode, n);
        });
        // put all targets into the result list, and release a warning if there's a node not found
        for (String t : sifLine) {
            CyNode node = tgtMap.get(t);
            if (node == null) {
                InputAndServices.logger.error("The subnetwork nodes file contains ones that are not in your selected networks. Like "+t+" ." );
                throw new IOException("The subnetwork nodes file contains ones that are not in your selected network. Like "+t+".");
            }
            res.add(node);
        }
    }
}
