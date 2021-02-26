import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.*;
import java.util.*;
public class Main {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        try {
            Document doc = Jsoup.connect("https://www.moscowmap.ru/metro.html#lines").maxBodySize(0).get();
            Elements webTable = doc.select("div.t-text-simple");
            List<String> lineNumbers = webTable.select("div.js-metro-stations").eachAttr("data-line");

            LinkedHashMap<String, JSONArray> linesWithStationMap = Parser.getLinesWithStation();
            List<Line> linesList = new ArrayList<>();
            for (int i = 0; i < lineNumbers.size(); i++) {
                linesList.add(new Line(lineNumbers.get(i), Parser.parseLineName(webTable).get(i)));
            }

            Metro metro = new Metro( linesWithStationMap, linesList);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/map.json"), metro);
            Parser.printFromJSON();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

