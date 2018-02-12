import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

/**
 * Created by alexe_000 on 08.02.2018.
 */
public class TextFilter {

    /*
    * Параметры поиска, передаваемые программе во время запуска
    * */
    private static String[] parameters;

    /*
    * Список массивов слов каждой введенной строки
    * */
    private static List<String[]> inputStrings = new ArrayList<>();

    /*
    * Паттерн, используется для регулярного выражения
    * */
    private static Pattern pattern;

    /*
    * Масиив boolean, каждое значение которого соответствует результату фильтрации
    * одной введенной строки
    * */
    private static boolean[] rezultIndexes;
    
    public static void main(String[] args) {
        parameters = args;
        startProgramm();
    }

    /*
    * Ввод строк
    * */
    private static void startProgramm(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Параметры программы: " + parametersToString());
        System.out.println("Строки:");

        while (true){

            StringBuffer addString = new StringBuffer(scanner.nextLine());

            //Признак окончания ввода данных, ввод пустой строки
            if(addString.toString().equals("")){
                break;
            }

            //Удаляем из входной строки последний символ, если он ';'
            if(addString.charAt(addString.length() - 1) == ';'){

                addString.deleteCharAt(addString.length() - 1);

            }

            //Разбиваем входную строку на массив слов, который заносим в список inputStrings
            inputStrings.add(addString.toString().split(" "));
        }

        //rezultIndexes.length() = inputStrings.size
        rezultIndexes = new boolean[inputStrings.size()];

        filter();//Выполняем фильтр
    }

    /*
    * Фильтр
    * */
    private static void filter(){

        boolean isPattern = false;

        //Цикл перебирает параметры поиска
        for (int i = 0; i < parameters.length; i++){
            parameters[i] = parameters[i].trim();//Обрезаем пробелы у параметра программы
            isPattern = isPattern(parameters[i]);//Явлеется ли параметр программы регулярным выражением

            //Цикл перебирает строки inputStrings
            for (int j = 0; j < inputStrings.size(); j++){

                //Цикл перебирает слова в каждом inputStrings
                for (int k = 0; k < inputStrings.get(j).length; k++){

                    //Если параметр поиска не регулярное выражение, просто сравниваем строки
                    if(!isPattern){
                        if(parameters[i].equals(inputStrings.get(j)[k])){
                            rezultIndexes[j] = true;
                            break;
                        }
                    }
                    //Если параметр - регулярное выражение, выполняем проверку
                    else if(doMatch(inputStrings.get(j)[k])){
                        rezultIndexes[j] = true;
                    }
                }
            }
        }
        displayRezults();
    }
    
    /*
    * Проверка на соответствие регулярному выражению
    * */
    private static boolean doMatch(String word){
        Matcher matcher = pattern.matcher(word);
        return matcher.matches();
    }

    /*
    * Является ли параметр поиска регулярным выражением
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
    * Выводим результат фильтрации
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
    * Параметры переданные в программу при запуске, привиденные к одной строке
    * */
    private static String parametersToString(){
        String parameterString = "";
        for (int i = 0; i < parameters.length; i++){
            parameterString += parameters[i] + " ";
        }
        return parameterString;
    }
}