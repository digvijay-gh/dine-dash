//package software.digvijay.dinedash.controller;
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class WebSocketController {
//
//    @MessageMapping("/message") // Maps messages sent to "/app/message"
//    @SendTo("/topic/reply") // Sends responses to clients subscribed to "/topic/reply"
//    public String sendMessage(String message) {
//        return "Received: " + message; // Modify this to send back the appropriate response
//    }
//}
