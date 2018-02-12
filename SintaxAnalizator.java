package acmp.parseexpression;

/**
 * Created by alexe_000 on 11.02.2018.
 */

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Scanner;

public class SintaxAnalizator {

    public static void main(String[] args) throws ParserException {
        Scanner scanner = new Scanner(System.in);
        Parser myParser = new Parser();
        while (true)
        {
            try
            {
                System.out.print("¬ведите выражение дл€ вычислени€\n");
                String str = scanner.nextLine();
                if(str.equals(""))
                    break;

                double result = myParser.evaluate(str);

                DecimalFormatSymbols s = new DecimalFormatSymbols();

                s.setDecimalSeparator('.');
                DecimalFormat f = new DecimalFormat("#,###.00", s);
                System.out.printf("%s = %s%n", str, f.format(result));

            }
            catch(ParserException e)
            {
                System.out.println(e);
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
    }

}