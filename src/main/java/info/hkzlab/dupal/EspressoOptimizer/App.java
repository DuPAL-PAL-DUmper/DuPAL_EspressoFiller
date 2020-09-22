package info.hkzlab.dupal.EspressoOptimizer;

import org.slf4j.*;

public class App {
    private final static Logger logger = LoggerFactory.getLogger(App.class);
    private final static String version = App.class.getPackage().getImplementationVersion();

    private static String inFile = null;
    private static String outFile = null;

    public static void main( String[] args )
    {
        if (args.length < 2) {
            logger.error("Wrong number of arguments passed.\n"
                    + "espresso_optimizer <input_file> <output_file>\n");

            return;
        }

        parseArgs(args);
    }

    private static void parseArgs(String[] args) {
        inFile = args[0];
        outFile = args[1];
    }
}
