package com.example.memetory.domain.memes.service;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.service.MemeService;
import com.example.memetory.domain.memes.dto.MemesInfo;
import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.dto.response.MemesListResponse;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.exception.NotFoundMemesException;
import com.example.memetory.domain.memes.repository.MemesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemesService {
    private final MemberService memberService;
    private final MemeService memeService;
    private final MemesRepository memesRepository;

    @Transactional
    public void register(MemesServiceDto memesServiceDto) {
        Member foundMember = memberService.findByEmail(memesServiceDto.getEmail());
        Meme foundMeme = memeService.getMemeBetweenService(memesServiceDto.getMemeId());

        Memes newMemes = memesServiceDto.toEntity(foundMember, foundMeme);
        memesRepository.save(newMemes);
    }

    @Transactional(readOnly = true)
    public MemesListResponse findTopTenMemesByLike() {
        List<MemesInfo> memesList = memesRepository.findTopTenMemesByLikeCount(PageRequest.of(0,10))
                .stream()
                .map(MemesInfo::of)
                .toList();

        return MemesListResponse.builder()
                .memesInfoList(memesList)
                .build();
    }

    @Transactional(readOnly = true)
    public MemesListResponse findTopTenMemesByLikeForMonth() {

        // 현재 시점부터 한달 전의 LocalDateTime
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);

        // 상위 10개만 가져오기 위해 pageable 적용
        List<MemesInfo> memesList = memesRepository.findTopTenMemesByLikeCountForMonth(PageRequest.of(0,10), monthAgo)
                .stream()
                .map(MemesInfo::of)
                .toList();

        return MemesListResponse.builder()
                .memesInfoList(memesList)
                .build();
    }

    @Transactional(readOnly = true)
    public Memes getMemesBetweenService(Long memesId) {
        Memes foundMemes = memesRepository.findById(memesId).orElseThrow(NotFoundMemesException::new);
        return foundMemes;
    }
}
