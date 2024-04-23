package com.example.memetory.domain.memes.service;

import com.example.memetory.domain.comment.dto.CommentInfo;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.service.MemeService;
import com.example.memetory.domain.memes.dto.MemesInfo;
import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.dto.response.MemesListResponse;
import com.example.memetory.domain.memes.dto.response.MemesResponse;
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

    private static final int LIMIT = 10;    // 데이터 베이스에서 가져올 데이터의 개수

    @Transactional
    public void register(MemesServiceDto memesServiceDto) {
        Member foundMember = memberService.findByEmail(memesServiceDto.getEmail());
        Meme foundMeme = memeService.getMemeBetweenService(memesServiceDto.getMemeId());

        Memes newMemes = memesServiceDto.toEntity(foundMember, foundMeme);
        memesRepository.save(newMemes);
    }

    @Transactional
    public void delete(MemesServiceDto memesServiceDto) {
        Memes foundMemes = findById(memesServiceDto.getMemesId());
        memesRepository.delete(foundMemes);
    }

    // 밈스 단일 조회
    @Transactional(readOnly = true)
    public MemesResponse findOne(MemesServiceDto memesServiceDto) {
        Memes foundMemes = findById(memesServiceDto.getMemesId());

        return buildMemesResponse(foundMemes);
    }

    // 인기차트 조회 (좋아요 순으로 상위 10개)
    @Transactional(readOnly = true)
    public MemesListResponse findTopMemesByLike() {
        List<MemesInfo> memesList = fetchTopMemesByLike();
        return buildMemesListResponse(memesList);
    }

    // 이달의 인기차트 조회 (한 달전 이후 부터 생성된 밈스 중에서 좋아요 순으로 상위 10개)
    @Transactional(readOnly = true)
    public MemesListResponse findTopMemesByLikeForMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<MemesInfo> memesList = fetchTopMemesByLikeForPeriod(oneMonthAgo);
        return buildMemesListResponse(memesList);
    }

    // 이주의 인기차트 조회 (한 주전 이후 부터 생성된 밈스 중에서 좋아요 순으로 상위 10개)
    @Transactional(readOnly = true)
    public MemesListResponse findTopMemesByLikeForWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        List<MemesInfo> memesList = fetchTopMemesByLikeForPeriod(oneWeekAgo);
        return buildMemesListResponse(memesList);
    }

    // 서비스 계층 간의 밈스 조회
    @Transactional(readOnly = true)
    public Memes getMemesBetweenService(Long memesId) {
        return findById(memesId);
    }

    private List<MemesInfo> fetchTopMemesByLike() {
        return memesRepository.findTopMemesByLikeCount(PageRequest.of(0, LIMIT))
                .stream()
                .map(MemesInfo::of)
                .toList();
    }

    private List<MemesInfo> fetchTopMemesByLikeForPeriod(LocalDateTime fromDateTime) {
        return memesRepository.findTopMemesByLikeCountForPeriod(PageRequest.of(0, LIMIT), fromDateTime)
                .stream()
                .map(MemesInfo::of)
                .toList();
    }

    private MemesResponse buildMemesResponse(Memes memes) {
        return MemesResponse.builder()
                .memesId(memes.getId())
                .memberId(memes.getMember().getId())
                .memberName(memes.getMember().getName())
                .memeUrl(memes.getMeme().getS3Url())
                .title(memes.getTitle())
                .commentCount(memes.getCommentCount())
                .commentInfoList(memes.getComments().stream().map(CommentInfo::of).toList())
                .likeCount(memes.getLikeCount())
                .createdAt(memes.getCreatedAt())
                .build();
    }

    private MemesListResponse buildMemesListResponse(List<MemesInfo> memesList) {
        return MemesListResponse.builder()
                .memesInfoList(memesList)
                .build();
    }

    private Memes findById(Long memesId) {
        return memesRepository.findByMemesId(memesId).orElseThrow(NotFoundMemesException::new);
    }
}
