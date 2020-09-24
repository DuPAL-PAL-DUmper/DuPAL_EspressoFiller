package info.hkzlab.dupal.EspressoOptimizer.espresso;

public class EspressoTable {
    public final int inputs;
    public final int outputs;
    public final String[] input_labels;
    public final String[] output_labels;
    public final boolean[] phase;
    public final EspressoTableEntry[] entries;

    public EspressoTable(final int inputs, final int outputs, final String[] input_labels, final String[] output_labels, final boolean[] phase, final EspressoTableEntry[] entries) {
        assert(inputs == input_labels.length);
        assert(outputs == output_labels.length);
        assert(phase.length == outputs);

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
        for(int idx = 0; idx < entries.length; idx++) {
            if(entries[idx] != null) {
                for(byte e : entries[idx].in) strBuf.append(e == 0 ? '0' : ((e > 0) ? '1' : '-'));
                strBuf.append(' ');
                for(byte e : entries[idx].out) strBuf.append(e == 0 ? '0' : ((e > 0) ? '1' : '-'));
                strBuf.append('\n');
            }
        }

        // Footer
        strBuf.append(".e\n");
        return strBuf.toString();
    }

    public EspressoTable copyTable() {
        return new EspressoTable(inputs, outputs, input_labels.clone(), output_labels.clone(), phase.clone(), entries.clone());
    }

    public static class EspressoTableEntry {
        public byte[] in;
        public byte[] out;

        public EspressoTableEntry(final int in_pins, final int out_pins) {
            in = new byte[in_pins];
            out = new byte[out_pins];
        }
    }
}
