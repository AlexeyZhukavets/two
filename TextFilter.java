import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

/**
 * Created by alexe_000 on 08.02.2018.
 */
public class TextFilter {

    /*
    * Search parameters passed to the program during startup
    * */
    private static String[] parameters;

    /*
    * list of entered strings
    * */
    private static List<String[]> inputStrings = new ArrayList<>();

    /*
    * The array with the result of checking each entered string
    * */
    private static boolean[] rezultIndexes;

    private static Pattern pattern;

    public static void main(String[] args) {
        parameters = args;
        startProgramm();
    }

    /*
    * Entering Strings
    * */
    private static void startProgramm(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Параметры программы: " + parametersToString());
        System.out.println("Строки:");

        while (true){

            StringBuffer addString = new StringBuffer(scanner.nextLine());

            //A sign of the end of data entry, input of an empty line
            if(addString.toString().equals("")){
                break;
            }

            //Remove the last character from the input line, if it ';'
            if(addString.charAt(addString.length() - 1) == ';'){

                addString.deleteCharAt(addString.length() - 1);

            }

            //Split the input string into an array of substrings
            inputStrings.add(addString.toString().split(" "));
        }

        //rezultIndexes.length() = inputStrings.size
        rezultIndexes = new boolean[inputStrings.size()];

        filter();//Run filter ()
    }

    /*
    * A method that compares the entered strings with the parameters of the program
    * */
    private static void filter(){

        boolean isPattern = false;

        //The loop goes through the search parameters
        for (int i = 0; i < parameters.length; i++){
            parameters[i] = parameters[i].trim();
            isPattern = isPattern(parameters[i]);//Is the program parameter a regular expression?

            //The loop goes through the inputStrings
            for (int j = 0; j < inputStrings.size(); j++){

                //The loop goes through the substrings in inputStrings
                for (int k = 0; k < inputStrings.get(j).length; k++){

                    //If the search parameter is not a regular expression, just compare the lines
                    if(!isPattern){
                        if(parameters[i].equals(inputStrings.get(j)[k])){
                            rezultIndexes[j] = true;
                            break;
                        }
                    }
                    //If the parameter is a regular expression
                    else if(doMatch(inputStrings.get(j)[k])){
                        rezultIndexes[j] = true;
                    }
                }
            }
        }
        displayRezults();//Output the result
    }

    /*
    * Regular expression test
    * */
    private static boolean doMatch(String word){
        Matcher matcher = pattern.matcher(word);
        return matcher.matches();
    }

    /*
    * Is the search parameter a regular expression?
    * */
    private static boolean isPattern(String regex){
        try {
            pattern = Pattern.compile(regex);
            return true;
        }catch (PatternSyntaxException ex){
            return false;
        }
    }

    /*
    * Display the result of the program
    * */
    private static void displayRezults(){
        System.out.println("Вывод:");
        for (int i = 0; i < inputStrings.size(); i++){
            if(rezultIndexes[i]){
                int arrayIndex = 0;
                while (arrayIndex < inputStrings.get(i).length - 1){
                    System.out.print(inputStrings.get(i)[arrayIndex] + " ");
                    arrayIndex++;
                }
                System.out.println(inputStrings.get(i)[inputStrings.get(i).length - 1] + ";");
            }
        }
    }

    /*
    * Search parameters grafted to one line
    * */
    private static String parametersToString(){
        String parameterString = "";
        for (int i = 0; i < parameters.length; i++){
            parameterString += parameters[i] + " ";
        }
        return parameterString;
    }
}