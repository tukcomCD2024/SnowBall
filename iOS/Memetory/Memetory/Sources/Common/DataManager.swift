//
//  DataManager.swift
//  Memetory
//
//  Created by 이승진 on 2024/04/15.
//

import Foundation
import UIKit

struct DataManager {
    static let shared = DataManager()
    let defaults = UserDefaults.standard
    private init(){}
    
    //첫 앱 실행 확인
    func setIsFirstTime(){
        defaults.set(true, forKey: "isFirstTime")
    }
    
    func getIsFirstTime()->Bool{
        return defaults.bool(forKey: "isFirstTime")
    }
    
    func setIsLogin(){
        defaults.set(true, forKey: "Login")
    }
    
    func logout(){
        defaults.set(false, forKey: "Login")
    }
    
    func getIsLogin() -> Bool {
        return defaults.bool(forKey: "Login")
    }
    
    func getUserName()->String {
        return defaults.string(forKey: "UserName") ?? "Error"
    }
    func getUserImage()->Data{
        return defaults.data(forKey: "ProfilePicture")!
    }
    
//    func setMyPost(myPosts : [LocalPostDTO]){
//        let encoder = JSONEncoder()
//        var list = [Data]()
//        for post in myPosts {
//            if let encoded = try? encoder.encode(post) {
//                list.append(encoded)
//            }
//        }
//        defaults.set(list, forKey: "myPosts")
//    }
//    
//    func getMyPost()->[LocalPostDTO] {
//        var list = [LocalPostDTO]()
//        if let savedData = defaults.array(forKey: "myPosts") as? [Data] {
//            let decoder = JSONDecoder()
//            for data in savedData {
//                if let savedObject = try? decoder.decode(LocalPostDTO.self, from: data) {
//                    list.append(savedObject)
//                }
//            }
//        }
//        
//        return list
//    }
    
    func saveImage(image : UIImage, key: String){
        let imageData = image.jpegData(compressionQuality: 0.5)
        defaults.set(imageData, forKey: key)
    }
    
    func saveText(text:String,key:String){
        //refresh token 앞 8자리로 저장
        defaults.set(text, forKey: key)
    }
}

