package writer;

import com.opencsv.CSVWriter;
import parser.HeadAndRef;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WriteCSV {
    public static boolean writingToCSV(List<HeadAndRef> links, String searchTerm) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter("D://output.csv"));
            List list = new ArrayList();
            String[] line1 = {"Search Result For: " + searchTerm};
            String[] columnNames = {"Page.No.", "Heading", "Reference", "Image URLs"};
            list.add(line1);
            list.add(columnNames);
            for (int i = 0; i < links.size(); i++) {
                /*String[] line = {Integer.toString(i+1),links.get(i).getHead(),links.get(i).getRef()}*/
                list.add(links.get(i).toStringArray());
            }
            writer.writeAll(list);
            writer.flush();
            System.out.println("Data Entered");
        } catch (IOException e) {
            System.out.println(e);
        }
        return true;
    }
}
