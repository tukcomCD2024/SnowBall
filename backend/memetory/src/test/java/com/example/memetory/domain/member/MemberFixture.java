package com.example.memetory.domain.member;

import com.example.memetory.domain.member.dto.MemberServiceDto;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.entity.Role;
import com.example.memetory.domain.member.entity.SocialType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberFixture {
	public final static Member MEMBER = Member.builder()
		.email("junrain@ourservice.com")
		.role(Role.USER)
		.imageUrl("imageUrl")
		.nickname("이준우")
		.socialType(SocialType.GOOGLE)
		.socialId("-1")
		.build();

	public final static Member GUEST_MEMBER = Member.builder()
		.email("junrain@ourservice.com")
		.role(Role.GUEST)
		.imageUrl("imageUrl")
		.nickname("이준우")
		.socialType(SocialType.GOOGLE)
		.socialId("-1")
		.build();

	public final static MemberServiceDto MEMBER_SERVICE_DTO = MemberServiceDto.builder()
		.imageUrl("imageUrl")
		.nickname("junRain")
		.email("junrain@ourservice.com")
		.build();
}
