package com.websocket.demo.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
//ทำให้เป็นส่วนนึงของ springboot เฉยๆ
//@MessageMapping หาก client จะส่งข้อความมายัง server ควรใช้ endpoint ไหน springboot จะรู้ว่าโอเค เข้ามา endpoint นี้จะเรียก metho mี่ถูกกกำกับไว้
//@SendTo  ต้องทราบว่าโยนไปยัง client ช่องทางไหน

@Controller
public class chatController {
    //url อันเดียวที่เข้ามา message แบบไหน
    //ต้องส่งเข้ามา /app
    //กำหนด  route เพิ่ม user เข้ามานะ เช่น  /app/add.User
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    //กำหนด format ว่า chat เรามี type เป็นอะไร
    //new class Chat message
    //return message ที่ส่งเข้าม
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        return chatMessage;
    }
    //เอา user ไปเก็บสักที่นึง
    //เก็บที่ header ของ session ของการ connect ของคนๆนั้น
    //ตอนแรกเราใฝใช้ message.sender ไม่ได้เพราะไม่มี getter/setter -> ถ้าจะเอา content ไปใช้ เราไม่มี method ให้เรียก
    //lombok @Getter
    //brodcast ออกไปไหน -> @sendto
    //คนที่พึ่งเข้ามา completed
    //message ที่ส่งเข้ามาที่ server ทำไง -> server กระจายออกไง copy เลย
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        int n = ChatMessage.UpdateCountUserinc();
        chatMessage.setCount(n);
        return chatMessage;
    }
}
//user จะส่ง /app/chat.sendMessage
//connect เข้ามากับ ส่ง message ทำได้ละ
//เนื่องจาก 2 method sendto เหมือนกัน client จะไม่ทราบว่า message ที่ได้จะได้มาจาก method ไหน
//แล้ววแต่ appliction ว่า client จำเป็นต้องแยกแยะหรือเปล่า
//ในที่นี้ไม่จำเป็น เพราะ ใน message มี  type ตัวมันเองก็สามารถแยะแยะได้เลยส่ง channe; ทางเดียวกันได้้
//headeracessor -> client ไม่ต้อง care ว่าจะส่งอะไรบ้าง แค่ส่ง message เข้ามาแล้ว spring จะจัดการให้ว่า method นี้ถ้าต้องการ argument เพิ่มเติม จะโยนเข้ามาเพิ่มให้เอง
//client จะใช้งานง่าย ไม่ต้องนั่งดู  signature แต่ละ method ดูแค่ตัว anotatoin sendto,messageMapping แค้นั้นพอ


//disconnect
//ตอนเข้า พิมพ์ชื่อ แล้วส่ง message
//ตอนออก มันปิดหน้าต่าง
//เลยไม่สามารถสร้าง disconnect ในนี้ได้ มันต้อง handle คนละแบบ