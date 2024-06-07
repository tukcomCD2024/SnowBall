package com.example.memetory.global.firebase;

import java.time.LocalDateTime;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FirebaseMessage {
	MEME_CREATE_MESSAGE("밈 생성 완료", "밈 생성이 완료되었습니다."),
	;

	private String title;
	private String body;

	public Message toMessageWithFcmToken(String fcmToken) {
		return Message.builder()
			.setToken(fcmToken)
			.setNotification(
				Notification.builder()
					.setTitle(this.getTitle())
					.setBody(this.getBody())
					.build()
			)
			.putData("time", LocalDateTime.now().toString())
			.build();
	}
}
