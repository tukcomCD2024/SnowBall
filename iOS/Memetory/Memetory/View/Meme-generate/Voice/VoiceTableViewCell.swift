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
    
    var isCheck: Bool = false {
        didSet {
            let imageName = isCheck ? "checkmark.square" : "checkmark.square.fill"
            checkButton.setImage(UIImage(systemName: imageName), for: .normal)
        }
    }
    
    let playButton: UIButton = {
        let button = UIButton(type: .custom)
//        button.layer.cornerRadius = 15
//        button.layer.masksToBounds = true
//        button.layer.borderWidth = 1
        button.setImage(UIImage(systemName: "play.circle"), for: .normal)
        button.tintColor = .black
        return button
    }()
    
    let nickNameLabel: UILabel = {
        let label = UILabel()
        label.text = "기본 목소리"
        label.font = UIFont.systemFont(ofSize: 18)
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
//    lazy var checkButton: UIButton = {
//        let button = UIButton()
//        button.layer.cornerRadius = 10
//        button.layer.masksToBounds = true
//        button.layer.borderWidth = 1
//        button.layer.borderColor = #colorLiteral(red: 0.6666666865, green: 0.6666666865, blue: 0.6666666865, alpha: 1)
//        button.backgroundColor = .white
////        button.isEnabled = true
//        button.isUserInteractionEnabled = false
//        button.setImage(UIImage(named: "Icon-Check-off"), for: .normal)
////        button.setImage(UIImage(named: "Icon-Check-on"), for: .selected)
//        return button
//    }()
    
    let checkButton: UIButton = {
        let button = UIButton()
//        button.layer.cornerRadius = 10
//        button.layer.masksToBounds = true
//        button.layer.borderWidth = 1
//        button.layer.borderColor = UIColor.lightGray.cgColor
//        button.setImage(UIImage(named: "Icon-Check-off"), for: .normal)
//        button.setImage(UIImage(named: "Icon-Check-On"), for: .selected)
        
        button.setImage(UIImage(systemName: "checkmark.square"), for: .normal)
        button.tintColor = .black
        button.isUserInteractionEnabled = false
                
                
        return button
    }()
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        isCheck.toggle()
    }

    override init(style: UITableViewCell.CellStyle, reuseIdentifier reuseIndetifier: String?) {
        super.init(style: .default, reuseIdentifier: reuseIndetifier)
        self.backgroundColor = .white
        self.addSubview(playButton)
        self.addSubview(nickNameLabel)
        self.addSubview(checkButton)
        setConstraints()
        
        // playButton 또는 nickNameLabel을 탭하여도 셀이 선택되도록 탭 제스처를 추가합니다.
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(cellTapped))
        playButton.addGestureRecognizer(tapGesture)
        nickNameLabel.addGestureRecognizer(tapGesture)
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
            make.width.height.equalTo(20)
        }
    }
    // 셀이 탭되었을 때 isSelected 속성을 토글합니다.
    @objc func cellTapped() {
        isSelected.toggle()
    }
    
}
