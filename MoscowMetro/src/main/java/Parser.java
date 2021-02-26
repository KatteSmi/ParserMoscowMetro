import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Parser {

    static LinkedHashMap<String, JSONArray> getLinesWithStation() throws IOException {
        Document document = Jsoup.connect("https://www.moscowmap.ru/metro.html#lines").maxBodySize(0).get();
        String cssQuery = "div[class$=t-metrostation-list-table]";
        LinkedHashMap<String, JSONArray> linesWithStationMap = new LinkedHashMap<>();
        Elements metroLines = document.select(cssQuery);
        for (Element metroLine : metroLines) {
            String lineId = metroLine.select("div[class^=js-metro-stations]")
                    .attr("data-line");
            JSONArray stationInLine = getStationInLine(metroLine);
            linesWithStationMap.put(lineId, stationInLine);
        }
        return linesWithStationMap;
    }

    static JSONArray getStationInLine(Element metroLine) {
        JSONArray stationInLine = new JSONArray();
        Elements stations = metroLine.getElementsByTag("a");
        for (Element station : stations) {
            String nameStation = station.select("span[class=name]").text();
            stationInLine.add(nameStation);
        }
        return stationInLine;
    }

    static List<String> parseLineName(Elements webTable) {
        List<String> lineNames = new ArrayList<>();
        Elements lines = webTable.select("span.js-metro-line");
        for (Element line : lines) {
            lineNames.add(line.text());
        }
        return lineNames.stream().distinct().collect(Collectors.toList());
    }

    static void printFromJSON() {
        
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonData = (JSONObject) parser.parse(getJsonFile());
            JSONObject stationsObject = (JSONObject) jsonData.get("stations");

            Set<Object> stationNumbers = new TreeSet<>(stationsObject.keySet());

            for (Object station : stationNumbers) {
                System.out.println("Линия № " + station + " содержит станций: " + new ArrayList<>((Collection<String>) stationsObject.get(station)).size());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static String getJsonFile() {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/map.json"));
            lines.forEach(line -> builder.append(line));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }
}