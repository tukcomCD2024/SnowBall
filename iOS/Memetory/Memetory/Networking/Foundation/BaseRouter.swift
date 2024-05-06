//
//  BaseRouter.swift
//  Memetory
//
//  Created by 이승진 on 2024/04/11.
//

import Foundation
import Alamofire

protocol BaseRouter : URLRequestConvertible {
    var baseURL : String { get }
    var method : HTTPMethod { get }
    var path : String { get }
    var parameter : RequestParams { get }
    var header : HeaderType { get }
    var multipart : MultipartFormData {get}
}

extension BaseRouter {
    func asURLRequest() throws -> URLRequest {
        var url : URL
        var urlRequest : URLRequest
        if path == "" {
            url = try String(baseURL+path).asURL()
            urlRequest = try URLRequest(url: url, method : method)
        } else {
            url = try baseURL.asURL()
            urlRequest = try URLRequest(url: url.appendingPathComponent(path), method : method)
        }
        urlRequest = self.makeHeaderForRequest(to: urlRequest)
        return try self.makePrameterForRequest(to: urlRequest, with: url)
    }
    
    private func makeHeaderForRequest(to request: URLRequest) -> URLRequest {
        var request = request
        switch header {
        case .withAuth :
            // 추후 Access Token 불러오기로 변환
            request.setValue(Constants.ApplicationJson, forHTTPHeaderField: Constants.ContentType)
            request.setValue("Bearer " + SecureDataManager.shared.getData(label: .accessToken), forHTTPHeaderField: Constants.Authorization)
            //request.setValue("1", forHTTPHeaderField: Constants.Authorization)
            return request
        case .basicHeader:
            request.setValue(Constants.ApplicationJson, forHTTPHeaderField: Constants.ContentType)
            return request
        case .multiPart:
            request.setValue("Bearer " + SecureDataManager.shared.getData(label: .accessToken), forHTTPHeaderField: Constants.Authorization)
            return request
        case .noHeader :
            return request
        case .onlyAuth:
            //request.setValue("application/json", forHTTPHeaderField: Constants.ContentType)
            request.setValue("Bearer " + SecureDataManager.shared.getData(label: .accessToken), forHTTPHeaderField: Constants.Authorization)
            return request
        }
    }
     
    private func makePrameterForRequest(to request: URLRequest, with url: URL) throws -> URLRequest {
        var request = request
        switch parameter {
        case .query(let parameters):
            let params = parameters?.toDictionary() ?? [:]
            let queryParams = params.map { URLQueryItem(name: $0.key, value: "\($0.value)") }
            var components : URLComponents?
            if path == "" {
                components = URLComponents(string: url.absoluteString)
            } else {
                components = URLComponents(string: url.appendingPathComponent(path).absoluteString)
            }
            components?.queryItems = queryParams
            request.url = components?.url
        case .body(let parameters):
            let params = parameters?.toDictionary() ?? [:]
            request.httpBody = try JSONSerialization.data(withJSONObject: params, options: [])
        case .bodyFromDictionary(let parameter):
            request.httpBody = try JSONSerialization.data(withJSONObject: parameter, options: [])
        case .none:
            break
        }
        return request
    }
}


extension BaseRouter {
    var baseURL: String {
        return Constants.baseURL
    }
    
    var header: HeaderType {
        return HeaderType.withAuth
    }
    
    var multipart: MultipartFormData {
        return MultipartFormData()
    }
}


