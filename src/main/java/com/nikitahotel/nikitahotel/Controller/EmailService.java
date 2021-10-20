package com.nikitahotel.nikitahotel.Controller;

import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.nikitahotel.nikitahotel.POJO.Reservation;
import com.nikitahotel.nikitahotel.POJO.UserDetails;

@Component
public class EmailService {

	@Autowired
	SpringTemplateEngine templateEngine;

	@Autowired
	private JavaMailSenderImpl emailSender;

	public void sendSimpleMessage(UserDetails guestInfo, Map<String, Object> returnMap, boolean canceled) throws MessagingException {

		MimeMessage mailMessage = emailSender.createMimeMessage();

		InternetAddress[] toInternetAddress = InternetAddress.parse(guestInfo.getEmail());
		mailMessage.setFrom("noreply@nikitahotel.com");
		mailMessage.setRecipients(Message.RecipientType.TO, toInternetAddress);
		if(canceled) {
			mailMessage.setSubject("NikitaKumbhaniJAVA - Your booking is canceled");	
		} else {
			mailMessage.setSubject("NikitaKumbhaniJAVA - Your booking is confirmed");
		}

		final Context ctx = new Context();
		ctx.setVariable("userName", guestInfo.getFirstname() + " " + guestInfo.getLastname());
		ctx.setVariable("reservationDetails", returnMap.get("reservationDetails"));
		ctx.setVariable("roomDetails", returnMap.get("roomDetails"));
		ctx.setVariable("canceled", canceled);

		final String htmlContent = this.templateEngine.process("email-templates/bookingconfirmedTemplate.html", ctx);
		mailMessage.setContent(htmlContent, "text/html");
		emailSender.send(mailMessage);

	}
}
