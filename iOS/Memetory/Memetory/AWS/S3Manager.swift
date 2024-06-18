//
//  S3Manager.swift
//  Memetory
//
//  Created by 이승진 on 2024/06/17.
//

import UIKit
import AWSS3

class S3Manager {
    static let shared = S3Manager()
    
    let S3BucketName = "memetory"
    
    private init() {
        
    }
    
    func uploadImage(image: UIImage, completion: @escaping (String?) -> Void) {
        guard let imageData = image.jpegData(compressionQuality: 0.9) else {
            print("imgae X")
            completion(nil)
            return
        }
        let transferUtility = AWSS3TransferUtility.default()
        
        let expression = AWSS3TransferUtilityUploadExpression()
        expression.setValue("AES256", forRequestHeader: "x-amz-server-side-encryption")
        expression.progressBlock = { (task, progress) in
            DispatchQueue.main.async {
                print("[ Upload progress ]: \(progress.fractionCompleted)")
            }
        }
        
        let currentDate = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyyMMddhhmmssSSS"
        let fileName = dateFormatter.string(from: currentDate) + ".jpg"
        
        transferUtility.uploadData(
            imageData,
            bucket: S3BucketName,
            key: fileName,
            contentType: "image/jpg",
            expression: expression
        ) { (task, error) in
            if let error = error {
                print("Error uploading image: \(error.localizedDescription)")
                completion(nil)
            } else {
                print("Image uploaded successfully!")
                completion(fileName)
            }
        }
    }
    
    
    func uploadFile(data: Data, bucketName: String, key: String, completion: @escaping (Bool, Error?) -> Void) {
        let expression = AWSS3TransferUtilityUploadExpression()
        let transferUtility = AWSS3TransferUtility.default()
        
        transferUtility.uploadData(data, bucket: bucketName, key: key, contentType: "text/plain", expression: expression) { task, error in
            if let error = error {
                print("Upload failed with error: \(error)")
                completion(false, error)
            } else {
                print("Upload successful")
                completion(true, nil)
            }
        }
    }
    
    func downloadFile(bucketName: String, key: String, completion: @escaping (Data?, Error?) -> Void) {
        let expression = AWSS3TransferUtilityDownloadExpression()
        let transferUtility = AWSS3TransferUtility.default()
        
        transferUtility.downloadData(fromBucket: bucketName, key: key, expression: expression) { task, url, data, error in
            if let error = error {
                print("Download failed with error: \(error)")
                completion(nil, error)
            } else {
                print("Download successful")
                completion(data, nil)
            }
        }
    }
}
