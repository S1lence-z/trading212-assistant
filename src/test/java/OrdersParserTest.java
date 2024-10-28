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
        assertEquals("100.00", parser.getSummarizedData().get("USD").get("totalExpenses"), "Total expenses should be updated correctly for USD");
        assertEquals("0.00", parser.getSummarizedData().get("USD").get("totalIncome"), "Total income should remain zero for USD");
        assertEquals("-100.00", parser.getSummarizedData().get("USD").get("totalProfit"), "Total profit should be calculated correctly after a buy order");
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
        assertEquals("0.00", parser.getSummarizedData().get("EUR").get("totalExpenses"), "Total expenses should remain zero for EUR");
        assertEquals("200.00", parser.getSummarizedData().get("EUR").get("totalIncome"), "Total income should be updated correctly for EUR");
        assertEquals("200.00", parser.getSummarizedData().get("EUR").get("totalProfit"), "Total profit should be calculated correctly after a sell order");
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
        assertTrue(parser.getSummarizedData().isEmpty(), "Summarized data should be cleared");
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
        assertEquals("200.00", parser.getSummarizedData().get("EUR").get("totalIncome"), "Total income for EUR should be updated correctly");
        assertEquals("100.00", parser.getSummarizedData().get("USD").get("totalExpenses"), "Total expenses for USD should be updated correctly");
        assertEquals("50.00", parser.getSummarizedData().get("GBP").get("totalExpenses"), "Total expenses for GBP should remain zero");
        assertEquals("-100.00", parser.getSummarizedData().get("USD").get("totalProfit"), "Total profit for USD should be calculated correctly");
        assertEquals("200.00", parser.getSummarizedData().get("EUR").get("totalProfit"), "Total profit for EUR should be calculated correctly");
    }
}
