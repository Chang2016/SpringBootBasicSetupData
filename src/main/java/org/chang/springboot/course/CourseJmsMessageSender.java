package org.chang.springboot.course;

import javax.jms.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class CourseJmsMessageSender {

  @Autowired
  private JmsTemplate jmsTemplate;

  @Autowired
  private Queue queue;

  public void sendMessage(Course course) {
    System.out.println("Jms Message Sender : " + course);
    jmsTemplate.convertAndSend(queue, course);
  }
}
