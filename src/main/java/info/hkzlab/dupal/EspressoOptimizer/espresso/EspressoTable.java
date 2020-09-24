package info.hkzlab.dupal.EspressoOptimizer.espresso;

import info.hkzlab.dupal.EspressoOptimizer.utilities.BitUtils;

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
            if(phase != null) {
            strBuf.append(".phase ");
            for(boolean p : phase) strBuf.append(p ? '1' : '0');
            strBuf.append('\n');
        }

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

    public boolean match(int input) {
        for(int idx = 0; idx < entries.length; idx++) {
            if(entries[idx] != null) {
                int[] expEntr = expandAddress(entries[idx].in);
                for(int entr : expEntr) if(entr == input) return true;
            }
        }

        return false;
    }

    public EspressoTable copyTable() {
        return new EspressoTable(inputs, outputs, input_labels.clone(), output_labels.clone(), phase.clone(), entries.clone());
    }

    public static int[] expandAddress(byte[] input) {
        int base = 0, dontcare_mask = 0;

        for(int idx = 0; idx < input.length; idx++) {
            if(input[idx] < 0) dontcare_mask |= (1 << idx);
            else base |= (input[idx] << idx);
        }

        int maxVal = BitUtils.consolidateBitField(dontcare_mask, dontcare_mask);
        int[] addrs = new int[maxVal + 1];

        for(int idx = 0; idx <= maxVal; idx++) {
            addrs[idx] = base | BitUtils.scatterBitField(idx, dontcare_mask);
        }

        return addrs;
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
