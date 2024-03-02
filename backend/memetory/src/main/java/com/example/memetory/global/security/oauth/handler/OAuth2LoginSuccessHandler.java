package com.example.memetory.global.security.oauth.handler;

import com.example.memetory.domain.member.entity.Role;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.service.JwtService;
import com.example.memetory.global.security.oauth.CustomOAuth2User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtService jwtService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		log.info("OAuth2 Login 성공!");
		try {
			CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();

			// User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
			// header에 accesstoken은 잘 넘어가는지, 페이지는 알맞게 변경되는지 등을 고려해서 다시 봐야 함.
			if (oAuth2User.getRole() == Role.GUEST) {
				String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
				response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
				// response.sendRedirect("oauth2/sign-up"); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트

				jwtService.setAccessTokenHeader(response, accessToken);

			} else {
				loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
			}
		} catch (Exception e) {
			throw e;
		}

	}

	private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
		String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
		String refreshToken = jwtService.createRefreshToken();

		jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
		jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
	}
}
