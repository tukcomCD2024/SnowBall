//
//  AppDelegate.swift
//  Memetory
//
//  Created by 이승진 on 2024/01/24.
//

import UIKit
import AWSS3
import KakaoSDKCommon
import AWSCore

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        sleep(3) // 런치스크린 3초 유지
        KakaoSDK.initSDK(appKey: "15e306b70c4ce1164dca0a70a9d39c94")
        let navigationBarAppearance = UINavigationBar.appearance()
        navigationBarAppearance.tintColor = .black
        
//        return true
        
         // AWS S3
//        let credentialProvider = AWSCognitoCredentialsProvider(regionType: .APNortheast2, identityPoolId: S3Configuration.IDENTITY_POOL_ID.rawValue)
//        guard let configuration = AWSServiceConfiguration(region: .APNortheast2, credentialsProvider: credentialProvider) else {
//            print("[ FAILED TO CONNECT AWS S3 ]")
//            return false
//        }
//        AWSS3TransferUtility.register(with: configuration, forKey: S3Configuration.CALLBACK_KEY.rawValue)
//        print("[ SUCCESS TO CONNECT AWS S3 ]")
//        return true
        
        print("Start loading environment")
        // 환경 변수 로드
        Environment.load()
        
        // 환경 변수 자격 증명 읽기
        guard let accessKey = ProcessInfo.processInfo.environment["AWS_ACCESS_KEY"],
              let secretKey = ProcessInfo.processInfo.environment["AWS_SECRET_KEY"] else {
            print("Missing AWS")
            return false
        }
        print("엑세스키: \(accessKey)")
        print("시크릿키: \(secretKey)")
        
        let credentialsProvider = AWSStaticCredentialsProvider(accessKey: accessKey, secretKey: secretKey)
        let configuration = AWSServiceConfiguration(region: .APNortheast2, credentialsProvider: credentialsProvider)
        AWSServiceManager.default().defaultServiceConfiguration = configuration
        
        print("[ SUCCESS TO CONNECT AWS S3 ]")
        return true
    }
    
    // MARK: UISceneSession Lifecycle
    
    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }
    
    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }
    
    
}

