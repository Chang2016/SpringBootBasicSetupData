package org.chang.springboot.course;

import javax.jms.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * If you want to use dynamic destination creation, you must specify the type of JMS destination to
 * create, using the "pubSubDomain" property. For other operations, this is not necessary.
 * Point-to-Point (Queues) is the default domain.
 *
 * Default settings for JMS Sessions are "not transacted" and "auto-acknowledge".
 */
@Component
public class CourseJmsMessageSender {

  private final Logger logger = LoggerFactory.getLogger(CourseJmsMessageSender.class);

  @Autowired
  private JmsTemplate jmsTemplate;

  @Autowired
  private Queue queue;

  public void sendMessage(CourseDto course) {
    logger.info(String.format("Jms Message Sender : %s", course));
    jmsTemplate.convertAndSend(queue, course);
  }
}
