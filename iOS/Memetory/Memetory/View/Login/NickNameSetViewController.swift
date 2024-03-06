//
//  NickNameSetViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/01/30.
//

import UIKit
import SnapKit

class NickNameSetViewController: UIViewController {
    
    let mainLabel: UILabel = {
        let label = UILabel()
        label.text = "자신만의 닉네임을\n입력해주세요!"
        label.numberOfLines = 0
        label.font = .boldSystemFont(ofSize: 30)
        label.textAlignment = .center
        label.font = UIFont(name: "Pretendard-Bold", size: 30)
        label.textColor = .black
        return label
    }()
    
    let subLabel: UILabel = {
        let label = UILabel()
        label.text = "닉네임"
        label.numberOfLines = 0
        label.font = UIFont(name: "Pretendard-Bold", size: 18)
        label.textColor = .black
        return label
    }()
    
    let nickNameTextField: UITextField = {
        let tf = UITextField()
        tf.textAlignment = .left
        tf.borderStyle = .none
        return tf
    }()
    
    let underlineView: UIView = {
        let view = UIView()
        view.backgroundColor = .lightGray
        return view
    }()
    
    let noticeLabel: UILabel = {
        let label = UILabel()
        label.text = "한글, 영어, 숫자로 구성할 수 있어요."
        label.font = .systemFont(ofSize: 12)
//        label.font = UIFont(name: "BMJUA_otf", size: 12)
        label.textColor = .lightGray
        return label
    }()
    
    let checkButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setTitle("확인", for: .normal)
        button.setTitleColor(UIColor.white, for: .normal)
        button.titleLabel?.font = UIFont(name: "Pretendard-Bold", size: 18)
        button.layer.cornerRadius = 10
        button.layer.masksToBounds = true
        button.layer.borderWidth = 1
        button.layer.borderColor = CGColor(
            red: 221.0 / 255.0,
            green: 136.0 / 255.0,
            blue: 88.0 / 255.0,
            alpha: 1.0
        )
        button.backgroundColor = UIColor(
            red: 221.0 / 255.0,
            green: 136.0 / 255.0,
            blue: 88.0 / 255.0,
            alpha: 1.0
        )
        button.addTarget(self, action: #selector(checkButtonTapped), for: .touchUpInside)
        return button
    }()

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .white
        navigationItem.hidesBackButton = true
        
        setViews()
        setConstraints()
    }
    
    private func setViews() {
        view.addSubview(mainLabel)
        view.addSubview(nickNameTextField)
        view.addSubview(underlineView)
        view.addSubview(subLabel)
        view.addSubview(noticeLabel)
        view.addSubview(checkButton)
    }
    
    private func setConstraints() {
        mainLabel.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.centerY.equalToSuperview().offset(-120)
        }
        
        nickNameTextField.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.centerY.equalToSuperview()
            make.leading.equalToSuperview().offset(30)
            make.trailing.equalToSuperview().offset(-30)
        }
        
        underlineView.snp.makeConstraints { make in
            make.top.equalTo(nickNameTextField.snp.bottom).offset(10)
            make.leading.trailing.equalTo(nickNameTextField)
            make.height.equalTo(1)
        }
        
        subLabel.snp.makeConstraints { make in
            make.leading.equalToSuperview().offset(30)
            make.bottom.equalTo(nickNameTextField.snp.top).offset(-10)
        }
        
        noticeLabel.snp.makeConstraints { make in
            make.leading.equalToSuperview().offset(30)
            make.top.equalTo(underlineView.snp.bottom).offset(10)
        }
        
        checkButton.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.top.equalTo(noticeLabel.snp.bottom).offset(80)
            make.width.equalTo(320)
            make.height.equalTo(45)
        }
    }
    
    @objc func checkButtonTapped() {
        let onboardingViewController = OnboardingViewController()
        navigationController?.pushViewController(onboardingViewController, animated: true)
    }

}
