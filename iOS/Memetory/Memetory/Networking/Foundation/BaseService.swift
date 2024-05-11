//
//  BaseService.swift
//  Memetory
//
//  Created by 이승진 on 2024/04/11.
//

import Foundation
import Alamofire

class BaseService {
    let AFManager: Session = {
            var session = AF
            let configuration = URLSessionConfiguration.af.default
            let eventLogger = APIEventLogger()
            session = Session(configuration: configuration, eventMonitors: [eventLogger])
            return session
        }()

    func requestReturnsData<T: Codable>(_ dataType : T.Type, router : BaseRouter, completion : @escaping (T) -> Void ){
        AFManager.request(router).responseDecodable(of: APIContainer<T>.self){ response in
            switch response.result {
            case .success(let container):
                completion(container.result)
            case .failure(let error ):
                print(error)
            }
        }
    }
    
    func multipartRequest<T: Codable>(_ dataType: T.Type, router: BaseRouter,completion:@escaping (T) -> Void){
        AFManager.upload(multipartFormData: router.multipart, with: router).responseDecodable(of: APIContainer<T>.self){ response in
            switch response.result {
            case .success(let container):
                completion(container.result)
            case .failure(let error ):
                print(error)
            }
        }
    }
    
    func requestReturnsNoData(router : BaseRouter, completion : @escaping (Any) -> Void){
        AFManager.request(router).responseData { response in
            if
                let data = response.data,
                let utf8Text = String(data: data, encoding: .utf8) {
                print("* RESPONSE DATA: \(utf8Text)") // encode data to UTF8
            }
            
        }
    }
    
    func authRequest(router : BaseRouter, completion : @escaping (AuthModelResponse)-> Void){
        AFManager.request(router).responseDecodable(of: AuthModelResponse.self) { response in
            switch response.result {
            case .success(let tokens):
                completion(tokens)
            case .failure(let error):
                print(error)
            }
        }
    }
    
    
}

final class MyRequestInterceptor: RequestInterceptor {
    func adapt(_ urlRequest: URLRequest, for session: Session, completion: @escaping (Result<URLRequest, Error>) -> Void) {
        var urlRequest = urlRequest
        
        //urlRequest.setValue("1", forHTTPHeaderField: "Authorization")
        completion(.success(urlRequest))
    }
}

