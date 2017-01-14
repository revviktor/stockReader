/*
 * Copyright (c) 2016 GE. All Rights Reserved.
 * GE Confidential: Restricted Internal Distribution
 */
package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

  @Autowired
  private SimpMessagingTemplate template;

  public void sendMessage(String message){
    template.convertAndSend("/topic/greetings", new Response( message));
  }
}
