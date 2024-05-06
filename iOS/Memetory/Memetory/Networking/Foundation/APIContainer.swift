//
//  APIContainer.swift
//  Memetory
//
//  Created by 이승진 on 2024/04/11.
//

import Foundation

struct APIContainer<T: Codable> : Codable{
    var message : String
    var code : String
    var isSuccess : Bool
    var result : T
}
