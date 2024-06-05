package com.example.memetory.domain.auth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.memetory.domain.auth.dto.LoginRequest;
import com.example.memetory.domain.auth.userInfo.OAuth2UserInfo;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.entity.Role;
import com.example.memetory.domain.member.entity.SocialType;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.service.JwtService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final MemberRepository memberRepository;
	private final OAuth2ProviderService oAuth2ProviderService;
	private final JwtService jwtService;

	@Transactional
	public void authenticateOrRegisterUser(LoginRequest loginRequest, HttpServletResponse response) {
		OAuth2UserInfo userInfo = oAuth2ProviderService.getUserInfo(loginRequest);
		Member member = findOrElseRegisterMember(userInfo, loginRequest.getSocialType());
		member.updateFcmToken(loginRequest.getFcmToken());
		jwtService.sendAccessAndRefreshToken(response, member.getEmail());
	}

	private Member findOrElseRegisterMember(OAuth2UserInfo userInfo, SocialType socialType) {
		return memberRepository.findBySocialTypeAndSocialId(socialType, userInfo.getId())
			.orElseGet(() -> registerMember(socialType, userInfo));
	}

	private Member registerMember(SocialType socialType, OAuth2UserInfo userInfo) {
		Member member = Member.builder()
			.socialType(socialType)
			.socialId(userInfo.getId())
			.email(UUID.randomUUID() + "@socialUser.com")
			.name(userInfo.getName())
			.nickname(String.valueOf(UUID.randomUUID()))
			.imageUrl(userInfo.getImageUrl())
			.role(Role.USER)
			.build();

		return memberRepository.save(member);
	}
}
