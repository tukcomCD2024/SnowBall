package com.example.memetory.domain.voice.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.voice.dto.VoiceServiceDto;
import com.example.memetory.domain.voice.entity.Voice;
import com.example.memetory.domain.voice.exception.AlreadyExistVoiceException;
import com.example.memetory.domain.voice.exception.NotFoundVoiceException;
import com.example.memetory.domain.voice.repository.VoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VoiceService {

    private final VoiceRepository voiceRepository;
    private final MemberService memberService;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public void register(VoiceServiceDto voiceServiceDto) {
        Member foundMember = memberService.findMemberFromEmail(voiceServiceDto.getEmail());
        Voice newVoice = voiceServiceDto.toEntity(foundMember);

        voiceRepository.save(newVoice);
    }

    @Transactional(readOnly = true)
    public String findVoiceByMemberId(VoiceServiceDto voiceServiceDto) {
        Voice foundVoice = findVoiceByMemberEmail(voiceServiceDto);
        return foundVoice.getElevenlabsVoiceId();
    }

    public void deleteVoice(VoiceServiceDto voiceServiceDto) {
        Voice foundVoice = findVoiceByMemberEmail(voiceServiceDto);
        voiceRepository.delete(foundVoice);
    }

    private Voice findVoiceByMemberEmail(VoiceServiceDto voiceServiceDto) {
        Member foundMember = memberService.findMemberFromEmail(voiceServiceDto.getEmail());
        return voiceRepository.findByMemberId(foundMember.getId()).orElseThrow(NotFoundVoiceException::new);
    }

    // 목소리 생성
    public MultiValueMap<String, Object> generateVoice(VoiceServiceDto voiceServiceDto) throws IOException {
        S3Object s3Object = getS3File(voiceServiceDto);

        return createFormData(s3Object, voiceServiceDto);
    }

    // 목소리가 이미 생성되어 있는지 체크
    public void isExistVoice(VoiceServiceDto voiceServiceDto) {
        Member foundMember = memberService.findMemberFromEmail(voiceServiceDto.getEmail());
        Optional<Voice> foundVoice = voiceRepository.findByMemberId(foundMember.getId());

        if (foundVoice.isPresent()) {
            throw new AlreadyExistVoiceException();
        }
    }

    // S3 파일 가져오기
    private S3Object getS3File(VoiceServiceDto voiceServiceDto) {
        return amazonS3Client.getObject(bucketName, voiceServiceDto.getS3Key());
    }

    // Elevenlabs로 보낼 formData 생성
    private MultiValueMap<String, Object> createFormData(S3Object s3Object, VoiceServiceDto voiceServiceDto) throws IOException {
        // S3Object로부터 데이터를 읽어오기 위한 InputStream 생성
        InputStream inputStream = s3Object.getObjectContent();

        // 데이터를 임시로 저장할 ByteArrayOutputStream 생성
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // 입력 스트림에서 데이터를 읽어와서 ByteArrayOutputStream에 쓰기
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        // ByteArrayOutputStream에 저장된 데이터를 byte 배열로 변환
        byte[] data = byteArrayOutputStream.toByteArray();

        // byte 배열을 파일 시스템 리소스로 변환
        FileSystemResource fileSystemResource = createFileSystemResource(data, voiceServiceDto.getS3Key());

        // Elevenlabs API 호출을 위한 폼 데이터 MultiValueMap 생성
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("files", fileSystemResource);
        formData.add("name", voiceServiceDto.getName());
        formData.add("description", voiceServiceDto.getDescription());

        // 입력 스트림 닫기
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // S3Object 닫기
        if (s3Object != null) {
            try {
                s3Object.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return formData;
    }

    private FileSystemResource createFileSystemResource(byte[] data, String s3Key) throws IOException {
        // 임시 파일 생성
        File tempFile = File.createTempFile(s3Key, "");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            // 데이터를 파일에 쓰기
            fos.write(data);
        }
        // 파일 시스템 리소스 생성하여 반환
        return new FileSystemResource(tempFile);
    }
}
