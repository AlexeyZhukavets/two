import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

/**
 * Created by alexe_000 on 08.02.2018.
 */
public class TextFilter {

    /*
    * ��������� ������, ������������ ��������� �� ����� �������
    * */
    private static String[] parameters;

    /*
    * ������ �������� ���� ������ ��������� ������
    * */
    private static List<String[]> inputStrings = new ArrayList<>();

    /*
    * �������, ������������ ��� ����������� ���������
    * */
    private static Pattern pattern;

    /*
    * ������ boolean, ������ �������� �������� ������������� ���������� ����������
    * ����� ��������� ������
    * */
    private static boolean[] rezultIndexes;
    
    public static void main(String[] args) {
        parameters = args;
        startProgramm();
    }

    /*
    * ���� �����
    * */
    private static void startProgramm(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("��������� ���������: " + parametersToString());
        System.out.println("������:");

        while (true){

            StringBuffer addString = new StringBuffer(scanner.nextLine());

            //������� ��������� ����� ������, ���� ������ ������
            if(addString.toString().equals("")){
                break;
            }

            //������� �� ������� ������ ��������� ������, ���� �� ';'
            if(addString.charAt(addString.length() - 1) == ';'){

                addString.deleteCharAt(addString.length() - 1);

            }

            //��������� ������� ������ �� ������ ����, ������� ������� � ������ inputStrings
            inputStrings.add(addString.toString().split(" "));
        }

        //rezultIndexes.length() = inputStrings.size
        rezultIndexes = new boolean[inputStrings.size()];

        filter();//��������� ������
    }

    /*
    * ������
    * */
    private static void filter(){

        boolean isPattern = false;

        //���� ���������� ��������� ������
        for (int i = 0; i < parameters.length; i++){
            parameters[i] = parameters[i].trim();//�������� ������� � ��������� ���������
            isPattern = isPattern(parameters[i]);//�������� �� �������� ��������� ���������� ����������

            //���� ���������� ������ inputStrings
            for (int j = 0; j < inputStrings.size(); j++){

                //���� ���������� ����� � ������ inputStrings
                for (int k = 0; k < inputStrings.get(j).length; k++){

                    //���� �������� ������ �� ���������� ���������, ������ ���������� ������
                    if(!isPattern){
                        if(parameters[i].equals(inputStrings.get(j)[k])){
                            rezultIndexes[j] = true;
                            break;
                        }
                    }
                    //���� �������� - ���������� ���������, ��������� ��������
                    else if(doMatch(inputStrings.get(j)[k])){
                        rezultIndexes[j] = true;
                    }
                }
            }
        }
        displayRezults();
    }
    
    /*
    * �������� �� ������������ ����������� ���������
    * */
    private static boolean doMatch(String word){
        Matcher matcher = pattern.matcher(word);
        return matcher.matches();
    }

    /*
    * �������� �� �������� ������ ���������� ����������
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
    * ������� ��������� ����������
    * */
    private static void displayRezults(){
        System.out.println("�����:");
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
    * ��������� ���������� � ��������� ��� �������, ����������� � ����� ������
    * */
    private static String parametersToString(){
        String parameterString = "";
        for (int i = 0; i < parameters.length; i++){
            parameterString += parameters[i] + " ";
        }
        return parameterString;
    }
}