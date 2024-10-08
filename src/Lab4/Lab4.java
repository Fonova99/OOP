package Lab4;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Lab4 {
    private JFrame pcAdmin;
    private JButton addButton;
    private JButton changeButton;
    private JButton removeButton;
    private JButton searchButton;
    private JToolBar toolBar;
    private DefaultTableModel model;
    private JTable registry;
    private JScrollPane scroll;
    private JTextField disease;
    private JComboBox doctor, speciality;
    private JPanel filterPanel;
    TextException exception = new TextException();
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
                    exception.checkException(disease);
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

        addButton.setToolTipText("Добавить информацию");
        changeButton.setToolTipText("Изменить информацию");
        removeButton.setToolTipText("Удалить информацию");
        searchButton.setToolTipText("Поиск информации");
    }

    private void createToolbar() {
        toolBar = new JToolBar("Панель инструментов");
        toolBar.add(addButton);
        toolBar.add(changeButton);
        toolBar.add(removeButton);
    }

    private void createTable() {
        String[] columns = {"Врач", "Специализация", "Номер кабинета", "График работы", "Справка о болезни", "Кол-во заболеваний"};
        String[][] data = {
                {"Дорогов А.В.", "Терапевт", "234", "пн-пт 09:00 - 14:00", "ОРВИ", "5"},
                {"Курляндцев Д.М.", "Хирург", "125", "вт,ср 11:00 - 14:00", "Гангрена", "3"}
        };
        model = new DefaultTableModel(data, columns);
        registry = new JTable(model);
        setColumnWidths(registry, columns);
        scroll = new JScrollPane(registry);
    }

    private void createSearchComponents() {
        doctor = new JComboBox(new String[]{"Врач", "Дорогов А.В.", "Курляндцев Д.М."});
        speciality = new JComboBox(new String[]{"Специальность", "Терапевт", "Хирург"});
        disease = new JTextField("Введите название заболевания");
        filterPanel = new JPanel();
        filterPanel.add(doctor);
        filterPanel.add(speciality);
        filterPanel.add(disease);
        filterPanel.add(searchButton);
    }

    private void placeComponents() {
        pcAdmin.setLayout(new BorderLayout());
        pcAdmin.add(toolBar, BorderLayout.NORTH);
        pcAdmin.add(scroll, BorderLayout.CENTER);
        pcAdmin.add(filterPanel, BorderLayout.SOUTH);
    }

    private static void setColumnWidths(JTable table, String[] columnNames) {
        FontMetrics fontMetrics = table.getFontMetrics(table.getFont());

        for (int i = 0; i < columnNames.length; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            int width = fontMetrics.stringWidth(columnNames[i]); // Добавляем небольшой запас
            column.setPreferredWidth(width);
        }
    }
}



