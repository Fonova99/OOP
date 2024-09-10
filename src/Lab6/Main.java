package Lab6;

public class Main {
    public static void main(String[] args) {
        Lab6 lab6 = new Lab6();
        lab6.show();
        new XmlFile().recordXmlFile(lab6.getModel());
    }
}
