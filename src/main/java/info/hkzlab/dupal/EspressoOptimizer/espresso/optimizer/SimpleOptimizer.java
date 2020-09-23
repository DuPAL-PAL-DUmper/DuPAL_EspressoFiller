package info.hkzlab.dupal.EspressoOptimizer.espresso.optimizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.swing.text.PlainDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hkzlab.dupal.EspressoOptimizer.espresso.EspressoTable;

public class SimpleOptimizer implements OptimizerInterface {
    private final static Logger logger = LoggerFactory.getLogger(SimpleOptimizer.class);
    private static final String[] espressoCmd = { "espresso", "-Dexact" };

    @Override
    public EspressoTable optimizeTable(EspressoTable table, String options) {
        if(table.outputs > 1) {
            logger.error("Optimizing a table with more than one output is not supported");
            return null;
        }
        
        EspressoTable tabCopy = table.copyTable();

        try {
            ProcessBuilder espressoBuilder = new ProcessBuilder(espressoCmd);
            int base_lines = minimizeAndCountLines(table, espressoBuilder.start());
            int tot_holes = 0;
            int plugged_holes = 0;

            for(byte[] entry : table.entries) if(entry == null) tot_holes++;

            logger.info("Starting table gets minimized in " + base_lines + " lines, has " + tot_holes + " holes in table");
            boolean keepMinimizing = true;
            int firstEmpty = 0;
            while(keepMinimizing) {
                keepMinimizing = false;
                for(int idx = firstEmpty; idx < tabCopy.entries.length; idx++) {
                    if(tabCopy.entries[idx] == null) {
                        firstEmpty = idx;
                        int reference_lines = minimizeAndCountLines(tabCopy, espressoBuilder.start());

                        logger.info("Attempting optimization at index " + idx + " reference lines " + reference_lines);
                        int nl_0, nl_1;
                        tabCopy.entries[idx] = new byte[] { 0 };
                        nl_0 = minimizeAndCountLines(tabCopy, espressoBuilder.start());
                        logger.info("0 -> new lines " + nl_0);
                        tabCopy.entries[idx] = new byte[] { 1 };
                        nl_1 = minimizeAndCountLines(tabCopy, espressoBuilder.start());
                        logger.info("1 -> new lines " + nl_1);

                        tabCopy.entries[idx] = null;
                        if(nl_0 <= nl_1) {
                            if(nl_0 <= reference_lines) tabCopy.entries[idx] = new byte[] {0};
                        } else {
                            if(nl_1 <= reference_lines) tabCopy.entries[idx] = new byte[] {1};
                        }

                        if(tabCopy.entries[idx] != null) {
                            plugged_holes++;
                            logger.info("Plugging holes... " + plugged_holes + "/" + tot_holes);
                                keepMinimizing = true;
                                break;
                        }
                    }
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        return tabCopy;
    }

    private int minimizeAndCountLines(EspressoTable table, Process espresso) throws IOException {
        int counter = 0;
        String tabStr = table.toString();

        OutputStream os = espresso.getOutputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(espresso.getInputStream()));

        os.write(tabStr.getBytes(StandardCharsets.US_ASCII));

        while(br.readLine() != null) counter++;
        
        espresso.destroy();

        return counter;
    }
}
