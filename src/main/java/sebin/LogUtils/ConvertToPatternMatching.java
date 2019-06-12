package sebin.LogUtils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConvertToPatternMatching {

    private final String string;

    private final static String REGEX = "(\"([^\"]|\"\")*\")\\s*?\\+.*?(\\+|\\n|\\r\\n|$)";
    private final static Pattern PATTERN = Pattern.compile(REGEX);

    private final static String REGEX_JOIN_STRINGS = "\"\\s*\\+\\s*\"";
    private final static Pattern PATTERN_JOIN_STRINGS = Pattern.compile(REGEX_JOIN_STRINGS);

    private final static Pattern PATTERN_WITH_QUOTES = Pattern.compile("(\"([^\"]|\"\")*\")");
    private final static Pattern PATTERN_PLUS_SIGNS = Pattern.compile("\\+.*?(\\+|\\n|\\r\\n|$)");

    public ConvertToPatternMatching(final String string) {
        this.string = string;
//        System.out.println(string);
    }

    public String convert() {
//        System.out.println("--1--" + string + "--1--");

        if(checkForProcessing(string)){
            final String convertedString = convertToPattern();
            final Matcher matcher = PATTERN_JOIN_STRINGS.matcher(convertedString);
            final String finalString = matcher.replaceAll("");
            //        System.out.println("--2--" + finalString + "--2--");
            return finalString.replaceAll("(\\{\\})([a-z]|[A-Z])","$1 $2");
        }else{
            return string;
        }

    }

    private boolean checkForProcessing(final String string) {
        final Matcher matcher = PATTERN_WITH_QUOTES.matcher(string);
        final List<String> quotedStrings = new ArrayList<>();
        while (matcher.find()){
            quotedStrings.add(matcher.group());
        }

        if(quotedStrings.size()==0){
            return true;
        }
//        System.out.println(quotedStrings);

        String stringWithoutQuotes = string;
        for (String quotedString : quotedStrings) {
            stringWithoutQuotes = stringWithoutQuotes.replace(quotedString,"");
        }
//        System.out.println(stringWithoutQuotes);
        if(StringUtils.isEmpty(stringWithoutQuotes)){
            return true;
        }
//        final List<String> variables = Arrays.asList(stringWithoutQuotes.split(","));
//        System.out.println(variables);
//        int numberOfVariables = 0;
//
//        for (String variable : variables) {
//            if(!variable.contains("+")){
//                numberOfVariables++;
//            }
//        }
//        System.out.println(numberOfVariables + "---" + variables.size());
//        if(numberOfVariables >0){
//            throw new RuntimeException("Possible Throwable or Simple Pattern Matching use in the file: in the log:: " + string);
//        }

            return true;
    }

    private String convertToPattern() {
        final VerifySimplePattern verifySimplePattern = new VerifySimplePattern(string);
        if(!verifySimplePattern.isNumberOfSimplePatternsMatchesWithConcat()){
            throw new RuntimeException("Possible Throwable or Simple Pattern Matching use in the file: in the log:: " + string);
        }
        if(verifySimplePattern.isNumberOfSimplePatternsMatches()){
            return string;
        }
        final StringWithoutQuotes stringWithoutQuotes = new StringWithoutQuotes(string);
        if(string.indexOf("{}") != -1 &&
                StringUtils.isNotBlank(stringWithoutQuotes.toString()) &&
                stringWithoutQuotes.toString().indexOf("+") != -1)
        {
            throw new RuntimeException("Possible mix of + and Simple Pattern Matching the log:: " + string);
        }
//        System.out.println("\n----------------------");
//        System.out.println(string);
//        System.out.println(REGEX);
        final Matcher matcher = PATTERN.matcher(string);
        final StringBuilder builder = new StringBuilder();
        final List<String> variables = new ArrayList<String>();
        int lastEndIndex = 0;

        while (matcher.find()) {
//            System.out.println(matcher.start() + " ---- " + matcher.end());
            final String gapBetweenMatches = string.substring(lastEndIndex, matcher.start()).trim();

//            System.out.println(" ----" + gapBetweenMatches +"----" );
            if(!StringUtils.isBlank(gapBetweenMatches)){
                final String startString = string.substring(lastEndIndex, matcher.start());
//                System.out.println("0:"+ startString);
                final List<String> strings = Arrays.asList(startString.split("\\+"))
                        .stream()
                        .map(string -> string.trim())
                        .filter(string -> StringUtils.isNotEmpty(string))
                        .map(string -> stripToString(string))
                        .collect(Collectors.toList());
                for (String string : strings) {
                    builder.append("\"{}\" + ");

                }
                variables.addAll(strings);
//                System.out.println(variables);
            }
            final String partOne = matcher.group();
//                        System.out.println("1:"+ partOne);
            final String variable = stripPlusSigns(partOne);
            variables.add(stripToString(variable));
//                        System.out.println("2:"+ variable);
            final String message = textWithQuotesWithSimplePattern(partOne,true);
//            System.out.println("3"+ message);
            builder.append(message + " + ");

            lastEndIndex = matcher.end();
        }
        final String lastPart = string.substring(lastEndIndex).trim();
//        System.out.println("4:"+lastPart);

        if (StringUtils.isBlank(lastPart)) { // remove the last +
            final String finalString = builder.toString();
//            System.out.println(finalString + "\n::Length::" + finalString.length());
            return appendPatternVariables(finalString.substring(0, finalString.length() - 2), variables);

        } else {
            if(string.equals(lastPart) && string.startsWith("\"") && string.endsWith("\"")){
                return string;
            }
            if(string.indexOf("\"") == -1 && string.indexOf(".toString()") == -1){
                return string;
            }
            String lastQuotedString = textWithQuotesWithSimplePattern(lastPart,false);
//            System.out.println("5:" + lastQuotedString);
            if(lastPart.indexOf("+") == -1 && lastQuotedString.length()>3){
                lastQuotedString = "\"" + lastQuotedString.substring(3,lastQuotedString.length());
//                System.out.println("5.1:" + lastQuotedString);
            }

            final String lastStringWithoutQuotes = new StringWithoutQuotes(lastPart).toString();
//            System.out.println("6:" + lastStringWithoutQuotes);

            final List<String> lastVariables = Arrays.asList(lastStringWithoutQuotes.split("\\+"))
                    .stream()
                    .filter(var -> StringUtils.isNotBlank(var))
                    .map(string -> stripToString(string))
                    .collect(Collectors.toList());
//            System.out.println("7:" + lastVariables);
//            System.out.println("7.1:" + builder.toString());

            if(StringUtils.isNotBlank(lastQuotedString)){
                if(lastVariables.size()> 1){
                    for(int i = 1;i < lastVariables.size();i++){
                        builder.append("\"{}\" + ");
                    }
                }
                builder.append(lastQuotedString );
            }
            if(StringUtils.isBlank(lastQuotedString)){
                if(lastVariables.size()> 0){
                    for(int i = 0;i < lastVariables.size();i++){
                        builder.append("\"{}\" + ");
                    }
                    builder.replace(builder.length()-3,builder.length(),""); // Remove the last +
                }
            }

            variables.addAll(lastVariables);


//            variables.add(stripToString(lastPart));

            return appendPatternVariables(builder.toString(), variables);
        }
    }


    private String stripToString(final String string) {
        final int index = string.indexOf(".toString()");
        return string.substring(0,
                (index >0?index:string.length()));
    }

    private String appendPatternVariables(final String string, final List<String> variables) {
        final String separator = " , ";
        final String commaSeparated = String.join(separator, variables);
        if(StringUtils.isBlank(commaSeparated)){
            return string;
        }
        return string + separator +commaSeparated;
    }
    private String textWithQuotesWithSimplePattern(final String string, boolean patternAtEnd) {
//        System.out.println(PATTERN_WITH_QUOTES);
//        System.out.println("+++"+string);
        final Matcher matcher = PATTERN_WITH_QUOTES.matcher(string);
        if (matcher.find()){
            if(patternAtEnd){
                return addPatternStringAtTheEnd(matcher.group().trim());
            }else{
                return addPatternStringAtTheStart(matcher.group().trim());
            }
        }
        return "";
    }


    private String stripPlusSigns(final String string) {
//        System.out.println(PATTERN_WITH_QUOTES);
        final Matcher matcher = PATTERN_WITH_QUOTES.matcher(string);
        if (matcher.find()){
            final String stringWithoutQuotes = string.replace(matcher.group(), "");
//            System.out.println("QQ:" + stringWithoutQuotes);
//            System.out.println(PATTERN_PLUS_SIGNS);
            final Matcher matcherPlus = PATTERN_PLUS_SIGNS.matcher(stringWithoutQuotes);
            if (matcherPlus.find()){
                return matcherPlus.group().replaceAll("\\+","").trim();
            }

        }
        return "";
    }

    private String addPatternStringAtTheStart(final String string) {
        final String tempString = string.replaceAll("^\"([a-z]|[A-Z])", "\" $1");
        return tempString
                .replaceAll("^\"","\"{}");
    }

    private static String addPatternStringAtTheEnd(final String string) {
        final String tempString = string.replaceAll("([a-z]|[A-Z])\"$", "$1 \"");
        return tempString
                .replaceAll("\"$","{}\"");

    }

    public static void main(String args[]){

        System.out.println("\n----------------------");

        final String string = "QUERY + query.toString() + \" [annualRecurrentRedPayRateBandId: {}]\", annualRecurrentRedPayRateBandId";
        System.out.println(string + "---" +
                new ConvertToPatternMatching(string).convert());

    }


}