//
//  LoadingViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/05/16.
//

import UIKit
import SnapKit

final class LoadingViewController: UIViewController {
    
    private let loadingView: LoadingView = {
        let view = LoadingView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let loadingImage: UIImageView = {
        let imageView = UIImageView()
        imageView.image = UIImage(named: "LoadingImage")
        imageView.contentMode = .scaleAspectFill
        imageView.layer.cornerRadius = 10
        imageView.layer.masksToBounds = true
        return imageView
    }()
    
    private let completeButton: UIButton = {
        let button = UIButton(type: .custom)
        button.setTitle("완료", for: .normal)
        button.setTitleColor(.white, for: .normal)
        button.titleLabel?.font = UIFont(name: "Pretendard-Bold", size: 18)
        button.layer.cornerRadius = 10
        button.layer.masksToBounds = true
        button.layer.borderWidth = 1
        button.layer.borderColor = UIColor.black.cgColor
        button.layer.backgroundColor = UIColor.black.cgColor
        button.addTarget(self, action: #selector(completeButtonTapped), for: .touchUpInside)
        return button
    }()
//    
//    private let tableView: UITableView = {
//        let view = UITableView()
//        view.allowsSelection = false
//        view.backgroundColor = .clear
//        view.separatorStyle = .none
//        view.bounces = true
//        view.showsVerticalScrollIndicator = true
//        view.contentInset = .zero
//        view.register(UITableViewCell.self, forCellReuseIdentifier: "cell")
//        view.translatesAutoresizingMaskIntoConstraints = false
//        return view
//    }()
    
    var items = (1...20).map(String.init)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.view.backgroundColor = .white
        
//        self.view.addSubview(self.tableView)
        self.view.addSubview(self.loadingImage)
        self.view.addSubview(self.loadingView)
        self.view.addSubview(self.completeButton)
        
//        NSLayoutConstraint.activate([
//            self.tableView.leftAnchor.constraint(equalTo: self.view.leftAnchor),
//            self.tableView.rightAnchor.constraint(equalTo: self.view.rightAnchor),
//            self.tableView.bottomAnchor.constraint(equalTo: self.view.bottomAnchor),
//            self.tableView.topAnchor.constraint(equalTo: self.view.safeAreaLayoutGuide.topAnchor),
//        ])
        
        
        
//        NSLayoutConstraint.activate([
//            self.loadingView.leftAnchor.constraint(equalTo: self.tableView.leftAnchor),
//            self.loadingView.rightAnchor.constraint(equalTo: self.tableView.rightAnchor),
//            self.loadingView.bottomAnchor.constraint(equalTo: self.tableView.bottomAnchor),
//            self.loadingView.topAnchor.constraint(equalTo: self.tableView.topAnchor),
//        ])
//        
//        self.tableView.dataSource = self
//        self.tableView.delegate = self
        
        self.loadingView.isLoading = true
        self.getSomeData { [weak self] in
            self?.loadingView.isLoading = false
        }
        
        setConstraints()
    }
    
    private func setConstraints() {
        
        loadingImage.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.centerY.equalToSuperview()
            make.width.height.equalTo(300)
        }
        
        loadingView.snp.makeConstraints { make in
            make.leading.trailing.top.bottom.equalToSuperview()
        }
        
        completeButton.snp.makeConstraints { make in
            make.top.equalTo(loadingImage.snp.bottom).offset(30)
            make.leading.equalToSuperview().offset(30)
            make.trailing.equalToSuperview().offset(-30)
        }
    }
    
    
    
    private func getSomeData(completion: @escaping () -> ()) {
        DispatchQueue.main.asyncAfter(deadline: .now() + 5.0) {
            completion()
        }
    }
    
    @objc func completeButtonTapped() {
        let templetSetVC = TemSelectViewController()
        navigationController?.pushViewController(templetSetVC, animated: true)
    }
}

//extension LoadingViewController: UITableViewDataSource {
//    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
//        self.items.count
//    }
//    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//        let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
//        cell.textLabel?.text = self.items[indexPath.row]
////        cell.backgroundColor = .gray
//        return cell
//    }
//}
//
//extension LoadingViewController: UITableViewDelegate {
//    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
//        120
//    }
//}

