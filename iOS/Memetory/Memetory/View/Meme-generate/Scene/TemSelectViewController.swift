//
//  TemSelectViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/02/13.
//

import UIKit
import SnapKit

class TemSelectViewController: UIViewController {
    
    private let sceneImage = Scene.sceneImage
    
    private enum Const {
      static let itemSize = CGSize(width: 300, height: 300)
      static let itemSpacing = 24.0
      
      static var insetX: CGFloat {
        (UIScreen.main.bounds.width - Self.itemSize.width) / 2.0
      }
      static var collectionViewContentInset: UIEdgeInsets {
        UIEdgeInsets(top: 0, left: Self.insetX, bottom: 0, right: Self.insetX)
      }
    }
    
    private let collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        layout.itemSize = Const.itemSize
        layout.minimumLineSpacing = Const.itemSpacing
        layout.minimumInteritemSpacing = 0
        let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
        cv.register(TemplateCollectionViewCell.self, forCellWithReuseIdentifier: TemplateCollectionViewCell.cellId)
        cv.contentInset = UIEdgeInsets(top: 0, left: 50, bottom: 0, right: 30)
        cv.showsHorizontalScrollIndicator = false
        cv.decelerationRate = .fast
        return cv
    }()
    
    let selectButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setTitle("완료하기", for: .normal)
        button.setTitleColor(.white, for: .normal)
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
    
    let adviceLabel: UILabel = {
        let label = UILabel()
        label.text = "모든 설정이 완료되면 눌러주세요!"
        label.font = UIFont(name: "Pretendard-Bold", size: 12)
        label.textColor = .lightGray
        return label
    }()

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .white
        tabBarController?.tabBar.isHidden = true
        title = "해리포터 템플릿"
        setViews()
        setConstraints()
    }
    
    @objc func selectButtonTapped() {
        let selectVC = SelectViewController()
        navigationController?.pushViewController(selectVC, animated: true)
    }
    
    func setViews() {
        collectionView.delegate = self
        collectionView.dataSource = self
        
        view.addSubview(collectionView)
        view.addSubview(selectButton)
        view.addSubview(adviceLabel)
    }
    
    func setConstraints() {
        collectionView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(150)
            make.bottom.equalTo(selectButton.snp.top).offset(-50)
            make.width.equalToSuperview()
        }
        
        selectButton.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.bottom.equalToSuperview().offset(-100)
            make.width.equalTo(320)
            make.height.equalTo(45)
        }
        
        adviceLabel.snp.makeConstraints { make in
            make.top.equalTo(selectButton.snp.bottom).offset(5)
            make.leading.equalTo(selectButton.snp.leading)
        }
    }
}

extension TemSelectViewController: UICollectionViewDataSource, UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
//        self.items.count
        return sceneImage.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: TemplateCollectionViewCell.cellId, for: indexPath) as! TemplateCollectionViewCell
//        cell.prepare(color: self.items[indexPath.item])
        cell.sceneImageView.image = UIImage(named: sceneImage[indexPath.item])
        return cell
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let selectVC = SelectViewController()
//        settingVC.delegate = self
        
//        let array = memberListManager.getMemberList()
//        detailVC.member = array[indexPath.row]
        navigationController?.pushViewController(selectVC, animated: true)
    }
}

extension TemSelectViewController: UICollectionViewDelegateFlowLayout {
    func scrollViewWillEndDragging(
        _ scrollView: UIScrollView,
        withVelocity velocity: CGPoint,
        targetContentOffset: UnsafeMutablePointer<CGPoint>
    ) {
        let scrolledOffsetX = targetContentOffset.pointee.x + scrollView.contentInset.left
        let cellWidth = Const.itemSize.width + Const.itemSpacing
        let index = round(scrolledOffsetX / cellWidth)
        targetContentOffset.pointee = CGPoint(x: index * cellWidth - scrollView.contentInset.left, y: scrollView.contentInset.top)
    }
}
