import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Dictionary;
import java.util.Hashtable;
import utils.parsers.TransactionsParser;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionsParserTest {
    private TransactionsParser parser;
    private static final String delimiter = " ---> ";

    @BeforeEach
    public void setUp() {
        parser = TransactionsParser.getInstance();
        parser.clearData(); // Ensure clean state before each test
    }

    @Test
    public void testSingletonInstance() {
        TransactionsParser anotherParser = TransactionsParser.getInstance();
        assertSame(parser, anotherParser, "TransactionsParser should be a singleton");
    }

    @Test
    public void testParseValidDepositLine() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Notes", 0);
        headerMap.put("Total", 1);
        headerMap.put("Currency (Total)", 2);
        parser.setHeaderMap(headerMap);

        String line = "Deposit,500,USD";
        parser.parse(line);

        assertEquals(1, parser.getAllData().size(), "There should be one entry in allData");
        assertEquals("Deposit" + delimiter + "500 USD", parser.getAllData().get("1"), "Parsed deposit data should match");

        // Check summarized data for deposits
        assertTrue(parser.getSummarizedData().containsKey("USD"), "Summarized data should contain USD");
        assertEquals("500.00", parser.getSummarizedData().get("USD").get("totalDeposits"), "Total deposits should be updated correctly");
    }

    @Test
    public void testParseValidWithdrawalLine() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Notes", 0);
        headerMap.put("Total", 1);
        headerMap.put("Currency (Total)", 2);
        parser.setHeaderMap(headerMap);

        parser.parse("Withdraw,200,EUR");

        assertEquals(1, parser.getAllData().size(), "There should be one entry in allData");
        assertEquals("Withdraw" + delimiter + "200 EUR", parser.getAllData().get("1"), "Parsed withdrawal data should match");

        // Check summarized data for withdrawals
        assertTrue(parser.getSummarizedData().containsKey("EUR"), "Summarized data should contain EUR");
        assertEquals("200.00", parser.getSummarizedData().get("EUR").get("totalWithdrawals"), "Total withdrawals should be updated correctly");
    }

    @Test
    public void testParseInvalidLineWithoutHeaderMap() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            parser.parse("Deposit,300,GBP");
        });

        assertEquals("Header map not set for TransactionsParser", exception.getMessage());
    }

    @Test
    public void testParseMultipleLines() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Notes", 0);
        headerMap.put("Total", 1);
        headerMap.put("Currency (Total)", 2);
        parser.setHeaderMap(headerMap);

        parser.parse("Deposit,500,USD");
        parser.parse("Withdraw,200,EUR");
        parser.parse("Deposit,100,GBP");

        assertEquals(3, parser.getAllData().size(), "There should be three entries in allData");

        // Check summarized data for deposits and withdrawals
        assertTrue(parser.getSummarizedData().containsKey("USD"), "Summarized data should contain USD");
        assertEquals("500.00", parser.getSummarizedData().get("USD").get("totalDeposits"), "Total deposits in USD should be 500.00");
        
        assertTrue(parser.getSummarizedData().containsKey("EUR"), "Summarized data should contain EUR");
        assertEquals("200.00", parser.getSummarizedData().get("EUR").get("totalWithdrawals"), "Total withdrawals in EUR should be 200.00");

        assertTrue(parser.getSummarizedData().containsKey("GBP"), "Summarized data should contain GBP");
        assertEquals("100.00", parser.getSummarizedData().get("GBP").get("totalDeposits"), "Total deposits in GBP should be 100.00");
    }

    @Test
    public void testClearData() {
        Dictionary<String, Integer> headerMap = new Hashtable<>();
        headerMap.put("Notes", 0);
        headerMap.put("Total", 1);
        headerMap.put("Currency (Total)", 2);
        parser.setHeaderMap(headerMap);

        parser.parse("Deposit,500,USD");
        parser.parse("Withdraw,200,EUR");
        parser.clearData();

        assertTrue(parser.getAllData().isEmpty(), "All data should be cleared");
        assertTrue(parser.getSummarizedData().isEmpty(), "Summarized data should be cleared");
    }
}
