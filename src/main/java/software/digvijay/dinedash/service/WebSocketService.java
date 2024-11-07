//package software.digvijay.dinedash.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;

//
//@Service
//public class WebSocketService {
//
//
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    public void sendLocationUpdate(String orderId, Location location) {
//        String destination = "/topic/locations/" + orderId;
//        messagingTemplate.convertAndSend(destination, location);
//        System.out.println("Sent location update to WebSocket: "+destination+" Location:" + location);
//    }
//}
