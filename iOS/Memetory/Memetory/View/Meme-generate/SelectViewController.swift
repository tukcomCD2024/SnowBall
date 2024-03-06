//
//  SelectViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/02/25.
//

import UIKit
import SnapKit

class SelectViewController: UIViewController {

    let faceImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.layer.borderWidth = 1.0
        imageView.layer.cornerRadius = 10 // 원하는 라운드 값으로 수정
        return imageView
    }()
    
    let selectImageButton: UIButton = {
        let button = UIButton()
        
        if let originalImage = UIImage(systemName: "plus.circle.fill") {
            // 원하는 색상으로 이미지 채색
            let tintedColor = UIColor(named: "SubColor") ?? .gray
            let tintedImage = originalImage.withTintColor(tintedColor, renderingMode: .alwaysOriginal)
            
            // 크기 조절
            let newSize = CGSize(width: 50, height: 50)
            let resizedImage = UIGraphicsImageRenderer(size: newSize).image { _ in
                tintedImage.draw(in: CGRect(origin: .zero, size: newSize))
            }
            
            button.setImage(resizedImage, for: .normal)
            button.addTarget(self, action: #selector(selectImage), for: .touchUpInside)
        }
        button.backgroundColor = .clear
        return button
    }()
    
    let cancelImageButton: UIButton = {
        let button = UIButton()
        button.isHidden = true
        let image = UIImage(systemName: "xmark.circle.fill")?.withRenderingMode(.alwaysOriginal)
        button.setImage(image, for: .normal)
        button.addTarget(self, action: #selector(cancelImage), for: .touchUpInside)
        button.imageView?.contentMode = .scaleAspectFit
        return button
    }()
    
    let adviceLabel: UILabel = {
        let label = UILabel()
        label.text = "한 사람의 얼굴이 정 가운데에 위치한 사진을 선택해주세요!"
        label.font = UIFont(name: "Pretendard-Bold", size: 12)
        label.textColor = .lightGray
        return label
    }()
    
    let lineTextView: UITextView = {
        let tv = UITextView()
        tv.autocapitalizationType = .none
        tv.layer.cornerRadius = 12
        tv.font = UIFont(name: "Pretendard-Bold", size: 16)
        return tv
    }()
    
//    let lineTextField: UITextField = {
//        let tf = UITextField()
//        tf.borderStyle = .roundedRect
//        tf.font = UIFont(name: "Pretendard-Bold", size: 16)
//        tf.placeholder = "대사를 입력해주세요!"
//        return tf
//    }()
    
//    let characterCountLabel: UILabel = {
//        let label = UILabel()
//        label.text = "0/50"
//        label.font = UIFont(name: "Pretendard-Bold", size: 12)
//        label.textAlignment = .right
//        return label
//    }()
    
    let voiceSelectButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setTitle("목소리 선택하기", for: .normal)
        button.setTitleColor(.black, for: .normal)
        button.titleLabel?.font = UIFont(name: "Pretendard-Bold", size: 18)
        button.layer.cornerRadius = 10
        button.layer.masksToBounds = true
        button.layer.borderWidth = 1
        button.addTarget(self, action: #selector(voiceSelectButtonTapped), for: .touchUpInside)
        return button
    }()
    
    let checkButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setTitle("확인", for: .normal)
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
//        lineTextField.delegate = self
        lineTextView.delegate = self
        tabBarController?.tabBar.isHidden = true
        
        title = "선택하기"
        setViews()
        setConstraints()
    }
    
    func setViews() {
        view.addSubview(faceImageView)
        view.addSubview(selectImageButton)
        view.addSubview(cancelImageButton)
        view.addSubview(adviceLabel)
        view.addSubview(lineTextView)
//        view.addSubview(lineTextField)
//        view.addSubview(characterCountLabel)
        view.addSubview(voiceSelectButton)
        view.addSubview(checkButton)
    }
    
    func setConstraints() {
        faceImageView.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide.snp.top).offset(10)
            make.leading.equalToSuperview().offset(30)
            make.trailing.equalToSuperview().offset(-30)
            make.height.equalTo(320)
        }
        
        selectImageButton.snp.makeConstraints { make in
            make.center.equalTo(faceImageView)
        }
        
        cancelImageButton.snp.makeConstraints { make in
            make.centerX.equalTo(faceImageView.snp.right)
            make.centerY.equalTo(faceImageView.snp.top)
        }
        
        adviceLabel.snp.makeConstraints { make in
            make.top.equalTo(faceImageView.snp.bottom).offset(5)
            make.left.equalTo(faceImageView)
        }
        
        lineTextView.snp.makeConstraints { make in
            make.top.equalTo(faceImageView.snp.bottom).offset(50)
            make.centerX.equalToSuperview()
            make.leading.equalToSuperview().offset(30)
            make.trailing.equalToSuperview().offset(-30)
            make.height.equalTo(100)
        }
        
