package com.example.memetory.domain.complain.repository;

import com.example.memetory.domain.complain.entity.Complain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplainRepository extends JpaRepository<Complain, Long> {
}
