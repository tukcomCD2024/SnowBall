//
//  VoiceSelectViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/03/04.
//

import UIKit
import SnapKit
import AWSS3
import AVFAudio

class VoiceSelectViewController: UIViewController {
    
    let S3BucketName = "memetory"
    let voiceUploadManager = VoiceManager()
    private let voiceDemo = Voice.voiceDemo
    
    
//    private let tableView =  UITableView()
    lazy var tableView = UITableView()
    
    let uploadButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setTitle("나의 업로드", for: .normal)
        button.setTitleColor(.white, for: .normal)
        button.titleLabel?.font = UIFont(name: "Pretendard-Bold", size: 18)
        button.layer.cornerRadius = 10
        button.layer.masksToBounds = true
        button.layer.borderWidth = 1
        button.layer.borderColor = UIColor.black.cgColor
        button.layer.backgroundColor = UIColor.black.cgColor
        button.addTarget(self, action: #selector(uploadButtonTapped), for: .touchUpInside)
        return button
    }()
    
    let checkButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setTitle("선택하기", for: .normal)
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
        button.addTarget(self, action: #selector(checkButtonTapped), for: .touchUpInside)
        return button
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        title = "목소리 선택하기"
        view.backgroundColor = .white
        
        setupTableView()
        setViews()
        setConstraints()
    }
    
    func setViews() {
        view.addSubview(tableView)
        view.addSubview(uploadButton)
        view.addSubview(checkButton)
    }
    
    func setConstraints() {
        tableView.snp.makeConstraints { make in
            make.top.equalToSuperview()
            make.leading.equalToSuperview().offset(10)
            make.trailing.equalToSuperview().offset(-10)
            make.bottom.equalTo(checkButton.snp.top).offset(20)
            
        }
        
        uploadButton.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.width.equalTo(320)
            make.height.equalTo(45)
            make.bottom.equalTo(checkButton.snp.top).offset(-30)
        }
        
        checkButton.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.width.equalTo(320)
            make.height.equalTo(45)
            make.bottom.equalToSuperview().offset(-100)
        }
    }
    
    func setupTableView() {
        tableView.dataSource = self
        tableView.delegate = self
        tableView.rowHeight = 60
        tableView.register(VoiceTableViewCell.self, forCellReuseIdentifier: "VoiceSelectCell")
    }
    
    
    @objc func uploadButtonTapped() {
        let audioPickerController = UIImagePickerController()
        audioPickerController.delegate = self
        audioPickerController.sourceType = .savedPhotosAlbum
        audioPickerController.mediaTypes = ["public.audio"]
        present(audioPickerController, animated: true, completion: nil)
    }
    
    @objc func checkButtonTapped() {
        let loadingVC = LoadingViewController()
        navigationController?.pushViewController(loadingVC, animated: true)
        
//        let SelectVC = SelectViewController()
//        navigationController?.pushViewController(SelectVC, animated: true)
    }
}

extension VoiceSelectViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return voiceDemo.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "VoiceSelectCell", for: indexPath) as! VoiceTableViewCell
        cell.selectionStyle = .none
        cell.nickNameLabel.text = voiceDemo[indexPath.item]
        return cell
    }
}

extension VoiceSelectViewController: UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        picker.dismiss(animated: true, completion: nil)
        
        guard let audioURL = info[UIImagePickerController.InfoKey.mediaURL] as? URL else {
            print("Error: No audio selected")
            return
        }
        
        uploadVoice(audioURL)
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    
    func uploadVoice(_ audioURL: URL) {
        voiceUploadManager.uploadVoiceToS3(audioURL) { (voiceURLString) in
            if let voiceURLString = voiceURLString {
                print("Uploaded voice URL: \(voiceURLString)")
                // 업로드 성공 시 여기에서 추가 작업을 수행할 수 있습니다.
            } else {
                print("Failed to upload voice.")
                // 업로드 실패 시 여기에서 처리할 수 있는 로직을 추가하세요.
            }
        }
    }
}
