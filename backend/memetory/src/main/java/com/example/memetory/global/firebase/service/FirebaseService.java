package com.example.memetory.global.firebase.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.memetory.global.firebase.FirebaseMessage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirebaseService {
	private final FirebaseApp firebaseApp;

	public void sendMessage(Message message) {
		try {
			String response = FirebaseMessaging.getInstance(firebaseApp).send(message);
			log.info("Sent message: {}", response);
		} catch (FirebaseMessagingException e) {
			log.error("cannot send message by token. error info : {}", e.getMessage());
		}
	}

	private Message buildMessage(FirebaseMessage message, String fcmToken) {
		return Message.builder()
			.setToken(fcmToken)
			.setNotification(
				Notification.builder()
					.setTitle(message.getTitle())
					.setBody(message.getBody())
					.build()
			)
			.putData("time", LocalDateTime.now().toString())
			.build();
	}
}
