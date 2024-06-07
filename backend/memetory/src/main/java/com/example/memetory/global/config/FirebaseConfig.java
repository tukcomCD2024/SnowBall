package com.example.memetory.global.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {
	@Value("${spring.firebase.path}")
	private String firebasePath;

	@Bean
	public FirebaseApp initializeFirebase() {
		try {
			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(
					GoogleCredentials.fromStream(new ClassPathResource(firebasePath).getInputStream()))
				.build();
			if (FirebaseApp.getApps().isEmpty()) {
				return FirebaseApp.initializeApp(options);
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return FirebaseApp.getInstance();
	}
}
