package Lab6;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XmlFile {
    Document doc;

    public void recordXmlFile(DefaultTableModel model) {
        if (model == null) {
            System.err.println("Model is not initialized.");
            return;
        }
        FileDialog read = new FileDialog((Frame) null, "Запись данных", FileDialog.SAVE);
        read.setFile("*.xml");
        read.setVisible(true); // Отобразить запрос пользователю
        String fileName = read.getDirectory() + read.getFile(); // Определить имя выбранного каталога и файла
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder(); //Создание парсера документа
            doc = builder.newDocument(); //Создание пустого документа
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Node administration = doc.createElement("Administration"); //Создание корневого элемента
        doc.appendChild(administration); //Добавление его в документ

        //Создание дочерних элементов и присвоение значений атрибутам
        for (int i = 0; i < model.getRowCount(); i++) {
            Element visit = doc.createElement("visit");
            administration.appendChild(visit);
            visit.setAttribute("doctor", (String) model.getValueAt(i, 0));
            visit.setAttribute("work", (String) model.getValueAt(i, 1));
            visit.setAttribute("disease", (String) model.getValueAt(i, 2));
            visit.setAttribute("quantity", (String) model.getValueAt(i, 3));
        }
        try {
            Transformer trans = TransformerFactory.newInstance().newTransformer(); // Создание преобразователя документа
            java.io.FileWriter fw = new FileWriter(fileName); // Создание файла для записи документа
            trans.transform(new DOMSource(doc), new StreamResult(fw)); // Запись документа в файл
        } catch (TransformerConfigurationException e) { //Ошибка создания XML-преобразователя
            e.printStackTrace();
        } catch (TransformerException e) { //Ошибка работы XML-преобразователя
            e.printStackTrace();
        } catch (IOException e) { //Ошибка ввода-вывода
            e.printStackTrace();
        }
    }

    public void readXmlFile(DefaultTableModel model) {
        if (model == null) {
            System.err.println("Model is not initialized.");
            return;
        }
        FileDialog read = new FileDialog((Frame) null, "Чтение данных", FileDialog.LOAD);
        read.setFile("*.xml");
        read.setVisible(true); // Отобразить запрос пользователю
        String fileName = read.getDirectory() + read.getFile(); // Определить имя выбранного каталога и файла
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder(); //Создание парсера документа
            doc = builder.parse(new File(fileName)); //Чтение из файла
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) { // Обработка ошибки парсера при чтении данных из XML-файла
            e.printStackTrace();
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        NodeList visits = doc.getElementsByTagName("visit"); //Получение списка элементов с именем visit
        model.setRowCount(0); //Очистка таблицы
        for (int i = 0; i < visits.getLength(); i++) { //Цикл просмотра списка элементов и запись данных в таблицу
            Node elem = visits.item(i); //Выбор очередного элемента списка
            NamedNodeMap attrs = elem.getAttributes(); //Получение списка атрибутов элемента
            //Чтение атрибутов элемента
            String doctor = attrs.getNamedItem("doctor").getNodeValue();
            String work = attrs.getNamedItem("work").getNodeValue();
            String disease = attrs.getNamedItem("disease").getNodeValue();
            String quantity = attrs.getNamedItem("quantity").getNodeValue();
            model.addRow(new String[]{doctor, work, disease, quantity}); //Запись данных в таблицу
        }
    }
}