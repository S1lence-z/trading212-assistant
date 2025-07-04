import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Dictionary;
import java.util.Hashtable;
import utils.parsers.InterestParser;

import static org.junit.jupiter.api.Assertions.*;

public class InterestParserTests {
    private InterestParser parser;
    private static final String delimiter = " ---> ";

    @BeforeEach
    public void setUp() {
        parser = InterestParser.getInstance();
        parser.clearData(); // Ensure clean state before each test
    }

    @Test
    public void testSingletonInstance() {
        InterestParser anotherParser = InterestParser.getInstance();
        assertSame(parser, anotherParser, "InterestParser should be a singleton");
    }

    @Test
    public void testParseValidLine() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Notes", 0);
        headerMap.put("Total", 1);
        headerMap.put("Currency (Total)", 2);
        parser.setHeaderMap(headerMap);

        String line = "Interest from savings,150,USD";
        parser.parse(line);

        assertEquals(1, parser.getAllData().size(), "There should be one entry in allData");
        assertEquals("Interest from savings" + delimiter + "150 USD", parser.getAllData().get("1"), "Parsed data should match");
        assertEquals("150.00", parser.getSummarizedData().get("USD"), "Total interest for USD should be formatted correctly");
    }

    @Test
    public void testParseInvalidLineWithoutHeaderMap() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            parser.parse("Interest from loan,200,EUR");
        });

        assertEquals("Header map not set for InterestParser", exception.getMessage());
    }

    @Test
    public void testClearData() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Notes", 0);
        headerMap.put("Total", 1);
        headerMap.put("Currency (Total)", 2);
        parser.setHeaderMap(headerMap);

        parser.parse("Interest from savings,150,USD");
        parser.clearData();

        assertTrue(parser.getAllData().isEmpty(), "All data should be cleared");
        assertEquals(0, parser.getSummarizedData().size(), "Summarized data should be cleared");
    }

    @Test
    public void testParseMultipleLines() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Notes", 0);
        headerMap.put("Total", 1);
        headerMap.put("Currency (Total)", 2);
        parser.setHeaderMap(headerMap);

        parser.parse("Interest from savings,150,USD");
        parser.parse("Interest from loan,200,EUR");
        parser.parse("Interest from bonds,50,CZK");

        assertEquals(3, parser.getAllData().size(), "There should be three entries in allData");
        assertEquals("150.00", parser.getSummarizedData().get("USD"), "Total interest for USD should be formatted correctly");
        assertEquals("200.00", parser.getSummarizedData().get("EUR"), "Total interest for EUR should be formatted correctly");
        assertEquals("50.00", parser.getSummarizedData().get("CZK"), "Total interest for CZK should be formatted correctly");
    }
}
