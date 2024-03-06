//
//  TemplateCollectionViewCell.swift
//  Memetory
//
//  Created by 이승진 on 2024/03/04.
//

import UIKit
import SnapKit

class TemplateCollectionViewCell: UICollectionViewCell {
    static let cellId = "TemplateCollectionViewCell"
    
    var sceneImageView: UIImageView = {
//        let img = UIImageView(image: UIImage(named: "LogModel1"))
        let img = UIImageView(image: UIImage(named: "Meme_Logo"))
        img.layer.cornerRadius = 10
        img.layer.borderWidth = 1
        img.layer.masksToBounds = true
        img.contentMode = .scaleAspectFill
        return img
    }()
    
    override init(frame : CGRect){
        super.init(frame: frame)
        setViews()
        setConstraints()
    }
    
    private func setViews(){
        self.clipsToBounds = true
        
        self.addSubview(sceneImageView)
    }
    
    private func setConstraints() {
        sceneImageView.snp.makeConstraints { make in
            make.width.height.equalToSuperview()
        }
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
