package Lab6;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Lab6 {
    private JFrame pcAdmin;
    private JButton addButton;
    private JButton changeButton;
    private JButton removeButton;
    private JButton searchButton;
    private JButton saveButton;
    private JButton openButton;
    private JToolBar toolBar;
    private DefaultTableModel model;
    private JTable registry;
    private JScrollPane scroll;
    private JTextField doctor;
    private JComboBox cabinet, disease;
    private JPanel filterPanel;
    private TextException exception = new TextException();
    private boolean noException = true;

    public void show() {
        createWindow();
        createButtons();
        createToolbar();
        createTable();
        createSearchComponents();
        placeComponents();
        pcAdmin.setVisible(true);
    }

    private void createWindow() {
        pcAdmin = new JFrame("Администратор поликлиники");
        pcAdmin.setSize(900, 500);
        pcAdmin.setLocation(100, 100);
        pcAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createButtons() {
        addButton = new JButton("Добавить");
        changeButton = new JButton("Изменить");
        removeButton = new JButton("Удалить");
        searchButton = new JButton("Поиск");
        saveButton = new JButton("Сохранить");
        openButton = new JButton("Загрузить");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(pcAdmin, "Добавляем информацию");
            }
        });
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(pcAdmin, "Изменяем информацию");
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(pcAdmin, "Удаляем информацию");
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    exception.checkException(doctor);
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(pcAdmin, ex.toString());
                    noException = false;
                } catch (TextException myEx) {
                    JOptionPane.showMessageDialog(pcAdmin, myEx.getMessage());
                    noException = false;
                }
                if (noException) {
                    JOptionPane.showMessageDialog(pcAdmin, "Ищем информацию");
                }
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog save = new FileDialog(pcAdmin, "Сохранение данных", FileDialog.SAVE);
                save.setFile("*.txt");
                save.setVisible(true); // Отобразить запрос пользователю
                String fileName = save.getDirectory() + save.getFile(); // Определить имя выбранного каталога и файла

                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                    for (int i = 0; i < model.getRowCount(); i++) // Для всех строк
                        for (int j = 0; j < model.getColumnCount(); j++) // Для всех столбцов
                        {
                            writer.write((String) model.getValueAt(i, j)); // Записать значение из ячейки
                            writer.write("\n"); // Записать символ перевода каретки
                        }
                    writer.close();
                } catch (IOException ex) // Ошибка записи в файл
                {
                    ex.printStackTrace();
                }
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog open = new FileDialog(pcAdmin, "Сохранение данных", FileDialog.LOAD);
                open.setFile("*.txt");
                open.setVisible(true); // Отобразить запрос пользователю
                String fileName = open.getDirectory() + open.getFile(); // Определить имя выбранного каталога и файла

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(fileName));
                    int rows = model.getRowCount();
                    for (int i = 0; i < rows; i++) {
                        model.removeRow(0); // Очистка таблицы
                    }
                    String doctor;
                    do {
                        doctor = reader.readLine();
                        if (doctor != null) {
                            String workSchedule = reader.readLine();
                            String diseases = reader.readLine();
                            String quantity = reader.readLine();
                            model.addRow(new String[]{doctor, workSchedule, diseases, quantity}); // Запись строки в таблицу
                        }
                    } while (doctor != null);
                    reader.close();
                } catch (FileNotFoundException ex) { // файл не найден
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        addButton.setToolTipText("Добавить информацию");
        changeButton.setToolTipText("Изменить информацию");
        removeButton.setToolTipText("Удалить информацию");
        searchButton.setToolTipText("Поиск информации");
        saveButton.setToolTipText("Сохранить данные");
        openButton.setToolTipText("Загрузить данные");
    }

    private void createToolbar() {
        toolBar = new JToolBar("Панель инструментов");
        toolBar.add(addButton);
        toolBar.add(changeButton);
        toolBar.add(removeButton);
        toolBar.add(saveButton);
        toolBar.add(openButton);
    }

    private void createTable() {
        String[] columns = {"Врач/Специализация", "График работы(№ кабинета, дни и часы приема)", "Справка о болезни", "Кол-во заболеваний"};
        String[][] data = {
                {"Дорогов А.В./Терапевт", "Кабинет №234, пн-пт 09:00 - 14:00", "ОРВИ", "5"},
                {"Курляндцев Д.М./Хирург", "Кабинет №125, вт,ср 11:00 - 14:00", "Гангрена", "3"}
        };
        model = new DefaultTableModel(data, columns);
        registry = new JTable(model);
        setColumnWidths(registry, columns);
        scroll = new JScrollPane(registry);
    }

    private void createSearchComponents() {
        doctor = new JTextField("ФИО или должность доктора");
        cabinet = new JComboBox(new String[]{"Кабинет", "234", "125"});
        disease = new JComboBox(new String[]{"Заболевание", "ОРВИ", "Гангрена"});
        filterPanel = new JPanel();
        filterPanel.add(doctor);
        filterPanel.add(cabinet);
        filterPanel.add(disease);
        filterPanel.add(searchButton);
    }

    private void placeComponents() {
        pcAdmin.setLayout(new BorderLayout());
        pcAdmin.add(toolBar, BorderLayout.NORTH);
        pcAdmin.add(scroll, BorderLayout.CENTER);
        pcAdmin.add(filterPanel, BorderLayout.SOUTH);
    }

    private void setColumnWidths(JTable table, String[] columnNames) {
        FontMetrics fontMetrics = table.getFontMetrics(table.getFont());

        for (int i = 0; i < columnNames.length; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            int width = fontMetrics.stringWidth(columnNames[i]); // Добавляем небольшой запас
            column.setPreferredWidth(width);
        }
    }

    public DefaultTableModel getModel() {
        return model;
    }
}





