/*
 * Copyright (c) 2016 GE. All Rights Reserved.
 * GE Confidential: Restricted Internal Distribution
 */
package hello;

import org.springframework.stereotype.Component;

@Component
public class MessageCollectorSender extends MessageSender{
StringBuilder build = new StringBuilder();

  public void sendMessage(String message){
   build.append(message+"\n"); 
  }
  public String getMessages(){
	  return build.toString();
  }
}
