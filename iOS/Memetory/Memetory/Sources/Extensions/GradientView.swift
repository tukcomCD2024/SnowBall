//
//  GradientView.swift
//  Memetory
//
//  Created by 이승진 on 2024/06/19.
//

import UIKit

final class GradientView: EmptyView {
    
    var gradientLayerColors: [CGColor]? {
        didSet {
            bind()
        }
    }

    private let gradientLayer = CAGradientLayer()
    
    override func configure() {
        super.configure()

        layer.addSublayer(gradientLayer)
        isUserInteractionEnabled = false
    }

    override func layoutSublayers(of layer: CALayer) {
        super.layoutSublayers(of: layer)
        gradientLayer.startPoint = CGPoint(x: 0.5, y: 0.0)
        gradientLayer.endPoint = CGPoint(x: 0.5, y: 1.0)
        gradientLayer.frame = bounds
    }

    override func bind() {
        super.bind()
        
        gradientLayer.colors = gradientLayerColors
    }
}

class EmptyView: UIView {
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        configure()
    }

    @available(*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("required init?(coder: NSCoder) has not been implemented")
    }

    func configure() {}
    func bind() {}
}

extension CGColor {
    static let feedGradientColor1 = CGColor(gray: 0, alpha: 0)
    static let feedGradientColor2 = CGColor(gray: 100/256, alpha: 0.5)
}
