package com.websocket.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /*
    * defined connection endpoint
    * */
    //กำหนด path ที่เราจะ connect from client ->server
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //localhost:8080/ws
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        //ws = Websocket  withSockJS -> connect WebSocket from library JS
        //โดยปกติ WS จะ connect ผ่านเข้ามาเหมือน Http Request ไม่ได้ ต้องมีตัวกลาง config ->SockJS
        //setAllowedOriginPatterns() -> allow adress/IP ที่จะเข้ามาถึง WebSocKet ของเราเป็น IP อะไรบ้าง * = ทุกที่
    }


    /**
     * defined message passing endpoint
     * @param registry
     */
    //ถ้า server จะรับ จะรับผ่านทางไหน
    //ถ้า server จะ Brodcast จะ Brodcast ออกทางไหน
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //path ที่ server จะรับข้อความจาก client
        //localhost:8080/app + แนบ body
        registry
                .setApplicationDestinationPrefixes("/app")
                .enableSimpleBroker("/topic");
        //topic
    }
}
