package com.example.memetory.domain.complain.service;

import com.example.memetory.domain.complain.dto.ComplainServiceDto;
import com.example.memetory.domain.complain.entity.Complain;
import com.example.memetory.domain.complain.exception.NotFoundComplainException;
import com.example.memetory.domain.complain.repository.ComplainRepository;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.service.MemesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComplainService {

    private final ComplainRepository complainRepository;
    private final MemberService memberService;
    private final MemesService memesService;

    @Transactional
    public void register(ComplainServiceDto complainServiceDto) {
        Member foundMember = memberService.findMemberFromEmail(complainServiceDto.getEmail());
        Memes foundMemes = memesService.findMemesFromMemesId(complainServiceDto.getMemesId());

        Complain newComplain = complainServiceDto.toEntity(foundMember, foundMemes);
        complainRepository.save(newComplain);
    }

    @Transactional
    public void delete(ComplainServiceDto complainServiceDto) {
        Complain foundComplain = complainRepository.findById(complainServiceDto.getComplainId()).orElseThrow(NotFoundComplainException::new);

        complainRepository.delete(foundComplain);
    }
}
