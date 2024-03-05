//
//  SettingViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/02/13.
//

import UIKit
import SnapKit

class SettingViewController: UIViewController {
    
//    weak var delegate: MemberDelegate?
    
    let sceneView: UIView = {
        let view = UIView()
        view.backgroundColor = .lightGray
        view.layer.cornerRadius = 10
        view.layer.masksToBounds = true
        view.layer.borderWidth = 1
        return view
    }()
    
    let faceSelectButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setTitle("얼굴 선택하기", for: .normal)
        button.setTitleColor(.black, for: .normal)
        button.titleLabel?.font = UIFont(name: "Pretendard-Bold", size: 18)
        button.layer.cornerRadius = 10
        button.layer.borderWidth = 1
        button.layer.masksToBounds = true
        button.backgroundColor = .white
        button.addTarget(self, action: #selector(faceSelectButtonTapped), for: .touchUpInside)
        return button
    }()
    
    let textSelectButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setTitle("대사 선택하기", for: .normal)
        button.setTitleColor(.black, for: .normal)
        button.titleLabel?.font = UIFont(name: "Pretendard-Bold", size: 18)
        button.layer.cornerRadius = 10
        button.layer.borderWidth = 1
        button.layer.masksToBounds = true
        button.backgroundColor = .white
        button.addTarget(self, action: #selector(textSelectButtonTapped), for: .touchUpInside)
        return button
    }()
    
    let voiceSelectButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setTitle("목소리 선택하기", for: .normal)
        button.setTitleColor(.black, for: .normal)
        button.titleLabel?.font = UIFont(name: "Pretendard-Bold", size: 18)
        button.layer.cornerRadius = 10
        button.layer.borderWidth = 1
        button.layer.masksToBounds = true
        button.backgroundColor = .white
        button.addTarget(self, action: #selector(voiceSelectButtonTapped), for: .touchUpInside)
        return button
    }()
    
    let selectButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setTitle("다음 장면", for: .normal)
        button.setTitleColor(.white, for: .normal)
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
        button.addTarget(self, action: #selector(selectButtonTapped), for: .touchUpInside)
        return button
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .white
        title = "설정하기"
        
        setViews()
        setConstraints()
    }
    
    func setViews() {
        view.addSubview(sceneView)
        view.addSubview(faceSelectButton)
        view.addSubview(textSelectButton)
        view.addSubview(voiceSelectButton)
        view.addSubview(selectButton)
    }
    
    func setConstraints() {
        sceneView.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide.snp.top).offset(10)
            make.leading.equalToSuperview().offset(20)
            make.trailing.equalToSuperview().offset(-20)
            make.height.equalTo(320)
        }
        
        faceSelectButton.snp.makeConstraints { make in
            make.top.equalTo(sceneView.snp.bottom).offset(10)
            make.leading.equalToSuperview().offset(20)
            make.trailing.equalToSuperview().offset(-20)
            make.height.equalTo(50)
        }
        
        textSelectButton.snp.makeConstraints { make in
            make.top.equalTo(faceSelectButton.snp.bottom).offset(10)
            make.leading.equalToSuperview().offset(20)
            make.trailing.equalToSuperview().offset(-20)
            make.height.equalTo(50)
        }
        
        voiceSelectButton.snp.makeConstraints { make in
            make.top.equalTo(textSelectButton.snp.bottom).offset(10)
            make.leading.equalToSuperview().offset(20)
            make.trailing.equalToSuperview().offset(-20)
            make.height.equalTo(50)
        }
        
        selectButton.snp.makeConstraints { make in
            make.top.equalTo(voiceSelectButton.snp.bottom).offset(10)
            make.leading.equalToSuperview().offset(20)
            make.trailing.equalToSuperview().offset(-20)
            make.height.equalTo(50)
        }
    }
    
    @objc func faceSelectButtonTapped() {
        let faceVC = FaceSelectViewController()
        navigationController?.pushViewController(faceVC, animated: true)
    }
    
    @objc func textSelectButtonTapped() {
        let textVC = SettingViewController()
        navigationController?.pushViewController(textVC, animated: true)
    }
    
    @objc func voiceSelectButtonTapped() {
        let voiceVC = SettingViewController()
        navigationController?.pushViewController(voiceVC, animated: true)
    }
    
    @objc func selectButtonTapped() {
        let temSelectVC = TemSelectViewController()
        navigationController?.pushViewController(temSelectVC, animated: true)
    }
}
