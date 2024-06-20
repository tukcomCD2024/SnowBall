//
//  StorageViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/02/13.
//

import UIKit
import SnapKit

class StorageViewController: UIViewController {
    
    private let videoTitle = Scene.videoTitle
    private let timeLabel = Scene.timeLabel
    
    let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "보관함"
        label.textColor = .black
//        label.font = .boldSystemFont(ofSize: 25)
        label.font = UIFont(name: "Pretendard-Bold", size: 25)
        return label
    }()
    
    lazy var gridView = {
         let layout = UICollectionViewFlowLayout()
         layout.scrollDirection = .vertical
         layout.itemSize = CGSize(width: UIScreen.main.bounds.width / 2 - 20, height: 225)
         layout.minimumInteritemSpacing = 10
         
         let grid = UICollectionView(frame: .zero, collectionViewLayout: layout)
         grid.register(StorageCollectionViewCell.self, forCellWithReuseIdentifier: StorageCollectionViewCell.cellId)
         grid.showsVerticalScrollIndicator = false
         return grid
     }()

    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationItem.setHidesBackButton(true, animated: false)
        view.backgroundColor = .white
        setupColletionView()
        setViews()
        setConstrainsts()
    }
    
    func setupColletionView() {
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


extension StorageViewController: UICollectionViewDataSource, UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        //        self.items.count
        return videoTitle.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: StorageCollectionViewCell.cellId, for: indexPath) as! StorageCollectionViewCell
        //        cell.prepare(color: self.items[indexPath.item])
        cell.titleLabel.text = videoTitle[indexPath.item]
        cell.timeLabel.text = timeLabel[indexPath.item]
        return cell
        
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let selectVC = TemSelectViewController()
        //        settingVC.delegate = self
        
        //        let array = memberListManager.getMemberList()
        //        detailVC.member = array[indexPath.row]
        navigationController?.pushViewController(selectVC, animated: true)
    }
}

