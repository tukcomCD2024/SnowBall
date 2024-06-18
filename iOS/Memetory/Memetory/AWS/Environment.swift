//
//  Environment.swift
//  Memetory
//
//  Created by 이승진 on 2024/06/17.
//

import Foundation

struct Environment {
    static func load() {
        guard let envPath = Bundle.main.path(forResource: ".env", ofType: nil) else {
            print(".env file not found")
            return
        }
        
        // 경로 출력
        print("Env file path: \(envPath)")
        
        do {
            let envContents = try String(contentsOfFile: envPath)
            print("Env file contents:\n\(envContents)")
            let envLines = envContents.split(separator: "\n")
            
            for line in envLines {
                let keyValue = line.split(separator: "=")
                if keyValue.count == 2 {
                    let key = String(keyValue[0]).trimmingCharacters(in: .whitespaces)
                    let value = String(keyValue[1]).trimmingCharacters(in: .whitespaces)
                    setenv(key, value, 1)
                }
            }
        } catch {
            print("Failed to read .env file: \(error)")
        }
    }
}
