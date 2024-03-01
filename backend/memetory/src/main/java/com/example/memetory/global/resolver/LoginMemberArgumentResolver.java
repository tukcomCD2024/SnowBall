package com.example.memetory.global.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.global.annotation.LoginMember;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

	private final MemberService memberService;

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.hasParameterAnnotation(LoginMember.class);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
		NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
		return memberService.getLoginMember();
	}
}