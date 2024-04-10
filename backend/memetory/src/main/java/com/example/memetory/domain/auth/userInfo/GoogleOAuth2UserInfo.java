package com.example.memetory.domain.auth.userInfo;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

	public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return (String)attributes.get("id");
	}

	@Override
	public String getName() {
		return (String)attributes.get("name");
	}

	@Override
	public String getImageUrl() {
		return (String)attributes.get("picture");
	}
}

