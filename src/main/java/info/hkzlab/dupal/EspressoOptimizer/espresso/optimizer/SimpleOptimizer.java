package info.hkzlab.dupal.EspressoOptimizer.espresso.optimizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

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

        ProcessBuilder espressoBuilder = new ProcessBuilder(espressoCmd);

        int lines = 0;
        try {
            lines = minimizeAndCountLines(table, espressoBuilder.start());
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("got " + lines);

        return table;
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
