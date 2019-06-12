package sebin.LogUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringWithoutQuotes {
    private final static Pattern PATTERN_WITH_QUOTES = Pattern.compile("(\"([^\"]|\"\")*\")");
    private final String string;

    public StringWithoutQuotes(final String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return stripQuotedStrings();
    }
    private String stripQuotedStrings() {
        String newString = string;
        final Matcher matcher = PATTERN_WITH_QUOTES.matcher(string);
        while (matcher.find()){
            newString = newString.replace(matcher.group(),"");
        }
        return newString;
    }

}
