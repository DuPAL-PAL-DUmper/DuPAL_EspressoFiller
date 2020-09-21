package info.hkzlab.dupal.EspressoOptimizer;

import org.junit.Test;

import info.hkzlab.dupal.EspressoOptimizer.espresso.EspressoTable;

import static org.junit.Assert.*;

public class EspressoTableTest 
{
    @Test
    public void shouldGenerateACorrectEspressoTable()
    {
        String expected;
        String actual;
        
        byte[][] entries = new byte[][] {
            new byte[] {0, -1},
            null,
            new byte[] {1, 1},
            new byte[] {0, 0},
            new byte[] {1, 0},
            new byte[] {1, 1},
            new byte[] {-1, 0},
            new byte[] {0, 0},
            new byte[] {0, 1},
            new byte[] {0, 1},
            new byte[] {-1, 0},
            new byte[] {0, 0},
            new byte[] {-1, 0},
            new byte[] {1, 1},
            null,
            new byte[] {0, 0},
        };
          
        expected = ".i 4\n" +
                   ".o 2\n" +
                   ".ilb i1 i2 i3 i4 \n" + 
                   ".ob o1 o2 \n" +
                   ".phase 01\n" +
                   "\n" +
                   "0000 0-\n" +
                   "0100 11\n" +
                   "1100 00\n" +
                   "0010 10\n" +
                   "1010 11\n" +
                   "0110 -0\n" +
                   "1110 00\n" +
                   "0001 01\n" +
                   "1001 01\n" +
                   "0101 -0\n" +
                   "1101 00\n" +
                   "0011 -0\n" +
                   "1011 11\n" +
                   "1111 00\n" +
                   ".e\n";

        actual = (new EspressoTable(4, 2, new String[] {"i1", "i2", "i3", "i4"}, new String[] {"o1", "o2"}, new boolean[] {false , true}, entries)).toString();

        assertEquals("Should generate a correct Espresso table", expected, actual);
    }
}
