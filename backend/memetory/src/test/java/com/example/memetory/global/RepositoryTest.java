package com.example.memetory.global;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.memetory.domain.comment.repository.CommentQDtoFactory;
import com.example.memetory.domain.meme.repository.MemeQDtoFactory;
import com.example.memetory.domain.memes.repository.MemesQDtoFactory;
import com.example.memetory.global.config.JpaAuditingConfig;
import com.example.memetory.global.config.QueryDslConfig;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@Import({JpaAuditingConfig.class, QueryDslConfig.class, MemeQDtoFactory.class, MemesQDtoFactory.class,
	CommentQDtoFactory.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public @interface RepositoryTest {
}
