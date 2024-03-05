//
//  MemesViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/02/13.
//

import UIKit
import SnapKit

class MemesViewController: UIViewController {
    
    let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "밈's"
        label.textColor = .black
//        label.font = .boldSystemFont(ofSize: 25)
        label.font = UIFont(name: "Pretendard-Bold", size: 25)
        return label
    }()

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .white
        setViews()
        setConstrainsts()
    }
    
    private func setViews() {
        view.addSubview(titleLabel)
    }
    
    private func setConstrainsts() {
        titleLabel.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(80)
            make.leading.equalToSuperview().offset(15)
        }
    }
}
