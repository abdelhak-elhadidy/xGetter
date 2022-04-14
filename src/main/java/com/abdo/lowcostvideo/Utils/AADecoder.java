package com.abdo.lowcostvideo.Utils;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adapted from:
 * https://github.com/madushan1000/jdtrunk/blob/00b2e9b3b18124ed39ece74b24afe6f08007b23f/src/org/jdownloader/encoding/AADecoder.java
 */
public class AADecoder {
    private static final String HEX_HASH_MARKER = "(oﾟｰﾟo)+ ";
    private static final String BLOCK_START_MARKER = "(ﾟДﾟ)[ﾟεﾟ]+";

    private static final String[] BYTES = new String[]{"(c^_^o)", "(ﾟΘﾟ)", "((o^_^o) - (ﾟΘﾟ))", "(o^_^o)", "(ﾟｰﾟ)", "((ﾟｰﾟ) + (ﾟΘﾟ))", "((o^_^o) +(o^_^o))", "((ﾟｰﾟ) + (o^_^o))", "((ﾟｰﾟ) + (ﾟｰﾟ))", "((ﾟｰﾟ) + (ﾟｰﾟ) + (ﾟΘﾟ))", "(ﾟДﾟ) .ﾟωﾟﾉ", "(ﾟДﾟ) .ﾟΘﾟﾉ", "(ﾟДﾟ) ['c']", "(ﾟДﾟ) .ﾟｰﾟﾉ", "(ﾟДﾟ) .ﾟДﾟﾉ", "(ﾟДﾟ) [ﾟΘﾟ]"};

    private static final String AA_PREFIX = "ﾟωﾟﾉ= /｀ｍ´）ﾉ ~┻━┻   //*´∇｀*/ ['_']; o=(ﾟｰﾟ)  =_=3; c=(ﾟΘﾟ) =(ﾟｰﾟ)-(ﾟｰﾟ); ";
    private static final String AA_SUFFIX = "(ﾟДﾟ)[ﾟoﾟ]) (ﾟΘﾟ)) ('_');";

    public static boolean DEBUG = false;

    private AADecoder(){
        // Don't allow instantiation.
    }

    public static boolean isAAEncoded(String js) {
        if (!js.startsWith(AA_PREFIX)) {
            return false;
        }
        if (!js.endsWith(AA_SUFFIX)) {
            return false;
        }
        return true;
    }

    public static ArrayList<String> extractAllAAEncodedStrings(String js){
        int start, end;
        ArrayList<String> scripts = new ArrayList<>();
        while ((start = js.indexOf(AA_PREFIX)) != -1) {
            if((end = js.indexOf(AA_SUFFIX)) != -1) {
                end = end + AA_SUFFIX.length();
                scripts.add(js.substring(start, end));
                if(end == js.length()) {
                    break;
                }
                js = js.substring(end+1);
            }
        }
        return scripts;
    }

    // Long version. Contains additional info, but lot's of unused variables that may be important for botguard
    public static String decode(String js) throws Exception {
        js = js.replace("/*´∇｀*/", "");
        // trim
        js = js.replaceAll("^\\s+|\\s+$", "");

        String data = getPatternMatch("\\(ﾟДﾟ\\)\\[ﾟoﾟ\\]\\+ (.+?)\\(ﾟДﾟ\\)\\[ﾟoﾟ\\]\\)", js, 1);

        String out = "";
        while (!isEmpty(data)) {
            int index = data.indexOf(BLOCK_START_MARKER);
            if (index != 0) {
                throw new Exception("No AAEncode");
            }
            data = data.substring(BLOCK_START_MARKER.length());
            String encodedBlock = null;
            index = data.indexOf(BLOCK_START_MARKER);
            if (index == -1) {
                encodedBlock = data;
                data = "";
            } else {
                encodedBlock = data.substring(0, index);
                data = data.substring(encodedBlock.length());
            }

            int radix = 8;
            if (encodedBlock.indexOf(HEX_HASH_MARKER) == 0) {
                encodedBlock = encodedBlock.substring(HEX_HASH_MARKER.length());
                radix = 16;
            }
            if (DEBUG) {
                System.out.println("Rad " + radix);
            }
            String uniCodeNumString = decodeBlock(encodedBlock, radix);
            if (isEmpty(uniCodeNumString)) {
                throw new RuntimeException("Bad decoding for " + encodedBlock);
            }
            out += Character.toString((char) Integer.parseInt(uniCodeNumString, radix));
            if (DEBUG) {
                System.out.println(out);
            }
        }
        return out;
    }

    private static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    private static String getPatternMatch(String pattern, String input, int group) {
        Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(input);
        matcher.reset();
        if (matcher.find()) {
            return matcher.group(group);
        }
        return null;
    }

