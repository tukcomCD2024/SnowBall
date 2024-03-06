//
//  VoiceTableViewCell.swift
//  Memetory
//
//  Created by 이승진 on 2024/03/04.
//

import UIKit
import SnapKit

class VoiceTableViewCell: UITableViewCell {
    static let cellId = "VoiceTableViewCell"
    
    let playButton: UIButton = {
        let button = UIButton(type: .custom)
//        button.layer.cornerRadius = 15
//        button.layer.masksToBounds = true
//        button.layer.borderWidth = 1
        button.setImage(UIImage(systemName: "play.circle"), for: .normal)
        return button
    }()
    
    let nickNameLabel: UILabel = {
        let label = UILabel()
        label.text = "기본 목소리"
        label.font = UIFont.systemFont(ofSize: 18)
        label.textAlignment = .center
        return label
    }()
    
    let checkButton: UIButton = {
        let button = UIButton(type: .custom)
        button.layer.cornerRadius = 15
        button.layer.masksToBounds = true
        button.layer.borderWidth = 1
        button.layer.borderColor = #colorLiteral(red: 0.6666666865, green: 0.6666666865, blue: 0.6666666865, alpha: 1)
        button.isEnabled = true
        button.setImage(UIImage(named: "Icon-Check-off"), for: .normal)
        button.setImage(UIImage(named: "Icon-Check-on"), for: .selected)
        return button
    }()
    

    override init(style: UITableViewCell.CellStyle, reuseIdentifier reuseIndetifier: String?) {
        super.init(style: .default, reuseIdentifier: reuseIndetifier)
        
        self.addSubview(playButton)
        self.addSubview(nickNameLabel)
        self.addSubview(checkButton)
        setConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func setConstraints() {
        playButton.snp.makeConstraints { make in
            make.centerY.equalTo(self.snp.centerY)
            make.leading.equalTo(self.snp.leading).offset(15)
            make.width.height.equalTo(30)
        }
        
        nickNameLabel.snp.makeConstraints { make in
            make.centerY.equalTo(self.snp.centerY)
            make.leading.equalTo(playButton.snp.trailing).offset(10)
        }
        
        checkButton.snp.makeConstraints { make in
            make.centerY.equalTo(self.snp.centerY)
            make.trailing.equalTo(self.snp.trailing ).offset(-15)
            make.width.height.equalTo(30)
        }
    }
}
