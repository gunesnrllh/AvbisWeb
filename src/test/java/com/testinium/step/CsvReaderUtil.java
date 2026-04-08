package com.testinium.step;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;

public class CsvReaderUtil {

    public static String[][] readCSV(String filePath) {
        String[][] data = null;

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            var allRows = reader.readAll();
            data = new String[allRows.size()][];
            for (int i = 0; i < allRows.size(); i++) {
                data[i] = allRows.get(i);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return data;
    }
}