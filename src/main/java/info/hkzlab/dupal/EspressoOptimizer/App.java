package info.hkzlab.dupal.EspressoOptimizer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.*;

import info.hkzlab.dupal.EspressoOptimizer.espresso.EspressoTable;
import info.hkzlab.dupal.EspressoOptimizer.espresso.TableParser;

public class App {
    private final static Logger logger = LoggerFactory.getLogger(App.class);
    private final static String version = App.class.getPackage().getImplementationVersion();

    private static String inFile = null;
    private static String outFile = null;
    private static byte fill_type = -1;

    public static void main(String[] args) throws IOException {
        EspressoTable table = null;
        logger.info("Espresso Optimizer v" + version);

        if (args.length < 2) {
            logger.error("Wrong number of arguments passed.\n" + "espresso_optimizer <input_file> <output_file> [fill_type 0|1]\n");

            return;
        }

        parseArgs(args);

        logger.info("main() -> Reading table from " + inFile);
        
        try {
            table = TableParser.readTableFromBuffer(new BufferedReader(new FileReader(inFile)), true, fill_type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if(table == null) {
            logger.error("Failed reading table from file " + inFile);
            return;
        }

        saveTableToFile(table, outFile);
    }

    private static void parseArgs(String[] args) {
        inFile = args[0];
        outFile = args[1];

        if(args.length > 2) {
            fill_type = Byte.parseByte(args[2]);
        }
    }

    private static void saveTableToFile(EspressoTable table, String outFile) throws IOException {
        FileOutputStream fout = null;

        try {
            fout = new FileOutputStream(outFile);

            fout.write(table.toString().getBytes(StandardCharsets.US_ASCII));
            
            fout.flush();
            fout.close();
        } catch(IOException e) {
            logger.error("Error printing out the registered outputs table (not including outputs).");
            throw e;
        }
    }
}
