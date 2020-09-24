package info.hkzlab.dupal.EspressoOptimizer.espresso;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import info.hkzlab.dupal.EspressoOptimizer.espresso.EspressoTable.EspressoTableEntry;

public class TableParser {
    private TableParser() {
    };

    public static EspressoTable readTableFromBuffer(BufferedReader bufr, boolean expand) throws IOException {
        int inputs = 0;
        int outputs = 0;
        String[] input_labels = null;
        String[] output_labels = null;
        boolean[] phase = null;
        ArrayList<EspressoTableEntry> entries = new ArrayList<>();

        String line;
        while((line = bufr.readLine()) != null) {
            line = line.trim();

            if(line.length() == 0) continue;

            if(line.startsWith(".e")) break; // This is the footer
            else if(line.startsWith("#")) continue;
            else if(line.startsWith(".i ")) {
                line = line.substring(2).trim();
                inputs = Integer.parseInt(line);
            } else if(line.startsWith(".o ")) {
                line = line.substring(2).trim();
                outputs = Integer.parseInt(line);
            } else if(line.startsWith(".p ")) {
                // Ignore
            } else if(line.startsWith(".ilb ")) {
                line = line.substring(4).trim();
                String[] labels = line.split(" ");
                assert(labels.length == inputs);
                input_labels = labels;
            } else if(line.startsWith(".ob ")) {
                line = line.substring(3).trim();
                String[] labels = line.split(" ");
                assert(labels.length == outputs);
                output_labels = labels;
            } else if(line.startsWith(".phase ")) {
                line = line.substring(6).trim();
                assert(line.length() == outputs);
                phase = new boolean[outputs];
                int ph_idx = 0;
                for (char ch : line.toCharArray()) {
                    phase[ph_idx] = ch == '0' ? false : true;
                    ph_idx++;
                }
            } else { // Table entry
                EspressoTableEntry tabEntry = new EspressoTableEntry(inputs, outputs);

                String[] table_entry = line.split(" ");
                assert(table_entry.length == 2);
                assert(table_entry[0].length() == inputs);
                assert(table_entry[1].length() == outputs);

                int adr_idx = 0;
                for(char ch : table_entry[0].toCharArray()) {
                    if(ch == '-') tabEntry.in[adr_idx] = (byte)-1;
                    else tabEntry.in[adr_idx] = (byte)((ch == '0') ? 0 : 1);
                    adr_idx++;
                }

                adr_idx = 0;
                for(char ch : table_entry[1].toCharArray()) {
                    if(ch == '-') tabEntry.out[adr_idx] = (byte)-1;
                    else tabEntry.out[adr_idx] = (byte)((ch == '0') ? 0 : 1);
                    adr_idx++;
                }

                entries.add(tabEntry);
            }
        }
        
        return new EspressoTable(inputs, outputs, input_labels, output_labels, phase, entries.toArray(new EspressoTableEntry[entries.size()]), expand);
    }
}
