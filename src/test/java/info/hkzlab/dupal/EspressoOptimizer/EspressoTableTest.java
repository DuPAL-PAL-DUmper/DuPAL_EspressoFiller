package info.hkzlab.dupal.EspressoOptimizer;

import org.junit.Test;

import info.hkzlab.dupal.EspressoFiller.espresso.EspressoTable;
import info.hkzlab.dupal.EspressoFiller.espresso.EspressoTable.EspressoTableEntry;

import static org.junit.Assert.*;

public class EspressoTableTest 
{
    @Test
    public void shouldGenerateACorrectEspressoTable()
    {
        String expected;
        String actual;
       
        EspressoTableEntry entries[] = new EspressoTableEntry[] {
            new EspressoTableEntry(4, 2),
            new EspressoTableEntry(4, 2),
            new EspressoTableEntry(4, 2),
            new EspressoTableEntry(4, 2),
            new EspressoTableEntry(4, 2),
            new EspressoTableEntry(4, 2),
        };

        entries[0].in = new byte[] { 0, 0, 0, -1 };
        entries[0].out = new byte[] { 0, 1 };
        entries[1].in = new byte[] { 0, 0, 1, 0};
        entries[1].out = new byte[] { 1, -1 };
        entries[2].in = new byte[] { 0, 1, 0, 1};
        entries[2].out = new byte[] { 0, 1 };
        entries[3].in = new byte[] { -1, 1, 1, 0};
        entries[3].out = new byte[] { 0, 1 };
        entries[4].in = new byte[] { -1, 0, 0, 0};
        entries[4].out = new byte[] { 0, 0 };
        entries[5].in = new byte[] { 0, 1, 1, 0};
        entries[5].out = new byte[] { 1, 0 };
        
        expected = ".i 4\n" +
                   ".o 2\n" +
                   ".ilb i1 i2 i3 i4 \n" + 
                   ".ob o1 o2 \n" +
                   ".phase 01\n" +
                   "\n" +
                   "000- 01\n" +
                   "0010 1-\n" +
                   "0101 01\n" +
                   "-110 01\n" +
                   "-000 00\n" +
                   "0110 10\n" +
                   ".e\n";


        actual = (new EspressoTable(4, 2, new String[] {"i1", "i2", "i3", "i4"}, new String[] {"o1", "o2"}, new boolean[] {false , true}, entries, false, (byte)-1)).toString();

        assertEquals("Should generate a correct Espresso table", expected, actual);
    }

    @Test
    public void shouldCorrectlyMatchEntriesInTable() {
        EspressoTable table;
       
        EspressoTableEntry entries[] = new EspressoTableEntry[] {
            new EspressoTableEntry(4, 2),
            new EspressoTableEntry(4, 2),
            new EspressoTableEntry(4, 2),
            new EspressoTableEntry(4, 2),
            new EspressoTableEntry(4, 2),
            new EspressoTableEntry(4, 2),
        };

        entries[0].in = new byte[] { 0, 0, 0, -1 };
        entries[0].out = new byte[] { 0, 1 };
        entries[1].in = new byte[] { 0, 0, 1, 0};
        entries[1].out = new byte[] { 1, -1 };
        entries[2].in = new byte[] { 0, 1, 0, 1};
        entries[2].out = new byte[] { 0, 1 };
        entries[3].in = new byte[] { -1, 1, 1, 0};
        entries[3].out = new byte[] { 0, 1 };
        entries[4].in = new byte[] { -1, 0, 0, 0};
        entries[4].out = new byte[] { 0, 0 };
        entries[5].in = new byte[] { 0, 1, 1, 0};
        entries[5].out = new byte[] { 1, 0 };

        table = new EspressoTable(4, 2, new String[] {"i1", "i2", "i3", "i4"}, new String[] {"o1", "o2"}, new boolean[] {false , true}, entries, false, (byte)-1);

        assertTrue("Should correctly match input", table.match(0b0000));
        assertTrue("Should correctly match input", table.match(0b0001));
        assertTrue("Should correctly match input", table.match(0b0111));
        assertFalse("Should correctly not match input", table.match(0b0011));
    }
}
