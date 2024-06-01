package com.server.quant_bot.quant.notification.service;


import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class NotificationSnsServiceTest {


    @Autowired
    NotificationSnsService notificationSnsService;

    @Autowired
    DefaultMessageService messageService;

    @Test
    @DisplayName("문자메세지를 보내야 한다..")
    void findByUserTest() {
        Message message = new Message();
        message.setFrom("01041780430");
        message.setTo("01041780430");
        message.setText("Hello World");

        try {
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

    }

}