//        lineTextField.snp.makeConstraints { make in
//            make.top.equalTo(faceImageView.snp.bottom).offset(50)
//            make.centerX.equalToSuperview()
//            make.leading.equalToSuperview().offset(30)
//            make.trailing.equalToSuperview().offset(-30)
//            make.height.equalTo(45)
//        }
        
//        characterCountLabel.snp.makeConstraints { make in
//            make.top.equalTo(lineTextView.snp.bottom).offset(5)
//            make.right.equalTo(lineTextView)
//        }
        
        voiceSelectButton.snp.makeConstraints { make in
            make.top.equalTo(lineTextView.snp.bottom).offset(30)
            make.leading.equalToSuperview().offset(30)
            make.trailing.equalToSuperview().offset(-30)
            make.height.equalTo(50)
        }
        
        checkButton.snp.makeConstraints { make in
//            make.top.equalTo(faceImageView.snp.bottom).offset(80)
            make.leading.equalToSuperview().offset(30)
            make.trailing.equalToSuperview().offset(-30)
            make.top.equalTo(voiceSelectButton.snp.bottom).offset(30)
            make.height.equalTo(50)
        }
    }
    
    @objc func voiceSelectButtonTapped() {
        let voiceSelectVC = VoiceSelectViewController()
        navigationController?.pushViewController(voiceSelectVC, animated: true)
    }
    
    @objc func selectButtonTapped() {
        let temSelectVC = TemSelectViewController()
        navigationController?.pushViewController(temSelectVC, animated: true)
    }
}

extension SelectViewController : UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    // MARK: - UIImagePickerDelegate
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        if let selectedImage = info[.originalImage] as? UIImage {
            faceImageView.image = selectedImage
            selectImageButton.isHidden = true
            cancelImageButton.isHidden = false
        }
        picker.dismiss(animated: true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    
    // 이미지 선택 버튼의 액션 메서드
    @objc func selectImage() {
        let imagePicker = UIImagePickerController()
        imagePicker.delegate = self
        imagePicker.sourceType = .photoLibrary
        present(imagePicker, animated: true, completion: nil)
    }
    
    // 이미지 취소 버튼의 액션 메서드
    @objc func cancelImage() {
        faceImageView.image = UIImage()
        cancelImageButton.isHidden = true
        selectImageButton.isHidden = false
    }
}
extension SelectViewController: UITextViewDelegate {
    //화면 터치시 키보드 내림
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
//    func textView(_ textView: UITextView, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
//        let newText = (textView.text as NSString?)?.replacingCharacters(in: range, with: string) ?? ""
//        let characterCount = newText.count
//        
//        if characterCount <= 50 {
//            characterCountLabel.text = "\(characterCount)/50"
//            return true
//        } else {
//            return false
//        }
//    }
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        // 텍스트 필드가 편집을 시작할 때 호출되는 메서드
        textView.layer.cornerRadius = 8.0 // 둥근 테두리 반지름 설정
        textView.layer.borderWidth = 1.0 // 테두리 두께 설정
//        textView.text = nil
//        textField.layer.borderColor = WithYouAsset.mainColorDark.color.cgColor
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
//        textField.layer.borderColor = WithYouAsset.subColor.color.cgColor
        if (lineTextView.text == "") {
            lineTextView.text = "대사를 입력해주세요!"
            lineTextView.textColor = .gray
        }
    }
    
    func textViewShouldReturn(_ textView: UITextView) -> Bool {
        // Process of closing the Keyboard when the line feed button is pressed.
        textView.resignFirstResponder()
        return true
    }
}

//extension SelectViewController : UITextFieldDelegate {
    // MARK: - UITextFieldDelegate
    
    //화면 터치시 키보드 내림
//    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
//        self.view.endEditing(true)
//    }
    
//    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
//        let newText = (textField.text as NSString?)?.replacingCharacters(in: range, with: string) ?? ""
//        let characterCount = newText.count
//        
//        if characterCount <= 50 {
//            characterCountLabel.text = "\(characterCount)/50"
//            return true
//        } else {
//            return false
//        }
//    }
    
//    func textFieldDidBeginEditing(_ textField: UITextField) {
//        // 텍스트 필드가 편집을 시작할 때 호출되는 메서드
//        textField.layer.cornerRadius = 8.0 // 둥근 테두리 반지름 설정
//        textField.layer.borderWidth = 1.0 // 테두리 두께 설정
////        textField.layer.borderColor = WithYouAsset.mainColorDark.color.cgColor
//    }
//    
//    func textFieldDidEndEditing(_ textField: UITextField) {
////        textField.layer.borderColor = WithYouAsset.subColor.color.cgColor
//    }
//    
//    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
//        // Process of closing the Keyboard when the line feed button is pressed.
//        textField.resignFirstResponder()
//        return true
//    }
//}
