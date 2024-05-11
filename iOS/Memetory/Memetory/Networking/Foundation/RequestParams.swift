//
//  RequestParams.swift
//  Memetory
//
//  Created by 이승진 on 2024/04/11.
//

import Foundation

enum RequestParams {
    case body(_ parameter: Encodable?)
    case bodyFromDictionary(_ parameter : Dictionary<String, Any>)
    case query(_ parameter: Encodable?)
    case none
}
