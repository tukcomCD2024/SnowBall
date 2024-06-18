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
    
    static let sceneImage = ["demo1", "demo2"]
    static let templateTitle = ["해리포터", "파묘", "범죄도시4", "악마와의 토크쇼", "혹성탈출", "쿵푸팬더4"]
    
    static let videoTitle = ["해리포터 패러디", "파묘 패러디", "범죄도시4 패러디", "악마와의 토크쇼 패러디", "혹성탈출 패러디", "쿵푸팬더4 패러디"]
    
    static let timeLabel = ["0:27","0:10", "0:20", "0:35", "0:12", "0:23" ]
//    lazy var sceneImage: UIImage? = {
//        return UIImage(named: "demo\(num).jpeg")
//    }()
//    
//    var num: Int
}
