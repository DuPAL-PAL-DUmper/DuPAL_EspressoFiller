package info.hkzlab.dupal.EspressoOptimizer.espresso.optimizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hkzlab.dupal.EspressoOptimizer.espresso.EspressoTable;
import info.hkzlab.dupal.EspressoOptimizer.espresso.TableParser;
import info.hkzlab.dupal.EspressoOptimizer.espresso.EspressoTable.EspressoTableEntry;

public class SimpleOptimizer implements OptimizerInterface {
    private final static Logger logger = LoggerFactory.getLogger(SimpleOptimizer.class);
    private static final String[] espressoCmd = { "espresso", "-Dexact" };

    @Override
    public EspressoTable optimizeTable(EspressoTable table, String options) {
        EspressoTable minimizedTable = null;

        if(table.outputs > 1) {
            logger.error("Optimizing a table with more than one output is not supported");
            return null;
        }
        
        ProcessBuilder espressoBuilder = new ProcessBuilder(espressoCmd);
        Byte[] expandedTable = new Byte[1 << table.inputs];

        logger.info("Expanding the original table!");
        for(EspressoTableEntry entry : table.entries) {
            if(entry.out[0] >= 0) { // Ignore the "don't care" ones
                int[] addresses = EspressoTable.expandAddress(entry.in);
                for(int addr : addresses) expandedTable[addr] = entry.out[0]; // We'll be taking only the first of the outputs
            }
        }

        int table_hole = 0;
        for(Byte eEntry : expandedTable) if(eEntry == null) table_hole++;
        logger.info("Expanded into a table with size " + expandedTable.length + " and " + table_hole + " holes.");

        try {
            logger.info("Minimizing the original table!");
            Process espresso = espressoBuilder.start();
            String cmdOut = minimizeTable(table, espresso);
            espresso.destroy();

            minimizedTable = TableParser.readTableFromBuffer(new BufferedReader(new StringReader(cmdOut)), false);
            System.out.println(minimizedTable);

        } catch(IOException e) {
            e.printStackTrace();
        }

        Arrays.sort(minimizedTable.entries, new Comparator<EspressoTableEntry>(){
            @Override
            public int compare(EspressoTableEntry ent_a, EspressoTableEntry ent_b) {
                int count_a = 0, count_b = 0;

                for(byte i : ent_a.in) if(i >= 0) count_a++;
                for(byte i : ent_b.in) if(i >= 0) count_b++;

                return Integer.compare(count_b, count_a);
            }
        });

        System.out.println(minimizedTable);

        for(int opt_idx = 0; opt_idx < minimizedTable.entries.length; opt_idx++) {
            boolean good_match = true;
            EspressoTableEntry entry = minimizedTable.entries[opt_idx];
            minimizedTable.entries[opt_idx] = null;

            logger.info("Trying to remove entry " + opt_idx);

            for(int idx = 0; idx < expandedTable.length; idx++) {
                if(expandedTable[idx] != null) {
                    if(((expandedTable[idx] == 0) && !minimizedTable.match(idx)) ||
                    (expandedTable[idx] != 0) && minimizedTable.match(idx)) {
                        logger.error("Error matching at index " + idx);
                        good_match = false;
                        break;
                    }
                }
            }

            if(!good_match) { 
                logger.info("Restoring entry " + opt_idx);
                minimizedTable.entries[opt_idx] = entry; // Restore the entry
            }
        }

        return minimizedTable;
    }

    private String minimizeTable(EspressoTable table, Process espresso) throws IOException {
        StringBuffer strBuf = new StringBuffer();
        String tabStr = table.toString();

        OutputStream os = espresso.getOutputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(espresso.getInputStream()));

        os.write(tabStr.getBytes(StandardCharsets.US_ASCII));

        String line = null;
        while((line = br.readLine()) != null) strBuf.append(line+"\n");
        
        return strBuf.toString();
    }
}
