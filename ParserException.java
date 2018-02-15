/**
 * Created by alexe_000 on 11.02.2018.
 */
public class ParserException extends Exception{

    private String errStr;  //  Описание ошибки

    public ParserException(String errStr) {
        super();
        this.errStr = errStr;
    }

    public String toString(){
        return this.errStr;
    }
}