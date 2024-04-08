//
//  File.swift
//  Memetory
//
//  Created by 이승진 on 2024/04/01.
//

import Foundation
import AWSS3

// AWS S3 관련 설정
let credentialsProvider = AWSStaticCredentialsProvider(accessKey: "YOUR_ACCESS_KEY", secretKey: "YOUR_SECRET_KEY")
let configuration = AWSServiceConfiguration(region: .USEast1, credentialsProvider: credentialsProvider)
AWSServiceManager.default().defaultServiceConfiguration = configuration

// 업로드할 이미지 데이터
let image = UIImage(named: "your_image_file")
let imageData = image.jpegData(compressionQuality: 0.8)!

// S3에 이미지 업로드
let S3BucketName = "your_bucket_name"
let remoteName = "your_remote_file_name.jpg"

let request = AWSS3TransferManagerUploadRequest()!
request.bucket = S3BucketName
request.key = remoteName
request.body = URL(fileURLWithPath: NSTemporaryDirectory()).appendingPathComponent(remoteName)
request.body = imageData as NSData
request.contentType = "image/jpeg"

let transferManager = AWSS3TransferManager.default()
transferManager.upload(request).continueWith { (task) -> Any? in
    if let error = task.error {
        print("Error uploading image: \(error.localizedDescription)")
    } else {
        print("Image uploaded successfully.")
        // 이미지 업로드가 성공하면 추가 작업 수행
    }
    return nil
}
