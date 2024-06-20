//
//  HomeTableViewCell.swift
//  Memetory
//
//  Created by 이승진 on 2024/06/20.
//

import UIKit
import SnapKit

class HomeTableViewCell: UITableViewCell {
    static let cellId = "HomeTableViewCell"
    
    let mainView: UIView = {
        let view = UIView()
        return view
    }()
    
    let mainLabel: UILabel = {
        let label = UILabel()
        label.text = "이 달의 인기차트"
        label.textColor = .black
        label.font = .boldSystemFont(ofSize: 20)
        return label
    }()
    
    let mainImageView: UIImageView = {
        let image = UIImageView()
        image.image = UIImage(named: "demo01")
        image.contentMode = .scaleAspectFill
        image.layer.masksToBounds = true
        image.layer.cornerRadius = 15
        return image
    }()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier reuseIndetifier: String?) {
        super.init(style: .default, reuseIdentifier: reuseIndetifier)
        setup()
        setConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setup() {
        self.addSubview(mainView)
        self.addSubview(mainLabel)
        self.addSubview(mainImageView)
    }
    
    private func setConstraints() {
        mainView.snp.makeConstraints { make in
            make.edges.equalTo(contentView).offset(15)
        }
        
        mainLabel.snp.makeConstraints { make in
            make.top.equalTo(mainView.snp.top).offset(15)
            make.leading.equalTo(contentView).offset(15)
        }
        
        mainImageView.snp.makeConstraints { make in
            make.top.equalTo(mainLabel.snp.bottom).offset(15)
            make.leading.equalToSuperview().offset(15)
            make.trailing.equalToSuperview().offset(-15)
        }
        
    }
}
