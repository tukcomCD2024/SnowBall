//
//  MemesViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/02/13.
//

import UIKit
import SnapKit

class MemesViewController: UIViewController {
    
    lazy var collectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        layout.minimumLineSpacing = 0
        layout.minimumInteritemSpacing = 0
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.backgroundColor = .white
        return collectionView
    }()
    
    private var nowPage = 0
    
    private let videoURLStrArr = ["dummyVideo03", "dummyVideo04", "dummyVideo05", "dummyVideo06", "dummyVideo07"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
    
        setupcollectionView()
//        removeVisualEffectSubviews()
        
    }
    
    //MARK: -Actions
    
    //MARK: -Helpers
    private func setupcollectionView() {
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.decelerationRate = .fast // 스크롤이 빨리 되도록
        collectionView.register(
            MemesCollectionViewCell.self,
            forCellWithReuseIdentifier: MemesCollectionViewCell.identifier)
        
        view.addSubview(collectionView)
               
        // Setup constraints using SnapKit
        collectionView.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide.snp.top)
            make.bottom.equalTo(view.safeAreaLayoutGuide.snp.bottom)
            make.leading.trailing.equalToSuperview()
        }
        
        startLoop()
    }
    
    private func startLoop() {
        let _ = Timer.scheduledTimer(withTimeInterval: 10, repeats: true) { _ in
            self.moveNextPage()
        }
    }
    
    private func moveNextPage() {
        let itemCount = collectionView.numberOfItems(inSection: 0)
        
        nowPage += 1
        if (nowPage >= itemCount) {
            // 마지막 페이지
            nowPage = 0
        }
        
        collectionView.scrollToItem(
            at: IndexPath(item: nowPage, section: 0),
            at: .centeredVertically,
            animated: true)
    }
    
//    // Method to remove UIVisualEffectSubview
//    private func removeVisualEffectSubviews() {
//        for subview in view.subviews {
//            if let visualEffectSubview = subview as? UIVisualEffectView {
//                visualEffectSubview.removeFromSuperview()
//            }
//        }
//    }
}

//MARK: -UICollectionViewDelegate, UICollectionViewDataSource
extension MemesViewController: UICollectionViewDelegate, UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return videoURLStrArr.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: MemesCollectionViewCell.identifier,
            for: indexPath) as? MemesCollectionViewCell else { return UICollectionViewCell() }
        cell.setupURL(videoURLStrArr.randomElement()!)
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if let cell = collectionView.cellForItem(at: indexPath) as? MemesCollectionViewCell {
            cell.videoView?.cleanUP()
        }
    }
}

//MARK: -UICollectionViewDelegateFlowLayout
extension MemesViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: collectionView.frame.width, height: collectionView.frame.height)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        1
    }
}
//
//extension UITabBar {
//    func setUpUITabBar(){
//        self.backgroundImage = UIImage()
//        self.shadowImage = UIImage()
//        self.clipsToBounds = true
//    }
//}
