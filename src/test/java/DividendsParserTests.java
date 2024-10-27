import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Dictionary;
import java.util.Hashtable;
import utils.parsers.DividendsParser;

import static org.junit.jupiter.api.Assertions.*;

public class DividendsParserTests {
    private DividendsParser parser;
    private static final String delimiter = " ---> ";

    @BeforeEach
    public void setUp() {
        parser = DividendsParser.getInstance();
        parser.clearData();
    }

    @Test
    public void testSingletonInstance() {
        DividendsParser anotherParser = DividendsParser.getInstance();
        assertSame(parser, anotherParser, "DividendsParser should be a singleton");
    }

    @Test
    public void testParseValidLine() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Name", 0);
        headerMap.put("Total", 1);
        headerMap.put("Currency (Total)", 2);
        parser.setHeaderMap(headerMap);

        String line = "Company A,100,USD";
        parser.parse(line);

        assertEquals(1, parser.getAllData().size(), "There should be one entry in allData");
        assertEquals("Company A" + delimiter + "100 USD", parser.getAllData().get("1"), "Parsed data should match");
        assertEquals("100,00", parser.getSummarizedData().get("totalDividends"), "Total dividends should be updated correctly");
    }

    @Test
    public void testParseInvalidLineWithoutHeaderMap() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            parser.parse("Company B,200,EUR");
        });

        assertEquals("Header map not set for DividendsParser", exception.getMessage());
    }

    @Test
    public void testClearData() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Name", 0);
        headerMap.put("Total", 1);
        headerMap.put("Currency (Total)", 2);
        parser.setHeaderMap(headerMap);

        parser.parse("Company A,100,USD");
        parser.clearData();

        assertTrue(parser.getAllData().isEmpty(), "All data should be cleared");
        assertEquals(0.0, parser.getTotalDividends(), 0.0001, "Total dividends should be reset to zero");
    }

    @Test
    public void testParseMultipleLines() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Name", 0);
        headerMap.put("Total", 1);
        headerMap.put("Currency (Total)", 2);
        parser.setHeaderMap(headerMap);

        parser.parse("Company A,100,USD");
        parser.parse("Company B,150,EUR");
        parser.parse("Company C,50,GBP");

        assertEquals(3, parser.getAllData().size(), "There should be three entries in allData");
        assertEquals("300,00", parser.getSummarizedData().get("totalDividends"), "Total dividends should be the sum of all entries");
    }
}
