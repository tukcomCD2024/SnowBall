package com.example.memetory.global.integration;

import static com.example.memetory.domain.member.MemberFixture.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.util.JwtUtil;

import io.restassured.RestAssured;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {
	protected final String MESSAGE = "message";
	protected final String ERROR_MESSAGE = "errorMessage";

	protected Member member;
	protected String accessToken;

	@LocalServerPort
	int port;

	@Autowired
	protected MemberRepository memberRepository;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private DatabaseCleanUp databaseCleanup;

	@BeforeEach
	public void setUp() {
		if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
			RestAssured.port = port;
			databaseCleanup.afterPropertiesSet();
		}

		databaseCleanup.execute();

		member = memberRepository.save(MEMBER());
		accessToken = jwtUtil.generateAccessToken(member.getEmail());
	}
}
