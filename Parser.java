import java.math.BigDecimal;

/**
 * Created by alexe_000 on 11.02.2018.
 */
public class Parser {

    //  Declaration of lexemes
    final int NONE = 0;         //  FAIL
    final int DELIMITER = 1;    //  Delimiter(+-*/^=, ")", "(" )
    final int VARIABLE = 2;     //  Variable
    final int NUMBER = 3;       //  Number

    //  Declaration of error constants
    final int SYNTAXERROR = 0;  //  Syntax error (10 + 5 6 / 1)
    final int UNBALPARENS = 1;  //  Mismatch of the number of open and closed brackets
    final int NOEXP = 2;        //  Missing expression when starting the analyzer
    final int DIVBYZERO = 3;    //  Division by zero

    //  A lexeme that defines the end of an expression
    final String EOF = "\0";

    private String exp;     //  A reference to a string with an expression
    private int explds;     //  Current index in expression
    private String token;   //  Saving the current token
    private int tokType;    //  Token type


    public String toString(){
        return String.format("Exp = {0}\nexplds = {1}\nToken = {2}\nTokType = {3}", exp.toString(), explds,
                token.toString(), tokType);
    }

    // Get the following token
    private void getToken(){
        tokType = NONE;
        token = "";

        // Checking for the end of the expression
        if(explds == exp.length()){
            token = EOF;
            return;
        }
        // Check for spaces, if there is a space - ignore it.
        while(explds < exp.length() && Character.isWhitespace(exp.charAt(explds)))
            ++ explds;
        // Checking for the end of the expression
        if(explds == exp.length()){
            token = EOF;
            return;
        }
        if(isDelim(exp.charAt(explds))){
            token += exp.charAt(explds);
            explds++;
            tokType = DELIMITER;
        }
        else if(Character.isLetter(exp.charAt(explds))){
            while(!isDelim(exp.charAt(explds))){
                token += exp.charAt(explds);
                explds++;
                if(explds >= exp.length()){
                    break;
                }
            }
            tokType = VARIABLE;
        }
        else if (Character.isDigit(exp.charAt(explds))){
            while(!isDelim(exp.charAt(explds))){
                token += exp.charAt(explds);
                explds++;
                if(explds >= exp.length()){
                    break;
                }
            }
            tokType = NUMBER;
        }
        else {
            token = EOF;
            return;
        }
    }

    // Is delimiter
    private boolean isDelim(char charAt) {
        if((" +-/*%^=()".indexOf(charAt)) != -1){
            return true;
        }else {
            return false;
        }
    }

    // Analyzer input point
    public BigDecimal evaluate(String expstr) throws ParserException{

        expstr = expstr.replaceAll(",", ".");

        BigDecimal result;

        exp = expstr;
        explds = 0;
        getToken();

        if(token.equals(EOF)){
            handleErr(NOEXP);   // Not expression
        }

        // Analysis and calculation of the expression
        result = evalExp2();

        if(!token.equals(EOF)){
            handleErr(SYNTAXERROR);
        }

        return result;
    }

    // Add or subtract
    private BigDecimal evalExp2() throws ParserException{

        char op;
        BigDecimal result;
        BigDecimal partialResult;

        result = evalExp3();
        while((op = token.charAt(0)) == '+' ||
                op == '-'){
            getToken();
            partialResult = evalExp3();
            switch(op){
                case '-':
                    result = result.subtract(partialResult);
                    break;
                case '+':
                    result = result.add(partialResult);
                    break;
            }
        }
        return result;
    }

    // Multiply or divide
    private BigDecimal evalExp3() throws ParserException{

        char op;
        BigDecimal result;
        BigDecimal partialResult;

        result = evalExp4();
        while((op = token.charAt(0)) == '*' ||
                op == '/' | op == '%'){
            getToken();
            partialResult = evalExp4();
            switch(op){
                case '*':
                    result = result.multiply(partialResult);
                    break;
                case '/':
                    if(partialResult.compareTo(BigDecimal.ZERO) == 0){
                        handleErr(DIVBYZERO);
                    }else {
                        result = result.divide(partialResult);
                        break;
                    }
            }
        }
        return result;
    }

    // Pow
    private BigDecimal evalExp4() throws ParserException{

        BigDecimal result;
        BigDecimal partialResult;
        BigDecimal ex;

        result = evalExp5();

        if(token.equals("^")){
            getToken();
            partialResult = evalExp4();
            ex = result;
            if(partialResult.compareTo(BigDecimal.ZERO) == 0){
                result = new BigDecimal(1);
            }else{
                result = BigDecimal.valueOf(Math.pow(ex.doubleValue(), partialResult.intValue()));
            }
        }
        return result;
    }

    // Define the unary + or -
    private BigDecimal evalExp5() throws ParserException{

        BigDecimal result;

        String op = " ";

        if((tokType == DELIMITER) && token.equals("+") ||
                token.equals("-")){
            op = token;
            getToken();
        }
        result = evalExp6();
        if(op.equals("-")){
            result = result.negate();
        }

        return result;
    }

    // Expression in brackets
    private BigDecimal evalExp6() throws ParserException{

        BigDecimal result;

        if(token.equals("(")){
            getToken();
            result = evalExp2();
            if(!token.equals(")"))
                handleErr(UNBALPARENS);
            getToken();
        }
        else
            result = atom();
        return result;
    }

    // Get the value of a number
    private BigDecimal atom()   throws ParserException{

        BigDecimal result = null;

        switch(tokType){
            case NUMBER:
                try{
                    result = BigDecimal.valueOf(Double.parseDouble(token));
                }
                catch(NumberFormatException exc){
                    handleErr(SYNTAXERROR);
                }
                getToken();

                break;
            default:
                handleErr(SYNTAXERROR);
                break;
        }
        return result;
    }

    //  Throw Exception
    private void handleErr(int nOEXP2) throws ParserException{

        String[] err  = {
                "Невалидные данные",
                "Unbalanced Parentheses",
                "No Expression Present",
                "Деление на ноль"
        };
        throw new ParserException(err[nOEXP2]);
    }
}