    // http://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form
    // public domain
    private static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                this.ch = (++this.pos < str.length()) ? str.charAt(this.pos) : -1;
            }

            boolean eat(int charToEat) {
                while (this.ch == ' ') {
                    this.nextChar();
                }
                if (this.ch == charToEat) {
                    this.nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                this.nextChar();
                double x = this.parseExpression();
                if (this.pos < str.length()) {
                    throw new RuntimeException("Unexpected: " + (char) this.ch);
                }
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = this.parseTerm();
                for (; ; ) {
                    if (this.eat('+')) {
                        x += this.parseTerm(); // addition
                    } else if (this.eat('-')) {
                        x -= this.parseTerm(); // subtraction
                    } else if (this.eat('~')) {
                        x -= this.parseTerm(); // subtraction

                    } else {
                        return x;
                    }
                }
            }

            double parseTerm() {
                double x = this.parseFactor();
                for (; ; ) {
                    if (this.eat('*')) {
                        x *= this.parseFactor(); // multiplication
                    } else if (this.eat('/')) {
                        x /= this.parseFactor(); // division
                    } else if (this.eat('~')) {
                        double pf = this.parseFactor();
                        x = ~(int) Math.floor(x); // division
                    } else {
                        return x;
                    }
                }
            }

            double parseFactor() {
                if (this.eat('+')) {
                    return this.parseFactor(); // unary plus
                }
                if (this.eat('-')) {
                    return -this.parseFactor(); // unary minus
                }
                if (this.eat('~')) {
                    return ~(int) Math.floor(this.parseFactor()); // unary minus
                }
                double x;
                int startPos = this.pos;
                if (this.eat('(')) { // parentheses
                    x = this.parseExpression();
                    this.eat(')');
                } else if ((this.ch >= '0' && this.ch <= '9') || this.ch == '.') { // numbers
                    while ((this.ch >= '0' && this.ch <= '9') || this.ch == '.') {
                        this.nextChar();
                    }
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (this.ch >= 'a' && this.ch <= 'z') { // functions
                    while (this.ch >= 'a' && this.ch <= 'z') {
                        this.nextChar();
                    }
                    String func = str.substring(startPos, this.pos);
                    x = this.parseFactor();
                    if (func.equals("sqrt")) {
                        x = Math.sqrt(x);
                    } else if (func.equals("sin")) {
                        x = Math.sin(Math.toRadians(x));
                    } else if (func.equals("cos")) {
                        x = Math.cos(Math.toRadians(x));
                    } else if (func.equals("tan")) {
                        x = Math.tan(Math.toRadians(x));
                    } else {
                        throw new RuntimeException("Unknown function: " + func);
                    }
                } else {
                    throw new RuntimeException("Unexpected: " + (char) this.ch);
                }

                if (this.eat('^')) {
                    x = Math.pow(x, this.parseFactor()); // exponentiation
                }

                return x;
            }
        }.parse();
    }

    private static String decodeBlock(String encodedBlock, int radix) {
        for (int i = 0; i < BYTES.length; i++) {
            encodedBlock = encodedBlock.replace(BYTES[i], String.valueOf(i));
        }

        StringBuilder exp = new StringBuilder();
        ArrayList<String> expressions = new ArrayList<String>();
        int braceCount = 0;

        for (int i = 0; i < encodedBlock.length(); i++) {
            char c = encodedBlock.charAt(i);
            if (c == '(') {
                if (exp.length() > 0 && braceCount == 0) {
                    expressions.add(exp.toString());
                    exp.setLength(0);
                }
                braceCount++;
                if (!Character.isWhitespace(c)) {
                    exp.append(c);
                }
            } else if (c == ')') {
                braceCount--;
                if (!Character.isWhitespace(c)) {
                    exp.append(c);
                }
            } else {
                if (!Character.isWhitespace(c)) {
                    exp.append(c);
                } else if (i > 0 && c == ' ' && encodedBlock.charAt(i - 1) == '+') {
                    // block end
                    if (braceCount == 0) {
                        if (exp.length() > 0) {
                            expressions.add(exp.toString());
                            exp.setLength(0);
                        }
                    }

                }
            }
        }
        if (exp.length() > 0) {
            expressions.add(exp.toString());
            exp.setLength(0);
        }

        String ret = "";
        for (String expression : expressions) {
            expression = expression.trim().replaceAll("\\+$", "");

            ret += Integer.toString((int) eval(expression), radix);
        }
        if (DEBUG) {
            System.out.println(ret + " - " + encodedBlock);
        }
        return ret;
    }
}