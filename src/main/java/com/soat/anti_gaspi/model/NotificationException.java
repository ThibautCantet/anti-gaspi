package com.soat.anti_gaspi.model;

import javax.mail.MessagingException;

public class NotificationException extends Throwable {
   public NotificationException(MessagingException e) {
      super(e);
   }
}