//
//  StorageCollectionViewCell.swift
//  Memetory
//
//  Created by 이승진 on 2024/05/16.
//

import UIKit
import SnapKit

class StorageCollectionViewCell: UICollectionViewCell {
    static let cellId = "StorageCollectionViewCell"

    let titleLabel = {
        let label = UILabel()
        label.textColor = .white
        label.font = UIFont(name: "Pretendard-Bold", size: 15)
        return label
    }()
    
    let playButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setImage(UIImage(systemName: "play.circle"), for: .normal)
        button.tintColor = .white
        return button
    }()
    
    let timeLabel: UILabel = {
        let label = UILabel()
        label.textColor = .white
        label.font = UIFont(name: "Pretendard-Bold", size: 12)
        return label
    }()
    
    
    override init(frame : CGRect){
        super.init(frame: frame)
        
        self.backgroundColor = .black
        setUp()
        setConstraints()
    }
    
    private func setUp(){
        self.clipsToBounds = true
//        self.addSubview(mainImageView)
        self.addSubview(titleLabel)
        self.addSubview(playButton)
        self.addSubview(timeLabel)
    }
    
    func setConstraints() {
        titleLabel.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(10)
            make.centerX.equalTo(self.snp.centerX)
        }
        
        playButton.snp.makeConstraints { make in
            make.centerX.equalTo(self.snp.centerX)
            make.centerY.equalTo(self.snp.centerY)
            make.width.height.equalTo(200)
        }
        
        timeLabel.snp.makeConstraints { make in
            make.trailing.equalToSuperview().offset(-15)
            make.bottom.equalToSuperview().offset(-15)
        }
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
