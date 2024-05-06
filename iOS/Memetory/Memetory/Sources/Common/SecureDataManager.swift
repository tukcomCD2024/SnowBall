//
//  SecureDataManager.swift
//  Memetory
//
//  Created by 이승진 on 2024/04/15.
//
import Foundation
import Security

enum TokenType : String{
    case accessToken
    case refreshToken
}

enum KeychainError: Error {
    case noPassword
    case unexpectedPasswordData
    case unhandledError(status: OSStatus)
}

class SecureDataManager {
    static let shared = SecureDataManager()
    
    let serviceName = "Memetory"
    private init(){}
    let appName = "Memetory"
    
    func setData(_ token: String, label : TokenType)-> Bool{
        let keychainItem = [
                    kSecClass: kSecClassGenericPassword,
                    kSecAttrAccount: label.rawValue,
                    kSecAttrService: serviceName,
                    kSecValueData: token.data(using: .utf8, allowLossyConversion: false)!
                ] as NSDictionary
        
        //Test
        SecItemDelete(keychainItem)
        
        let status = SecItemAdd(keychainItem as CFDictionary, nil)
    
        if status == errSecSuccess {
            print("SecureDataManager: 저장성공")
            return true
        } else if status == errSecDuplicateItem {
            print("SecureDataManager: keychain에 Item이 이미 있음")
            return false
        } else {
            print("SecureDataManager: 저장 실패 \(status)")
            return false
        }
    }
    
    func getData(label : TokenType) -> String {
        let searchQuery: NSDictionary = [kSecClass: kSecClassGenericPassword,
                                     kSecAttrAccount: label.rawValue,
                                  kSecAttrService : serviceName,
                                    kSecReturnData: kCFBooleanTrue!]
        var result: AnyObject?
        let searchStatus = SecItemCopyMatching(searchQuery, &result)
        
        if searchStatus == errSecSuccess {
            guard let token = result as? Data else { return "Error"}
            print("SecureDataManager: 불러오기 성공")
            return String(data: token, encoding: String.Encoding.utf8)!
        } else {
            return "SecureDataManager: 불러오기 실패, status = \(searchStatus)"
        }
    }
}
