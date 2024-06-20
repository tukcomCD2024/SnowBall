//
//  Scene.swift
//  Memetory
//
//  Created by 이승진 on 2024/03/05.
//

import UIKit

struct Scene : Codable {
    var sourceImage: String
    var targetImage: String
    var text: String
    var voiceId: String
    
    static let sceneImage = ["DemoPhoto01", "DemoPhoto02", "DemoPhoto03"]
    static let templateTitle = ["악마를 보았다", "신세계", "말왕", "악마와의 토크쇼", "혹성탈출", "쿵푸팬더4"]
    
    static let videoTitle = ["악마를 보았다 패러디", "신세계 패러디", "말왕 패러디", "악마와의 토크쇼 패러디", "혹성탈출 패러디", "쿵푸팬더4 패러디"]
    
    static let timeLabel = ["0:27","0:10", "0:20", "0:35", "0:12", "0:23" ]
//    lazy var sceneImage: UIImage? = {
//        return UIImage(named: "demo\(num).jpeg")
//    }()
//    
//    var num: Int
    
    static let mainLabel = ["이달의 좋아요", "이 주의 좋아요"]
}
