//
//  LoadingView.swift
//  Memetory
//
//  Created by 이승진 on 2024/05/16.
//

import UIKit
import SnapKit

final class LoadingView: UIView {
    
    private let backgroundView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        return view
    }()
    
    private let activityIndicatorView: UIActivityIndicatorView = {
        let view = UIActivityIndicatorView(style: .large)
        return view
        
    }()
    
    var isLoading = false {
        didSet {
            self.isHidden = !self.isLoading
            self.isLoading ? self.activityIndicatorView.startAnimating() : self.activityIndicatorView.stopAnimating()
        }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        self.addSubview(self.backgroundView)
        self.addSubview(self.activityIndicatorView)
        
        setConstriants()
    }
    
    func setConstriants() {
        backgroundView.snp.makeConstraints { make in
            make.leading.equalToSuperview()
            make.trailing.equalToSuperview()
            make.top.equalToSuperview()
            make.bottom.equalToSuperview()
        }
        
        activityIndicatorView.snp.makeConstraints { make in
            make.centerX.equalTo(self.snp.centerX)
            make.centerY.equalTo(self.snp.centerY)
        }
    }
    required init?(coder: NSCoder) {
        fatalError()
    }
}


