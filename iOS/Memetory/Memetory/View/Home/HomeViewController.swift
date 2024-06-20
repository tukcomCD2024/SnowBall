//
//  HomeViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/02/13.
//

import UIKit
import SnapKit

class HomeViewController: UIViewController {
    
    private let mainLabel = Scene.mainLabel
    private let mainImage = Scene.sceneImage
    
    let tableView: UITableView = {
        let tableView = UITableView()
        tableView.register(HomeTableViewCell.self, forCellReuseIdentifier: HomeTableViewCell.cellId)
        return tableView
    }()
    
    let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "메인 화면"
        label.textColor = .black
        label.font = UIFont(name: "Pretendard-Bold", size: 25)
        return label
    }()
    
    let chartLabel: UILabel = {
        let label = UILabel()
        label.text = "영상 인기 차트"
        label.textColor = .black
        label.font = .boldSystemFont(ofSize: 20)
        return label
    }()
    
    let firstLabel: UILabel = {
        let label = UILabel()
        label.text = "1. 말왕 매그네릭"
        label.textColor = .black
        label.font = .boldSystemFont(ofSize: 20)
        return label
    }()
    
    let secondLabel: UILabel = {
        let label = UILabel()
        label.text = "2. 악마를 보았다 - 너 좋아하면 안되냐?"
        label.textColor = .black
        label.font = .boldSystemFont(ofSize: 20)
        return label
    }()
    
    let thirdLabel: UILabel = {
        let label = UILabel()
        label.text = "3. 신세계 - 거 장난이 심한거 아니오?"
        label.textColor = .black
        label.font = .boldSystemFont(ofSize: 20)
        return label
    }()
    
    let fourthLabel: UILabel = {
        let label = UILabel()
        label.text = "4. 말왕 - 장춘동 왕족발 보쌈"
        label.textColor = .black
        label.font = .boldSystemFont(ofSize: 20)
        return label
    }()
    
    let monthLikeView: UIView = {
        let view = UIView()
        view.layer.cornerRadius = 10
        view.layer.masksToBounds = true
        view.layer.borderColor = UIColor.black.cgColor
        view.layer.borderWidth = 2
        return view
    }()
    
    let playButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setImage(UIImage(systemName: "play.circle"), for: .normal)
        button.tintColor = .white
        return button
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = .white
        setViews()
        setConstraints()
        tableView.delegate = self
        tableView.dataSource = self
    }
    
    private func setViews() {
        view.addSubview(titleLabel)
        view.addSubview(monthLikeView)
        view.addSubview(tableView)
        view.addSubview(chartLabel)
        
        // monthLikeView에 레이블 추가
        monthLikeView.addSubview(firstLabel)
        monthLikeView.addSubview(secondLabel)
        monthLikeView.addSubview(thirdLabel)
        monthLikeView.addSubview(fourthLabel)
    }
    
    private func setConstraints() {
        titleLabel.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(80)
            make.leading.equalToSuperview().offset(15)
        }
        
        chartLabel.snp.makeConstraints { make in
            make.top.equalTo(titleLabel.snp.bottom).offset(15)
            make.leading.equalToSuperview().offset(15)
        }
        
        monthLikeView.snp.makeConstraints { make in
            make.top.equalTo(chartLabel.snp.bottom).offset(15)
            make.leading.equalToSuperview().offset(15)
            make.trailing.equalToSuperview().offset(-15)
            make.height.equalTo(200) // 높이는 레이블을 감쌀 수 있도록 충분히 설정
        }
        
        firstLabel.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(10)
            make.leading.equalToSuperview().offset(10)
        }
        
        secondLabel.snp.makeConstraints { make in
            make.top.equalTo(firstLabel.snp.bottom).offset(10)
            make.leading.equalToSuperview().offset(10)
        }
        
        thirdLabel.snp.makeConstraints { make in
            make.top.equalTo(secondLabel.snp.bottom).offset(10)
            make.leading.equalToSuperview().offset(10)
        }
        
        fourthLabel.snp.makeConstraints { make in
            make.top.equalTo(thirdLabel.snp.bottom).offset(10)
            make.leading.equalToSuperview().offset(10)
        }
        
        tableView.snp.makeConstraints { make in
            make.top.equalTo(monthLikeView.snp.bottom).offset(20)
            make.leading.equalToSuperview().offset(15)
            make.trailing.equalToSuperview().offset(-15)
            make.bottom.equalToSuperview()
        }
    }
}

extension HomeViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return mainLabel.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: HomeTableViewCell.cellId, for: indexPath) as! HomeTableViewCell
        cell.mainLabel.text = mainLabel[indexPath.item]
        cell.mainImageView.image = UIImage(named: mainImage[indexPath.item])
        cell.selectionStyle = .none
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 450
    }
}
