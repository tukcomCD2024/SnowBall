//
//  VoiceManager.swift
//  Memetory
//
//  Created by 이승진 on 2024/05/15.
//

import UIKit
import AVFoundation
import AWSS3
import AWSCore

class VoiceManager {
    
    let S3BucketName = "memetory"
    
    func uploadVoiceToS3(_ voiceURL: URL, completion: @escaping (String?) -> Void) {

        
        // S3 전송 유틸리티 구성
        let transferUtility = AWSS3TransferUtility.default()
        
        // 파일 이름을 위한 날짜 포맷 설정
        let currentDate = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyyMMddhhmmssSSS"
        let fileName = dateFormatter.string(from: currentDate) + ".m4a" // 음성 파일 형식에 따라 확장자를 변경할 수 있습니다.
        
        // 음성 파일 업로드
        transferUtility.uploadFile(
            voiceURL,
            bucket: S3BucketName,
            key: fileName, // 음성 파일 이름 설정
            contentType: "audio/m4a", // 음성 파일 타입에 따라 적절한 MIME 타입을 설정해야 합니다.
            expression: nil,
            completionHandler: { (task, error) in
                if let error = error {
                    print("Error uploading voice: \(error.localizedDescription)")
                    completion(nil)
                } else {
                    print("Voice uploaded successfully!")
                    // 업로드 성공 시 업로드된 음성 파일의 URL을 반환합니다.
                    let voiceURLString = "https://\(self.S3BucketName).s3.amazonaws.com/\(fileName)"
                    completion(voiceURLString)
                }
            }
        )
    }
}
