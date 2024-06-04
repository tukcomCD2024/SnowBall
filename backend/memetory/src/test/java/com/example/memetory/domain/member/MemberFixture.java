package com.example.memetory.domain.member;

import com.example.memetory.domain.member.dto.MemberServiceDto;
import com.example.memetory.domain.member.dto.request.MemberUpdateRequest;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.entity.Role;
import com.example.memetory.domain.member.entity.SocialType;

public class MemberFixture {
	public final static Member MEMBER() {
		return Member.builder()
			.email("junrain@ourservice.com")
			.role(Role.USER)
			.imageUrl("imageUrl")
			.name("이준우")
			.nickname("junRain")
			.socialType(SocialType.GOOGLE)
			.socialId("123456789")
			.build();
	}

	public final static Member OTHER_MEMBER() {
		return Member.builder()
			.email("jerry@ourservice.com")
			.role(Role.USER)
			.imageUrl("imageUrl")
			.name("강재혁")
			.nickname("goDDm")
			.socialType(SocialType.GOOGLE)
			.socialId("-2")
			.build();
	}

	public final static MemberServiceDto MEMBER_SERVICE_DTO() {
		return MemberServiceDto.builder()
			.imageUrl("imageUrl")
			.nickname("junRain")
			.email("junrain@ourservice.com")
			.build();
	}

	public final static MemberServiceDto UPDATED_MEMBER_SERVICE_DTO() {
		return MemberServiceDto.builder()
			.imageUrl("imageUrl2")
			.nickname("junRain2")
			.email("junrain@ourservice.com")
			.build();
	}

	public final static MemberUpdateRequest MEMBER_UPDATE_REQUEST() {
		return new MemberUpdateRequest("junrain2", "imageUrl2");
	}

	public final static MemberUpdateRequest DUPLICATED_NICKNAME_MEMBER_UPDATE_REQUEST() {
		return new MemberUpdateRequest("junrain", "imageUrl2");
	}

}
