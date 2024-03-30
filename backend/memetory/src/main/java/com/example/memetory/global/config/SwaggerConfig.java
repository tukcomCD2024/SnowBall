package com.example.memetory.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .servers(getServers())  // 호스팅 되는 서버의 정보
            .info(getInfo())        // API에 대한 기본 정보
            .components(new Components()    // Swagger의 구성 요소
                .addSecuritySchemes("access_token", accessTokenSecuritySchema())
            );
    }

    private List<Server> getServers() {
        return List.of(
            new Server()
                .url("/")
                .description("백엔드 api 서버")
        );
    }

    private Info getInfo() {
        return new Info()
            .title("Memetory API")
            .description("AI를 이용한 영상제작 서비스")
            .version("v1");
    }

    // 보안 스키마 추가
    private SecurityScheme accessTokenSecuritySchema() {
        return new SecurityScheme()
            .name("Authorization")  // 식별자
            .scheme("bearer")   // Bearer 토큰 사용을 위한 스키마 설정
            .bearerFormat("JWT")    // 사용할 Bearer 토큰 형식 설정 (여기서는 JWT 설정)
            .in(SecurityScheme.In.HEADER)   // 인증 정보가 포함된 위치 (쿠키, 헤더, 쿼리 3가지 중 하나)
            .type(SecurityScheme.Type.HTTP);    // HTTP를 통한 인증 수행
    }
}
