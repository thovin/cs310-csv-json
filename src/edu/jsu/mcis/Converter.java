package edu.jsu.mcis;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("colHeaders", Arrays.asList(iterator.next()));

            ArrayList<String[]> rawData = new ArrayList<>();
            while (iterator.hasNext()) {
                rawData.add(iterator.next());
            }

            ArrayList<String> rowHeaders = new ArrayList<>();
            for (int i = 0; i < rawData.size(); i++) {
                rowHeaders.add(rawData.get(i)[0]);
            }

            jsonObject.put("rowHeaders", rowHeaders);

            ArrayList<Integer> dataLine = new ArrayList<>();
            ArrayList<ArrayList> dataArray = new ArrayList<>();
            for (int i = 0; i < rawData.size(); i++) {
                dataLine = new ArrayList<>();
                for (int n = 0; n < rawData.get(0).length - 1; n++) {
                    dataLine.add(Integer.parseInt(rawData.get(i)[n + 1]));
                }
                dataArray.add(dataLine);
            }

            jsonObject.put("data", dataArray);
            results = jsonObject.toJSONString();

        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);

            ArrayList<String> temp = (ArrayList<String>) jsonObject.get("colHeaders");
            String[] line = temp.toArray(new String[temp.size()]);
            csvWriter.writeNext(line);

            ArrayList<String> rowHeaders = (ArrayList<String>) jsonObject.get("rowHeaders");
            ArrayList<ArrayList<Long>> data = (ArrayList<ArrayList<Long>>) jsonObject.get("data");
            line = new String[data.get(0).size() + 1];

            for (int i = 0; i < rowHeaders.size(); i++) {
                line[0] = rowHeaders.get(i);
                for (int n = 0; n < data.get(i).size(); n++) {
                    line[n + 1] = data.get(i).get(n).toString();
                }
                csvWriter.writeNext(line);
            }

            results = writer.toString();
            
        }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}