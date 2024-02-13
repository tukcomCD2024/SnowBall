//
//  OnboardingViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/02/08.
//

import UIKit
import SnapKit

class OnboardingViewController: UIViewController {
    
    var onboardingData: [OnboardingDataModel] = []
        var currentPage: Int = 0 {
            didSet {
                pageControl.currentPage = currentPage
                if currentPage == onboardingData.count - 1 {
                    nextButton.setTitle("Start", for: .normal)
                } else {
                    nextButton.setTitle("Next", for: .normal)
                }
            }
        }
    let pageControl: UIPageControl = {
        let page = UIPageControl()
        page.currentPageIndicatorTintColor = .black
        page.pageIndicatorTintColor = .gray
        return page
    }()
    
    let skipButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setTitle("skip", for: .normal)
        button.setTitleColor(.lightGray, for: .normal)
        return button
    }()
    
    let onboardingCollectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        return collectionView
    }()
    
    let nextButton: UIButton = {
        let button = UIButton(type: .custom)
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
        button.addTarget(self, action: #selector(nextButtonTapped), for: .touchUpInside)
        return button
    }()
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

        // navigationItem 숨기기
        self.navigationItem.setHidesBackButton(true, animated: false)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        
        setViews()
        setConstraints()
        setCollectionView()
        setOnboardingData()
    }
    
    @objc private func nextButtonTapped() {
        if currentPage == onboardingData.count - 1 {
            print("go to main")
            let nickNameSetViewController = NickNameSetViewController()
            navigationController?.pushViewController(nickNameSetViewController, animated: true)
        } else {
            currentPage += 1
            let indexPath = IndexPath(item: currentPage, section: 0)
            onboardingCollectionView.scrollToItem(at: indexPath, at: .centeredHorizontally, animated: true)
        }
    }
}

extension OnboardingViewController {
    private func setViews() {
        pageControl.isUserInteractionEnabled = false
        view.addSubview(pageControl)
        view.addSubview(skipButton)
        view.addSubview(onboardingCollectionView)
        view.addSubview(nextButton)
    }
    
    private func setConstraints() {
        pageControl.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(20)
            make.centerX.equalToSuperview()
        }
        
        skipButton.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(20)
            make.leading.equalTo(pageControl.snp.trailing).offset(15)
            make.width.equalTo(30)
            make.height.equalTo(15)
        }
        
        onboardingCollectionView.snp.makeConstraints { make in
            make.top.equalTo(pageControl.snp.bottom).offset(15)
            make.leading.equalToSuperview().offset(15)
            make.trailing.equalToSuperview().offset(-15)
            make.bottom.equalToSuperview().offset(-50)
        }
        
        nextButton.snp.makeConstraints { make in
            make.bottom.equalToSuperview().offset(-15)
            make.width.equalTo(320)
            make.height.equalTo(45)
            make.centerX.equalToSuperview()
        }
    }
    
    private func setCollectionView() {
        onboardingCollectionView.delegate = self
        onboardingCollectionView.dataSource = self
        
        // cell 등록
        onboardingCollectionView.register(OnboardingCollectionViewCell.self, forCellWithReuseIdentifier: OnboardingCollectionViewCell.identifier)
    }
    
    private func setOnboardingData() {
        onboardingData.append(contentsOf: [
            OnboardingDataModel(imageName: "MockUp",
                                title: "원하는 템플릿을 선택하고"),
            OnboardingDataModel(imageName: "frame",
                                title: "얼굴, 목소리, 대사를 입력하면?"),
            OnboardingDataModel(imageName: "MockUp",
                                title: "나만의 밈 완성!")
        ])
    }
}

// MARK: - CollectionView Delegate, DataSource
extension OnboardingViewController: UICollectionViewDelegate, UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return onboardingData.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = onboardingCollectionView.dequeueReusableCell(withReuseIdentifier: OnboardingCollectionViewCell.identifier, for: indexPath) as? OnboardingCollectionViewCell else { fatalError("셀 타입 캐스팅 실패")
        }
        cell.setOnboardingSlides(onboardingData[indexPath.row])
        return cell
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let width = scrollView.frame.width
        currentPage = Int(scrollView.contentOffset.x / width)
    }
}

// MARK: - CollectionView Delegate Flow Layout
extension OnboardingViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: collectionView.frame.width, height: collectionView.frame.height)
    }
}
