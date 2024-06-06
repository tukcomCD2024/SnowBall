package com.example.memetory.domain.meme.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.dto.AIServerSendDto;
import com.example.memetory.domain.meme.dto.MemePageResponse;
import com.example.memetory.domain.meme.dto.MemeResponse;
import com.example.memetory.domain.meme.dto.MemeServiceDto;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.exception.AccessDeniedMemeException;
import com.example.memetory.domain.meme.exception.NotFoundMemeException;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemeService {
	private final MemberService memberService;
	private final MemeRepository memeRepository;
	private final FirebaseApp firebaseApp;

	@Value("${spring.ai-server.url}")
	private String aiServerUrl;

	@Transactional
	public MemeResponse registerMeme(MemeServiceDto memeServiceDto) {
		Member member = memberService.findMemberFromId(memeServiceDto.getMemberId());
		Meme meme = memeServiceDto.toEntityFromMember(member);

		Meme savedMeme = memeRepository.save(meme);

		// TODO FCM을 통한 알림 전송 구현
		Message message = Message.builder()
			.setToken(member.getFcmToken())
			.setNotification(
				Notification.builder()
					.setTitle("밈 생성 완료")
					.setBody("밈 생성이 완료되었습니다.")
					.build()
			)
			.putData("time", LocalDateTime.now().toString())
			.build();


		try {
			String response = FirebaseMessaging.getInstance(firebaseApp).send(message);
			log.info("Sent message: {}", response);
		} catch (FirebaseMessagingException e) {
			log.error("cannot send message by token. error info : {}", e.getMessage());
		}

		return MemeResponse.of(savedMeme);
	}

	@Transactional(readOnly = true)
	public void sendToMemeServer(MemeServiceDto memeServiceDto) {
		WebClient.create(aiServerUrl)
			.post()
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(convertMemeServiceDtoIntoJson(memeServiceDto)))
			.retrieve()
			.bodyToMono(Void.class)
			.subscribe();
	}

	private String convertMemeServiceDtoIntoJson(MemeServiceDto memeServiceDto) {
		Gson gson = new Gson();

		Member member = memberService.findMemberFromEmail(memeServiceDto.getEmail());

		AIServerSendDto aiServerSendDto = AIServerSendDto.builder()
			.memberId(member.getId())
			.scene(memeServiceDto.getScene())
			.build();

		return gson.toJson(aiServerSendDto);
	}

	@Transactional(readOnly = true)
	public MemeResponse findMemberMemeResponse(MemeServiceDto memeServiceDto) {
		Meme meme = memeRepository.findById(memeServiceDto.getMemeId()).orElseThrow(NotFoundMemeException::new);

		Member loginMember = memberService.findMemberFromEmail(memeServiceDto.getEmail());
		Member memeMember = meme.getMember();
		certifyMemeMember(memeMember, loginMember);

		return MemeResponse.of(meme);
	}

	public void certifyMemeMember(Member m1, Member m2) {
		if (!m1.equals(m2)) {
			throw new AccessDeniedMemeException();
		}
	}

	@Transactional
	public MemePageResponse findMemberMemePageResponse(MemeServiceDto memeServiceDto, Pageable pageable) {
		Member member = memberService.findMemberFromEmail(memeServiceDto.getEmail());

		Page<MemeResponse> memeList = memeRepository.findAllByMember(member, pageable);

		return MemePageResponse.builder()
			.totalPage(memeList.getTotalPages())
			.currentPage(pageable.getPageNumber())
			.memeList(memeList.getContent())
			.build();
	}

	@Transactional(readOnly = true)
	public Meme findMemeFromId(Long memeId) {
		return memeRepository.findById(memeId).orElseThrow(NotFoundMemeException::new);
	}
}
