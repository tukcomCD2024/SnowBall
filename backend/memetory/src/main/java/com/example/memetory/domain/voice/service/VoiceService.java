package com.example.memetory.domain.voice.service;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.voice.dto.VoiceServiceDto;
import com.example.memetory.domain.voice.entity.Voice;
import com.example.memetory.domain.voice.repository.VoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoiceService {

    private final VoiceRepository voiceRepository;
    private final MemberService memberService;

    @Transactional
    public void register(VoiceServiceDto voiceServiceDto) {
        Member foundMember = memberService.findByEmail(voiceServiceDto.getEmail());
        Voice newVoice = voiceServiceDto.toEntity(foundMember);

        voiceRepository.save(newVoice);
    }
}
