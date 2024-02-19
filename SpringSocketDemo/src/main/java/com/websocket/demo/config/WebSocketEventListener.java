package com.websocket.demo.config;

import com.websocket.demo.chat.ChatMessage;
import com.websocket.demo.chat.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component  //ทำให้คลาสนี้เป็นส่วนนึงของ spring ถ้าไม่ใส่ spring จะไม่เห็น
@RequiredArgsConstructor //ทำ constructer ให้เฉพาะ final เท่านั้น!!!
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messageSendingOperations;
    //ใช้หลักการ dependacy  injection ในาร inject template message
    //lombok -> can provided ได้ ทำให้ class เรามี constructor  ที่จะ new มาให้
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) { //ดัก Event connect,disconnect ได้ @RequiredArgsConstructor
        /*    ดักได้แล้ว จะทำอะไรก็ทำ ลบคนนั้นออกจากฐานข้อมูล
        เตอนแรกเราเอา username ไปยัด session
        แลปแล้ว มี map ข้างในอันนึง map ว่า username คนนี้คือ sender คนไหน ทำไมไม่ทำแบบเดิม
        สิ่งที่ identify connection  จะผูกติดอยู่่อยู่กับ connectionsession ของเขาเอง คือ ระบุได้เลยว่า user คนนี้อยู่ที่ session นี้ไม่จำเป็นต้องมี map เป็นของเราเอง
        ตัว session ที่เขา connect  เป็นตัว identify ตัวของเขาเอง
        case same name ?
        วิธีแยกแยะ ->session แต่ละ user นี้เป็นเหตุผลที่เราใช้ เก็บข้อมูลใน heaer ของ session
        ตอนที่เขา disconnect ไปแล้ว เราจะดัก message ไม่ได้เพราะเขาไม่ได้ส่งอะไรมา
        ทางเดียวที่ทำได้คือ ไปหาจา session ที่เขา connect เข้ามาด้วย  username นั้น

        ทำการ acess session ทาง event ของเรา
        websocket ไม่ได้ส่งข้อมูลผ่านตัว websocket เอง มันใช้ protocal คือ stomp เอามาส่งข้อมูล
        การ get ข้อมูลส่วนใหญ่ผ่านทาง Stomp */
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        //ไม่ใช่ chatmessage มันเป็น websocket เวลา disconnect มันจะมี message บางอย่างมา message จะแนบ header มาด้วย
        //annotation @Builder จะสร้าง object มาใช้ได้ครั้งนึง
        //ใช้ object นั้นในการส่ง message กกลับไป
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .countUser(ChatMessage.UpdateCountUserdec())
                    .build();
//ทำการส่ง message ไปให้ทุกคนที่ subscribe channel brodcast นี้อยู่
            //ไม่มี Sendto ให้ใช้ class
            messageSendingOperations.convertAndSend("/topic/public", chatMessage);

        }
    }

}
