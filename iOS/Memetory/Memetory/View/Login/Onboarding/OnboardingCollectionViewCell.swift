//
//  OnboardingCollectionViewCell.swift
//  Memetory
//
//  Created by 이승진 on 2024/02/08.
//

import UIKit
import SnapKit

class OnboardingCollectionViewCell: UICollectionViewCell {
    static let identifier = "OnboardingCollectionViewCell"
    
//    private lazy var animationView = AnimationView()
    private var imageName: String = ""
    
    let mainView: UIView = {
        let view = UIView()

        return view
    }()
    
    let mainLabel: UILabel = {
        let label = UILabel()
//        label.font = UIFont(name: "BMJUA_ttf", size: 25)
        label.textColor = .black
        label.textAlignment = .center
        label.numberOfLines = 0
        label.font = .boldSystemFont(ofSize: 25)
        
        return label
    }()
    
    let mainImageView: UIImageView = {
        let image = UIImageView()
        image.contentMode = .scaleToFill
        return image
    }()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.setCell()
    }
    
    required init?(coder: NSCoder) {
        fatalError()
    }
    
    func setCell() {
        self.backgroundColor = .white
        self.addSubview(mainView)
        self.addSubview(mainLabel)
        self.addSubview(mainImageView)
        
        mainView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
        
        mainLabel.snp.makeConstraints { make in
            make.top.equalTo(mainView.snp.top).offset(10)
            make.leading.equalTo(mainView.snp.leading).offset(15)
            make.trailing.equalTo(mainView.snp.trailing).offset(-15)
        }
        
        mainImageView.snp.makeConstraints { make in
            make.top.equalTo(mainLabel.snp.bottom).offset(10)
            make.leading.equalTo(mainView.snp.leading).offset(15)
            make.trailing.equalTo(mainView.snp.trailing).offset(-15)
            make.bottom.equalTo(mainView.snp.bottom)
        }
    }
    
    override func prepareForReuse() {
        // 해당 처리를 해준 이유가 계속 로티 이미지가 겹쳐서 생성되는 문제가 있었기 때문
        imageName = ""
//        animationView.stop()
    }

    func setOnboardingSlides(_ slides: OnboardingDataModel) {
//        setAnimationView(slides.imageName)
        mainLabel.text = slides.title
        mainImageView.image = UIImage(named: slides.imageName)
    }
    
//    private func setAnimationView(_ imageName: String) {
//        animationView = AnimationView()
//        animationView.frame = onboardingView.bounds
//        animationView.animation = Animation.named(lottieName)
//        animationView.contentMode = .scaleAspectFit
//        animationView.loopMode = .loop
//        animationView.play()
//        onboardingView.addSubview(animationView)
//    }
}
