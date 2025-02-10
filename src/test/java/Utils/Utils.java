
package Utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    // Method to read data from the CSV file and return it as a list of Object arrays
    public static List<Object[]> readCSV(String filePath) throws IOException {
        List<Object[]> data = new ArrayList<>();
        
        
        // Open the CSV file and prepare for reading
        InputStreamReader reader = new InputStreamReader(CSVReader.class.getClassLoader().getResourceAsStream("Data/Put_scenarios.csv"));
        
        if (reader == null) {
            System.err.println("CSV file not found!");
        }        
        // Use CSVFormat.DEFAULT with withFirstRecordAsHeader() to handle CSV headers
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader() // This tells Commons CSV to use the first row as the header
                .parse(reader);
        
     // Iterate through each record (row) in the CSV
        for (CSVRecord record : records) {
            // Collect all columns in a row as a single object array
            List<String> rowValues = new ArrayList<>();
            for (String value : record) {
                rowValues.add(value);
            }

            // Convert list to Object[] and add to data
            data.add(rowValues.toArray());
        }
        
        return data;
    }
}
