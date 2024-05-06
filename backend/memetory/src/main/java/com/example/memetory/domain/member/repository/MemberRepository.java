package com.example.memetory.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.memetory.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {
}
