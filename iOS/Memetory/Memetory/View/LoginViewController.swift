//
//  LoginViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/01/30.
//

import UIKit
import SnapKit

class LoginViewController: UIViewController {
    
    let logoImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = UIImage(named: "Meme_Logo")
        imageView.contentMode = .scaleAspectFit
        return imageView
    }()
    
    let kakaoLoginButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setImage(UIImage(named: "KakaoLogin"), for: .normal)
        button.addTarget(self, action: #selector(loginButtonTapped), for: .touchUpInside)
        return button
    }()
    
    
    let googleLoginButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setImage(UIImage(named: "GoogleLogin"), for: .normal)
        button.addTarget(self, action: #selector(loginButtonTapped), for: .touchUpInside)
        return button
    }()
    
    let appleLoginButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setImage(UIImage(named: "AppleLogin"), for: .normal)
        button.addTarget(self, action: #selector(loginButtonTapped), for: .touchUpInside)
        return button
    }()
    

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .white
        setViews()
        setConstraints()
    }
    

    private func setViews() {
        view.addSubview(logoImageView)
        view.addSubview(kakaoLoginButton)
        view.addSubview(googleLoginButton)
        view.addSubview(appleLoginButton)
        
        
    }
    
    private func setConstraints() {
        logoImageView.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.centerY.equalToSuperview().offset(-60)
        }
        
        kakaoLoginButton.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.top.equalTo(logoImageView.snp.bottom).offset(80)
            make.width.equalTo(360)
            make.height.equalTo(60)
        }
        
        googleLoginButton.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.top.equalTo(kakaoLoginButton.snp.bottom).offset(10)
            make.width.equalTo(360)
            make.height.equalTo(60)
        }
        
        appleLoginButton.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.top.equalTo(googleLoginButton.snp.bottom).offset(10)
            make.width.equalTo(360)
            make.height.equalTo(60)
        }
    }
    
    @objc private func loginButtonTapped() {
        let nickNameSetViewController = NickNameSetViewController()
        navigationController?.pushViewController(nickNameSetViewController, animated: true)
    }

}
