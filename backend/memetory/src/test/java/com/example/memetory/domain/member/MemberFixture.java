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
		.name("이준우")
		.nickname("junRain")
		.socialType(SocialType.GOOGLE)
		.socialId("-1")
		.build();


	public final static MemberServiceDto MEMBER_SERVICE_DTO = MemberServiceDto.builder()
		.imageUrl("imageUrl")
		.nickname("junRain")
		.email("junrain@ourservice.com")
		.build();

	public final static MemberServiceDto UPDATE_MEMBER_SERVICE_DTO = MemberServiceDto.builder()
		.imageUrl("imageUrl2")
		.nickname("junRain2")
		.email("junrain@ourservice.com")
		.build();
}
