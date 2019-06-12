package sebin.LogUtils;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VerifySimplePattern {
    private final String string;

    public VerifySimplePattern(final String string) {
        this.string = string;
    }

    boolean isNumberOfSimplePatternsMatches() {
        final String stringWithoutQuotes = new StringWithoutQuotes(string).toString();
        final List<String> variables = Arrays.asList(stringWithoutQuotes.split(","))
                .stream()
                .map(var -> var.trim())
                .filter(var -> StringUtils.isNotBlank(var))
                .collect(Collectors.toList());
        final int numberOfSimplePatterns = StringUtils.countMatches(string, "{}");
        if (numberOfSimplePatterns != variables.size()) {
//            System.out.println(string);
//            System.out.println(variables);
            return false;
        }
        return true;
    }

    boolean isNumberOfSimplePatternsMatchesWithConcat() {
        final String stringWithoutQuotes = new StringWithoutQuotes(string).toString();
        if(string.equals(stringWithoutQuotes)){
            return true;
        }
        final List<String> variables = Arrays.asList(stringWithoutQuotes.split(","))
                .stream()
                .map(var -> var.trim())
                .filter(var -> (StringUtils.isNotBlank(var) && var.indexOf("+")==-1))
                .collect(Collectors.toList());
        final int numberOfSimplePatterns = StringUtils.countMatches(string, "{}");
        if (numberOfSimplePatterns != variables.size()) {
//            System.out.println(string);
//            System.out.println(variables);
            return false;
        }
        return true;
    }
}