package com.aobei.trainapi.server.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OnsHandlerTest {

    @Autowired
    OnsHandler handler;

    @Test
    public void sendRejectMessage() {
    }

    @Test
    public void sendCancelMessage() {
    }

    @Test
    public void sendRefundMessage() {
    }

    @Test
    public void sendCompleteMessage() {
    }

    @Test
    public void sendRobbingMessage() {
        handler.sendRobbingMessage("1530523131_1",1,null);

    }
}