package info.hkzlab.dupal.EspressoOptimizer.espresso.optimizer;

import info.hkzlab.dupal.EspressoOptimizer.espresso.EspressoTable;

public interface OptimizerInterface {
    public EspressoTable optimizeTable(EspressoTable table, String options);
}
