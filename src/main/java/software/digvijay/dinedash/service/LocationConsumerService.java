//package software.digvijay.dinedash.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;

//
//@Service
//public class LocationConsumerService {
//
//    private final WebSocketService webSocketService;
//    private final ObjectMapper objectMapper; // Use ObjectMapper as a bean
//
//    public LocationConsumerService(WebSocketService webSocketService, ObjectMapper objectMapper) {
//        this.webSocketService = webSocketService;
//        this.objectMapper = objectMapper; // Inject ObjectMapper
//    }
//
//    @KafkaListener(topics = "driver-locations", groupId = "location-consumer-group")
//    public void consume(String messageJson) {
//        // Handle the incoming JSON message
//        try {
//            // Deserialize the JSON into a Map or a custom class
//            LocationMessage message = objectMapper.readValue(messageJson, LocationMessage.class);
//            System.out.println("Received location update for Order ID: " + message.getOrderId());
//            System.out.println("Location Data: " + message.getLocation());
//
//            // Send the location update to WebSocket
//            webSocketService.sendLocationUpdate(message.getOrderId(), message.getLocation());
//
//        } catch (JsonProcessingException e) {
//            System.err.println("Error deserializing location JSON: " + e.getMessage());
//        }
//    }
//
//    // Inner class to represent the incoming message structure
//    public static class LocationMessage {
//        private String orderId;
//        private Location location;
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
