package info.hkzlab.dupal.EspressoOptimizer;

import org.junit.Test;

import info.hkzlab.dupal.EspressoOptimizer.espresso.TableParser;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class TableParserTest {
    @Test
    public void shouldCorrectlyParseValidEspressoTable() throws IOException
    {
        String inputTable = ".i 4\n" +
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

        BufferedReader bufr = new BufferedReader(new StringReader(inputTable));
        String outTable = TableParser.readTableFromBuffer(bufr).toString();

        assertEquals("Should correctly parse a valid Espresso table", inputTable, outTable);
    }

        @Test
    public void shouldCorrectlyRebuildEspressoTable() throws IOException
    {
        String inputTable = ".i 3\n" +
                   ".o 2\n" +
                   ".ilb i1 i2 i3 \n" + 
                   ".ob o1 o2 \n" +
                   ".phase 01\n" +
                   "\n" +
                   "--- 00\n" +
                   ".e\n";
        
        String expectedTable = ".i 3\n" +
                               ".o 2\n" +
                               ".ilb i1 i2 i3 \n" + 
                               ".ob o1 o2 \n" +
                               ".phase 01\n" +
                               "\n" +
                               "000 00\n" +
                               "100 00\n" +
                               "010 00\n" +
                               "110 00\n" +
                               "001 00\n" +
                               "101 00\n" +
                               "011 00\n" +
                               "111 00\n" +
                               ".e\n";

        BufferedReader bufr = new BufferedReader(new StringReader(inputTable));
        String outTable = TableParser.readTableFromBuffer(bufr).toString();

        assertEquals("Should correctly rebuild an Espresso table with 'don't care' inputs", expectedTable, outTable);
    }
}
