package info.hkzlab.dupal.EspressoOptimizer.espresso.optimizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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

        try {
            Process espresso = espressoBuilder.start();
            String[] cmdOut = minimizeTable(table, espresso;
            espresso.destroy();


        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String[] minimizeTable(EspressoTable table, Process espresso) throws IOException {
        ArrayList<String> cmdOut = new ArrayList<>();
        String tabStr = table.toString();

        OutputStream os = espresso.getOutputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(espresso.getInputStream()));

        os.write(tabStr.getBytes(StandardCharsets.US_ASCII));

        String line = null;
        while((line = br.readLine()) != null) cmdOut.add(line);
        
        return cmdOut.toArray(new String[cmdOut.size()]);
    }
}
