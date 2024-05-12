//
//  MainTemplateCollectionViewCell.swift
//  Memetory
//
//  Created by 이승진 on 2024/05/12.
//

import UIKit

class MainTemplateCollectionViewCell: UICollectionViewCell {
    static let cellId = "MainTemplateCollectionViewCell"
    
    let mainImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = UIImage(named: "demo1")
        imageView.contentMode = .scaleAspectFill
        imageView.layer.cornerRadius = 15
        imageView.layer.masksToBounds = true
        return imageView
    }()
    
    let titleLabel = {
        let label = UILabel()
        label.textColor = .white
        label.font = UIFont(name: "Pretendard-Bold", size: 15)
        return label
    }()
    
    
    override init(frame : CGRect){
        super.init(frame: frame)
        
        self.backgroundColor = .black
        setUp()
        setConstraints()
    }
    
//    public func bind(log: Log, isBigCell : Bool){
//        self.title.text = log.title
//        self.date.text = log.getTravelPeriod()
//        dDay.setTitle(dateController.days(from:log.startDate), for: .normal)
//
//        self.backImage.kf.setImage(with: URL(string: log.imageUrl))
//        self.backImage.alpha = 0.5
//
//        setConst(isBigCell)
//        setSize(isBigCell)
//    }
//
//    private func setSize(_ isBigCell : Bool){
//        title.font = WithYouFontFamily.Pretendard.bold.font(size: isBigCell ? 22 : 16)
//        date.font = WithYouFontFamily.Pretendard.medium.font(size: isBigCell ? 15 : 12)
//        //Only for big cell
//        dDay.titleLabel?.font = WithYouFontFamily.Pretendard.bold.font(size: 15)
//    }
    
    private func setUp(){
        self.clipsToBounds = true
//        self.addSubview(mainImageView)
        self.addSubview(titleLabel)
    }
    
    func setConstraints() {
//        mainImageView.snp.makeConstraints { make in
//            make.height.width.equalTo(270)
//            make.top.equalTo(self.snp.top).offset(20)
//            make.centerY.equalTo(self.snp.centerY)
//            make.centerX.equalTo(self.snp.centerX)
//
//        }
        titleLabel.snp.makeConstraints { make in
            make.centerX.equalTo(self.snp.centerX)
            make.centerY.equalTo(self.snp.centerY)
        }
    }
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
