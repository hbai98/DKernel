package Cytoscape.plugin.DKernel.internal.IO;

import IO.AbstractFileReader;
import org.cytoscape.model.CyNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class NetworkReader extends AbstractFileReader {
    public NetworkReader(){

    }

    /**
     * Read a txt file which contains all nodes to propagate
     * @param subnet external txt file
     * @exception IOException the selected file is not valid
     * @return a list that contains CyNodes from the file
     */
    public List<CyNode> read(File subnet) throws IOException {
        // settings
        this.setInputFilePath(subnet.getAbsolutePath());
        setSplitter("\\s+");
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
            parse(udG, sifLine);
            // clean for each line
            sifLine.clear();
        }

        return null;
    }
}
