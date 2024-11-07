//package software.digvijay.dinedash.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
 //
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.Random;
//@Slf4j
//@Service
//public class SendLocationService {
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate; // Sending JSON as String
//    private final Random random = new Random();
//    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper for JSON serialization
//
//    public void sendRandomLocation(String orderId) {
//        // Generate random latitude and longitude
//        double latitude = -90 + (90 - (-90)) * random.nextDouble(); // Range -90 to 90
//        double longitude = -180 + (180 - (-180)) * random.nextDouble(); // Range -180 to 180
//
//        // Create a Location object
//        Location location = new Location(latitude, longitude);
//
//        // Create a message object combining orderId and location
//        LocationMessage message = new LocationMessage(orderId, location);
//
//        // Convert the message object to JSON
//        try {
//            String messageJson = objectMapper.writeValueAsString(message);
//            // Send the JSON message to the Kafka topic
//            kafkaTemplate.send("driver-locations", messageJson);
//            System.out.println("Sent location: " + messageJson + " for Order ID: " + orderId);
//        } catch (JsonProcessingException e) {
//            System.err.println("Error serializing location message: " + e.getMessage());
//        }
//    }
//    @Scheduled(cron="0 0/1 * 1/1 * ?")
//    public void letsSendLocation(){
//        log.info(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
//        sendRandomLocation("10001");
//    }
//    public static class LocationMessage {
//        private String orderId;
//        private Location location;
//
//        public LocationMessage(String orderId, Location location) {
//            this.orderId = orderId;
//            this.location = location;
//        }
//
//        public String getOrderId() {
//            return orderId;
//        }
//
//        public void setOrderId(String orderId) {
//            this.orderId = orderId;
//        }
//
//        public Location getLocation() {
//            return location;
//        }
//
//        public void setLocation(Location location) {
//            this.location = location;
//        }
//    }
//}