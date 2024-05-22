package com.example.memetory.domain.member.controller;

import static com.example.memetory.global.response.ErrorCode.*;
import static com.example.memetory.global.response.ResultCode.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.example.memetory.domain.member.dto.request.MemberUpdateRequest;
import com.example.memetory.domain.member.exception.DuplicatedMemberException;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.global.LoginTest;

@DisplayName("Member 컨트롤러 테스트의 ")
@WebMvcTest(MemberController.class)
public class MemberControllerTest extends LoginTest {
	@MockBean
	private MemberService memberService;

	@Test
	@DisplayName("멤버 업데이트 성공")
	public void 멤버_업데이트_성공() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(post("/member").contentType(MediaType.APPLICATION_JSON)
			.content(toRequestBody(new MemberUpdateRequest("junrain2", "imageUrl2")))
			.header("Authorization", "Bearer " + accessToken)).andDo(print());

		// then
		perform.andExpect(status().isOk()).andExpect(jsonPath(MESSAGE).value(UPDATE_MEMBER_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("멤버 업데이트 실패(닉네임 중복)")
	public void 멤버_업데이트_실패_닉네임_중복() throws Exception {
		// given -> 결과에 대한 객체, Member 객체 저장할 필요 존재
		doThrow(new DuplicatedMemberException()).when(memberService).update(any());

		// when
		final ResultActions perform = mockMvc.perform(post("/member").contentType(MediaType.APPLICATION_JSON)
			.content(toRequestBody(new MemberUpdateRequest("junrain", "imageUrl")))
			.header("Authorization", "Bearer " + accessToken)).andDo(print());

		// then
		perform.andExpect(status().isConflict())
			.andExpect(jsonPath(ERROR_MESSAGE).value(NICKNAME_IS_DUPLICATED.getMessage()));
	}
}
