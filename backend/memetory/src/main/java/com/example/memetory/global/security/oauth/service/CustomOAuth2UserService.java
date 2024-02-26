package com.example.memetory.global.security.oauth.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.entity.SocialType;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.oauth.CustomOAuth2User;
import com.example.memetory.global.security.oauth.OAuthAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final MemberRepository memberRepository;

	//    private static final String NAVER = "naver";
	//    private static final String KAKAO = "kakao";

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		/**
		 * userRequest에서 registrationId 추출 후 registrationId으로 SocialType 저장
		 * http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
		 * userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
		 */
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		SocialType socialType = getSocialType(registrationId);
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값
		Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)

		// socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
		OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

		Member createdUser = getMember(extractAttributes, socialType); // getUser() 메소드로 User 객체 생성 후 반환

		// DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성해서 반환
		return new CustomOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
			attributes,
			extractAttributes.getNameAttributeKey(),
			createdUser.getEmail(),
			createdUser.getRole()
		);
	}

	private SocialType getSocialType(String registrationId) {
		//        if(KAKAO.equals(registrationId)) {
		//            return SocialType.KAKAO;
		//        }
		return SocialType.GOOGLE;
	}

	private Member getMember(OAuthAttributes attributes, SocialType socialType) {
		Member findMember = memberRepository.findBySocialTypeAndSocialId(socialType,
			attributes.getOauth2UserInfo().getId()).orElse(null);

		if (findMember == null) {
			return saveMember(attributes, socialType);
		}
		return findMember;
	}

	private Member saveMember(OAuthAttributes attributes, SocialType socialType) {
		Member createdMember = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
		return memberRepository.save(createdMember);
	}
}
