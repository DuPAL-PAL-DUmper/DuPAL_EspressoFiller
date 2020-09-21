package info.hkzlab.dupal.EspressoOptimizer.espresso;

public class EspressoTable {
    public final int inputs;
    public final int outputs;
    public final String[] input_labels;
    public final String[] output_labels;
    public final boolean[] phase;
    public final byte[][] entries;

    public EspressoTable(final int inputs, final int outputs, final String[] input_labels, final String[] output_labels, final boolean[] phase, final byte[][] entries) {
        assert(inputs == input_labels.length);
        assert(outputs == output_labels.length);

        this.inputs = inputs;
        this.outputs = outputs;
        this.input_labels = input_labels;
        this.output_labels = output_labels;
        this.phase = phase;
        this.entries = entries;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();

        // IOs
        strBuf.append(".i " + inputs + "\n");
        strBuf.append(".o " + outputs + "\n");

        // Labels
        strBuf.append(".ilb ");
        for(String label : input_labels) strBuf.append(label + " ");
        strBuf.append('\n');
        
        strBuf.append(".ob ");
        for(String label : output_labels) strBuf.append(label + " ");
        strBuf.append('\n');

        // Phase
        strBuf.append(".phase ");
        for(boolean p : phase) strBuf.append(p ? '1' : '0');
        strBuf.append('\n');

        strBuf.append('\n');

        // Entries

        // Footer
        strBuf.append(".e\n");
        return strBuf.toString();
    }
}
