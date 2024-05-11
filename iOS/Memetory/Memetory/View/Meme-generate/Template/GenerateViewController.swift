//
//  GenerateViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/02/13.
//

import UIKit
import SnapKit
import RxSwift

final class GenerateViewController: UIViewController {
    
    private let templateTitle = Scene.templateTitle
    
    var disposeBag = DisposeBag()
    
    let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "템플릿 선택"
        label.textColor = .black
        label.font = UIFont(name: "Pretendard-Bold", size: 25)
        return label
    }()
    
    lazy var gridView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        layout.itemSize = CGSize(width: UIScreen.main.bounds.width / 2 - 20, height: 225)
        layout.minimumInteritemSpacing = 10
        
        let grid = UICollectionView(frame: .zero, collectionViewLayout: layout)
        grid.register(MainTemplateCollectionViewCell.self, forCellWithReuseIdentifier: MainTemplateCollectionViewCell.cellId)
        grid.showsVerticalScrollIndicator = false
        return grid
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
        gridView.delegate = self
        gridView.dataSource = self
    }
    
    private func setViews() {
        view.addSubview(titleLabel)
        view.addSubview(gridView)
    }
    
    private func setConstrainsts() {
        titleLabel.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(80)
            make.leading.equalToSuperview().offset(15)
        }
        
        gridView.snp.makeConstraints{
            $0.leading.equalToSuperview().offset(15)
            $0.trailing.equalToSuperview().offset(-15)
            $0.top.equalTo(titleLabel.snp.bottom).offset(30)
            $0.bottom.equalTo(view.safeAreaLayoutGuide)
        }
    }
}

extension GenerateViewController: UICollectionViewDataSource, UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
//        self.items.count
        return templateTitle.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: MainTemplateCollectionViewCell.cellId, for: indexPath) as! MainTemplateCollectionViewCell
//        cell.prepare(color: self.items[indexPath.item])
        cell.titleLabel.text = templateTitle[indexPath.item]
        return cell
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let detailVC = TemSelectViewController()
//        settingVC.delegate = self
        
//        let array = memberListManager.getMemberList()
//        detailVC.member = array[indexPath.row]
        navigationController?.pushViewController(detailVC, animated: true)
    }
}
