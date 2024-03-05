//
//  VoiceSelectViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/03/04.
//

import UIKit
import SnapKit

class VoiceSelectViewController: UIViewController {
    
    private let tableView =  UITableView()
    
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
        view.addSubview(checkButton)
    }
    
    func setConstraints() {
        tableView.snp.makeConstraints { make in
            make.top.equalToSuperview()
            make.leading.equalToSuperview().offset(10)
            make.trailing.equalToSuperview().offset(-10)
            make.bottom.equalTo(checkButton.snp.top).offset(20)
            
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
    
    @objc func checkButtonTapped() {
        
    }
}

extension VoiceSelectViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 5
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "VoiceSelectCell", for: indexPath) as! VoiceTableViewCell
        cell.selectionStyle = .none
        return cell
    }
}
