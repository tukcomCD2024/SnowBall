package com.example.memetory.domain.member.entity;

import com.example.memetory.domain.member.dto.MemberServiceDto;
import com.example.memetory.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	private String email;
	private String nickname;
	private String name;
	private String imageUrl;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	private SocialType socialType;

	private String socialId;

	@Builder
	public Member(String email, String nickname, String name, String imageUrl, Role role, SocialType socialType,
		String socialId) {
		this.email = email;
		this.nickname = nickname;
		this.name = name;
		this.imageUrl = imageUrl;
		this.role = role;
		this.socialType = socialType;
		this.socialId = socialId;
	}

	// Todo 닉네임이랑, 이미지 변경할 수 있게 하기
	public void update(MemberServiceDto memberServiceDto) {
		this.nickname = memberServiceDto.getNickname();
		this.imageUrl = memberServiceDto.getImageUrl();
	}
}
