//
//  Voice.swift
//  Memetory
//
//  Created by 이승진 on 2024/05/14.
//

import Foundation

struct Voice : Codable {
    var s3Key: String
    var name: String
    var description: String
    
    static let voiceDemo = ["Martin Li", "Max Mustermann - Ernst", "Thomas", "Grant - Calm Narration", "Devi - Clear Hindi pronunciation"]
}

