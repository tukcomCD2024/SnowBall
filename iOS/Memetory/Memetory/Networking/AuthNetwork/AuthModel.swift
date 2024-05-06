//
//  AuthModel.swift
//  Memetory
//
//  Created by 이승진 on 2024/04/10.
//

import Foundation

struct AuthModelRequest : Codable {
    var token : String
    var socialType: String
}

struct AuthModelResponse : Codable {
    var accessToken : String
    var refreshToken : String
}

struct AppleAuthModel : Codable {
    var accessToken : String
    var userName : String
    var email : String
    var provider : String
    var nonce  : String
}
