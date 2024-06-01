package com.example.memetory.domain.member.controller;

import static com.example.memetory.domain.member.MemberFixture.*;
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

import com.example.memetory.domain.member.dto.MemberServiceDto;
import com.example.memetory.domain.member.dto.request.MemberUpdateRequest;
import com.example.memetory.domain.member.dto.response.MemberResponse;
import com.example.memetory.domain.member.exception.DuplicatedMemberException;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.global.BaseControllerTest;

@DisplayName("Member 컨트롤러 테스트의 ")
@WebMvcTest(MemberController.class)
public class MemberControllerTest extends BaseControllerTest {
	@MockBean
	private MemberService memberService;

	@Test
	@DisplayName("MemberUpdateRequest를 통한 멤버 업데이트 성공")
	public void Given_MemberUpdateRequest_When_updateMember_Then_UPDATRE_MEMBER_SUCCESS() throws Exception {
		// given
		MemberUpdateRequest request = new MemberUpdateRequest("junrain2", "imageUrl2");
		MemberResponse expectedResult = MemberResponse.of(MEMBER());
		String expectedNickname = expectedResult.getNickName();

		given(memberService.updateMember(any())).willReturn(expectedResult);

		// when
		final ResultActions perform = mockMvc.perform(post("/member")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toRequestBody(request))
			.header("Authorization", "Bearer " + accessToken))
			.andDo(print());

		// then
		perform.andExpect(status().isOk())
			.andExpect(jsonPath(MESSAGE).value(UPDATE_MEMBER_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.data.nickName").value(expectedNickname));
	}

	@Test
	@DisplayName("중복된 닉네임으로 인한 NICKNAME_IS_DUPLICATED 반환")
	public void Given_MemberUpdateRequest_When_updateMember_Then_NICKNAME_IS_DUPLICATED() throws Exception {
		// given
		MemberUpdateRequest request = new MemberUpdateRequest("junrain2", "imageUrl2");

		doThrow(new DuplicatedMemberException()).when(memberService).updateMember(any());

		// when
		final ResultActions perform = mockMvc.perform(post("/member")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toRequestBody(request))
			.header("Authorization", "Bearer " + accessToken))
			.andDo(print());

		// then
		perform.andExpect(status().isConflict())
			.andExpect(jsonPath(ERROR_MESSAGE).value(NICKNAME_IS_DUPLICATED.getMessage()));
	}

	@Test
	@DisplayName("email을 통한 MemberResponse 반환 성공")
	public void Given_email_When_findMember_Then_MemberResponse() throws Exception {
		// given
		MemberResponse expectedResult = MemberResponse.of(MEMBER());
		String expectedNickname = expectedResult.getNickName();

		given(memberService.findMemberResponse(any(MemberServiceDto.class))).willReturn(expectedResult);

		// when
		final ResultActions perform = mockMvc.perform(get("/member")
			.contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + accessToken))
			.andDo(print());

		// then
		perform.andExpect(status().isOk())
			.andExpect(jsonPath(MESSAGE).value(GET_MEMBER_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.data.nickName").value(expectedNickname));
	}
}
