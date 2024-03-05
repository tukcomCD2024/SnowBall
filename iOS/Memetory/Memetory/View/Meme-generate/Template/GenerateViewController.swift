//
//  GenerateViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/02/13.
//

import UIKit
import SnapKit

final class GenerateViewController: UIViewController {
    
    private let tableView = UITableView()
    
    let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "템플릿 선택"
        label.textColor = .black
        label.font = UIFont(name: "Pretendard-Bold", size: 25)
//        label.font = .boldSystemFont(ofSize: 25)
        return label
    }()

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .white
        
        setupTableView()
        setViews()
        setConstrainsts()
    }
    
    func setupTableView() {
        // 델리게이트 패턴 대리자 설정
        tableView.dataSource = self
        tableView.delegate = self
        // 셀의 높이 설정
        tableView.rowHeight = 300
        
        // 셀의 등록 과정(스토리보드 사용시에는 스토리보드에서 자동 등록)
        tableView.register(TemplateTableViewCell.self, forCellReuseIdentifier: "TemplateCell")
        
//        tableView.backgroundColor = .gray
    }
    
    private func setViews() {
        view.addSubview(titleLabel)
        view.addSubview(tableView)
    }
    
    private func setConstrainsts() {
        titleLabel.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(80)
            make.leading.equalToSuperview().offset(15)
        }
        
        tableView.snp.makeConstraints { make in
            make.top.equalTo(titleLabel.snp.bottom).offset(20)
            make.leading.equalToSuperview()
            make.trailing.equalToSuperview()
            make.bottom.equalToSuperview()
        }
    }
}

extension GenerateViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "TemplateCell", for: indexPath) as! TemplateTableViewCell
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let detailVC = TemSelectViewController()
//        detailVC.movieData = moviesArray[indexPath.row]
        show(detailVC, sender: nil)
    }
}
