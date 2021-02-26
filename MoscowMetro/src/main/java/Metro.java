import java.util.LinkedHashMap;
import java.util.List;

public class Metro {
    private LinkedHashMap stations;
    private List lines;


    Metro(LinkedHashMap stations, List lines) {
        this.stations = stations;
        this.lines = lines;
    }

    public LinkedHashMap getStations() {
        return stations;
    }

    public void setStations(LinkedHashMap stations) {
        this.stations = stations;
    }

    public List getLines() {
        return lines;
    }

    public void setLines(List lines) {
        this.lines = lines;
    }


}
