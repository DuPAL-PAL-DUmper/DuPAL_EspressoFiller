package info.hkzlab.dupal.EspressoOptimizer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.*;

import info.hkzlab.dupal.EspressoOptimizer.espresso.EspressoTable;
import info.hkzlab.dupal.EspressoOptimizer.espresso.TableParser;
import info.hkzlab.dupal.EspressoOptimizer.espresso.optimizer.SimpleOptimizer;

public class App {
    private final static Logger logger = LoggerFactory.getLogger(App.class);
    private final static String version = App.class.getPackage().getImplementationVersion();

    private static String inFile = null;
    private static String outFile = null;

    public static void main(String[] args) {
        EspressoTable table = null;
        logger.info("Espresso Optimizer v" + version);

        if (args.length < 2) {
            logger.error("Wrong number of arguments passed.\n" + "espresso_optimizer <input_file> <output_file>\n");

            return;
        }

        parseArgs(args);

        logger.info("main() -> Reading table from " + inFile);
        
        try {
            table = TableParser.readTableFromBuffer(new BufferedReader(new FileReader(inFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(table == null) {
            logger.error("Failed reading table from file " + inFile);
            return;
        }

        logger.info("Read table.");

        (new SimpleOptimizer()).optimizeTable(table, "");
    }

    private static void parseArgs(String[] args) {
        inFile = args[0];
        outFile = args[1];
    }
}
