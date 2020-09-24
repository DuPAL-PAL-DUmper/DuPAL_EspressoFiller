package info.hkzlab.dupal.EspressoOptimizer.espresso.optimizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

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
        if(table.outputs > 1) {
            logger.error("Optimizing a table with more than one output is not supported");
            return null;
        }
        
        ProcessBuilder espressoBuilder = new ProcessBuilder(espressoCmd);
        Byte[] expandedTable = new Byte[1 << table.inputs];

        logger.info("Expanding the original table!");
        for(EspressoTableEntry entry : table.entries) {
            if(entry.out[0] >= 0) { // Ignore the "don't care" ones
                int[] addresses = TableParser.expandAddress(entry.in);
                for(int addr : addresses) expandedTable[addr] = entry.out[0]; // We'll be taking only the first one
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

            EspressoTable minimizedTable = TableParser.readTableFromBuffer(new BufferedReader(new StringReader(cmdOut)));
            System.out.println(minimizedTable);

        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
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
