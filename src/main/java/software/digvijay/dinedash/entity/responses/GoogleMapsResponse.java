package software.digvijay.dinedash.entity.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleMapsResponse {
    @JsonProperty("destination_addresses")
    private List<String> destinationAddresses;
    @JsonProperty("origin_addresses")
    private List<String> originAddresses;
    private List<Row> rows;
    private String status;

    @Getter
    @Setter
    public static class Row {  // Make this class static
        private List<Element> elements;
    }

    @Getter
    @Setter
    public static class Element {  // Make this class static
        private Distance distance;
        private Duration duration;
        private String status;
    }

    @Getter
    @Setter
    public static class Distance {  // Make this class static
        private String text;
        private int value;
    }

    @Getter
    @Setter
    public static class Duration {  // Make this class static
        private String text;
        private int value;
    }
}
