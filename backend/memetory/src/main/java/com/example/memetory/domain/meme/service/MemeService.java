package com.example.memetory.domain.meme.service;

import static com.example.memetory.global.firebase.FirebaseMessage.*;

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
import com.example.memetory.global.firebase.service.FirebaseService;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemeService {
	private final MemberService memberService;
	private final MemeRepository memeRepository;
	private final FirebaseService firebaseService;

	@Value("${spring.ai-server.url}")
	private String aiServerUrl;

	@Transactional
	public MemeResponse registerMeme(MemeServiceDto memeServiceDto) {
		Member member = memberService.findMemberFromId(memeServiceDto.getMemberId());
		Meme meme = memeServiceDto.toEntityFromMember(member);

		Meme savedMeme = memeRepository.save(meme);

		firebaseService.sendMessage(MEME_CREATE_MESSAGE.toMessageWithFcmToken(member.getFcmToken()));

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
