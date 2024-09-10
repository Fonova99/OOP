package Lab6;

import javax.swing.*;

public class TextException extends  Exception{
    public TextException() {
        super("Вы не ввели ФИО или должность доктора");
    }
    public void checkException(JTextField doctor) throws TextException, NullPointerException{
        String str = doctor.getText();
        if (str.contains("ФИО или должность доктора")) throw new TextException();
        if (str.length() == 0) throw new NullPointerException();
    }
}
