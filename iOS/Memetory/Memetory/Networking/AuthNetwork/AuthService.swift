//
//  AuthService.swift
//  Memetory
//
//  Created by 이승진 on 2024/04/10.
//

import Foundation

class AuthService : BaseService {
    static let shared = AuthService()
    override private init(){}
    
    func authWithKakao(_ authCode : String, _ completion : @escaping (AuthModelResponse)-> Void){
//        authRequest( router: AuthRouter.kakao(authCode: authCode), completion: completion)
        authRequest(router: AuthRouter.kakao(authCode: authCode), completion: completion)
    }
    
    func authWithApple(_ authCode : String, userName : String, email : String,nonce : String, _ completion : @escaping (AuthModelResponse)-> Void){
        authRequest( router: AuthRouter.apple(authCode: authCode, email: email, userName: userName,nonce: nonce), completion: completion)
    }
    
    func authWithGoogle(_ authCode : String, _ completion : @escaping (AuthModelResponse)-> Void){
//        authRequest( router: AuthRouter.google(authCode: authCode), completion: completion)
        authRequest(router: AuthRouter.google(authCode: authCode), completion: completion)
    }
}
