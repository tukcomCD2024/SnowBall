//
//  TemplateTableViewCell.swift
//  Memetory
//
//  Created by 이승진 on 2024/02/13.
//

import UIKit
import SnapKit

final class TemplateTableViewCell: UITableViewCell {
    
    static let cellId = "TemplateTableViewCell"
    
    let mainImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = UIImage(named: "demo1")
        imageView.contentMode = .scaleAspectFill
        imageView.layer.cornerRadius = 10
        imageView.layer.masksToBounds = true
        return imageView
    }()
    
//    let titleLabel: UILabel = {
//        let label = UILabel()
//        label.text = "해리포터 템플릿"
//        label.textColor = .black
//        label.font = UIFont(name: "Pretendard-Bold", size: 25)
//        return label
//    }()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier reuseIndetifier: String?) {
        super.init(style: .default, reuseIdentifier: reuseIndetifier)
        
        self.addSubview(mainImageView)
//        self.addSubview(titleLabel)
        setConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func setConstraints() {
        mainImageView.snp.makeConstraints { make in
            make.height.width.equalTo(270)
            make.top.equalTo(self.snp.top).offset(20)
            make.centerY.equalTo(self.snp.centerY)
            make.centerX.equalTo(self.snp.centerX)
            
        }
        
//        titleLabel.snp.makeConstraints { make in
//            make.top.equalTo(self.snp.top).offset(20)
//            make.centerY.equalTo(self.snp.centerY)
//            make.leading.equalTo(mainImageView.snp.trailing).offset(15)
//        }
    }
}
