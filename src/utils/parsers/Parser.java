package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

public abstract class Parser {
    protected static final Character delimiter = '_';
    public abstract void parse(String line);
    public abstract HashMap<String, String> getParsedData();
    protected abstract void clearParsedData();
    public abstract void setHeaderMap(Dictionary<String, Integer> headerMap);
}