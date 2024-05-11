//
//  AuthRouter.swift
//  Memetory
//
//  Created by 이승진 on 2024/04/10.
//

import Foundation
import Alamofire

enum AuthRouter {
    case kakao(authCode : String)
    case apple(authCode : String, email: String, userName : String, nonce : String)
    case google(authCode : String)
}

extension AuthRouter : BaseRouter {
    
    var baseURL: String {
        Constants.baseURL
    }
    
    var method: HTTPMethod {
        .post
    }
    
    var path: String {
        "/login"
    }
    
    var parameter: RequestParams {
        switch self {
        case .kakao(let code):
            return .body(AuthModelRequest(token: code, socialType: "kakao"))
        case .apple(let code, let email, let userName, let nonce):
            return .body(AppleAuthModel(accessToken: code, userName: userName, email: email, provider: "apple",nonce: nonce))
        case .google(let code):
            return .body(AuthModelRequest(token: code, socialType: "google"))
        }
    }
    
    var header: HeaderType {
        .basicHeader
    }
}
