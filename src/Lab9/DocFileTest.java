package Lab9;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DocFileTest {
    DefaultTableModel model;
    JFrame admin;
    DocFile docFile;

    @Before
    public void setUp() throws Exception {
        String[] columns = {"Column1", "Column2"};
        String[][] data = {
                {"1", "2"},
                {"3", "4"}
        };
        model = new DefaultTableModel(data, columns);

        admin = new JFrame();
        docFile = new DocFile();
    }

    @Test
    public void testSaveDocFileWithException()  {
        assertThrows(IOException.class, () -> {
            docFile.saveDocFile(admin, model);
        });
    }

    @Test
    public void testSaveDocFile() throws IOException {
        docFile.saveDocFile(admin, model);
        String file = "text.txt";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> lines = reader.lines().toList();reader.close();

        assertEquals(2, lines.size());
        assertEquals("1//2//", lines.get(0));
        assertEquals("3//4//", lines.get(1));
    }

}