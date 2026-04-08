package com.testinium.step;

public class TestCSV {
    public static void main(String[] args) {
        String[][] csvData = CsvReaderUtil.readCSV("src/test/resources/data.csv");
        for (String[] row : csvData) {
            for (String cell : row) {
                System.out.print(cell + " | ");
            }
            System.out.println();
        }
    }
}