import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.parsers.OrdersParser;

import java.util.Dictionary;
import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

public class OrdersParserTest {
    private OrdersParser parser;
    private static final String delimiter = " ---> ";

    @BeforeEach
    public void setUp() {
        parser = OrdersParser.getInstance();
        parser.clearData(); // Ensure clean state before each test
    }

    @Test
    public void testSingletonInstance() {
        OrdersParser anotherParser = OrdersParser.getInstance();
        assertSame(parser, anotherParser, "OrdersParser should be a singleton");
    }

    @Test
    public void testParseValidBuyLine() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Action", 0);
        headerMap.put("Name", 1);
        headerMap.put("Total", 2);
        headerMap.put("Currency (Total)", 3);
        parser.setHeaderMap(headerMap);

        String line = "buy,Company A,100,USD";
        parser.parse(line);

        assertEquals(1, parser.getAllData().size(), "There should be one entry in allData");
        assertEquals("buy" + delimiter + "Company A" + delimiter + "100 USD", parser.getAllData().get("1"), "Parsed buy data should match");
        assertEquals("100,00", parser.getSummarizedData().get("totalExpenses"), "Total expenses should be updated correctly");
    }

    @Test
    public void testParseValidSellLine() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Action", 0);
        headerMap.put("Name", 1);
        headerMap.put("Total", 2);
        headerMap.put("Currency (Total)", 3);
        parser.setHeaderMap(headerMap);

        parser.parse("sell,Company B,200,EUR");

        assertEquals(1, parser.getAllData().size(), "There should be one entry in allData");
        assertEquals("sell" + delimiter + "Company B" + delimiter + "200 EUR", parser.getAllData().get("1"), "Parsed sell data should match");
        assertEquals("200,00", parser.getSummarizedData().get("totalIncome"), "Total income should be updated correctly");
    }

    @Test
    public void testParseInvalidLineWithoutHeaderMap() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            parser.parse("buy,Company C,300,GBP");
        });

        assertEquals("Header map not set for OrdersParser", exception.getMessage());
    }

    @Test
    public void testParseInvalidActionType() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Action", 0);
        headerMap.put("Name", 1);
        headerMap.put("Total", 2);
        headerMap.put("Currency (Total)", 3);
        parser.setHeaderMap(headerMap);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            parser.parse("invalid,Company D,400,USD");
        });

        assertEquals("Invalid action type in OrdersParser", exception.getMessage());
    }

    @Test
    public void testClearData() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Action", 0);
        headerMap.put("Name", 1);
        headerMap.put("Total", 2);
        headerMap.put("Currency (Total)", 3);
        parser.setHeaderMap(headerMap);

        parser.parse("buy,Company A,100,USD");
        parser.parse("sell,Company B,200,EUR");
        parser.clearData();

        assertTrue(parser.getAllData().isEmpty(), "All data should be cleared");
        assertEquals(0.0, parser.getTotalIncome(), 0.0001, "Total income should be reset to zero");
        assertEquals(0.0, parser.getTotalExpenses(), 0.0001, "Total expenses should be reset to zero");
        assertEquals(0.0, parser.getTotalProfit(), 0.0001, "Total profit should be reset to zero");
    }

    @Test
    public void testParseMultipleLines() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Action", 0);
        headerMap.put("Name", 1);
        headerMap.put("Total", 2);
        headerMap.put("Currency (Total)", 3);
        parser.setHeaderMap(headerMap);

        parser.parse("buy,Company A,100,USD");
        parser.parse("sell,Company B,200,EUR");
        parser.parse("buy,Company C,50,GBP");

        assertEquals(3, parser.getAllData().size(), "There should be three entries in allData");
        assertEquals("200,00", parser.getSummarizedData().get("totalIncome"), "Total income should be updated correctly");
        assertEquals("150,00", parser.getSummarizedData().get("totalExpenses"), "Total expenses should be updated correctly");
        assertEquals("50,00", parser.getSummarizedData().get("totalProfit"), "Total profit should be calculated correctly");
    }
}
