//
//  TabBarViewController.swift
//  Memetory
//
//  Created by 이승진 on 2024/02/13.
//

import UIKit

class TabBarViewController: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        tabBar.barTintColor = .white
        tabBar.tintColor = .black
//        tabBar.layer.borderWidth = 0.3

        let homeView = HomeViewController()
        let generateView = GenerateViewController()
        let memesView = MemesViewController()
        let storageView = StorageViewController()

        
        let nav1 = UINavigationController(rootViewController: homeView)
        let nav2 = UINavigationController(rootViewController: generateView)
        let nav3 = UINavigationController(rootViewController: memesView)
        let nav4 = UINavigationController(rootViewController: storageView)
        
        
        nav1.tabBarItem = UITabBarItem(title: "홈", image: UIImage(named: "HomeIcon"), tag: 1)
        nav2.tabBarItem = UITabBarItem(title: "밈 생성", image: UIImage(named: "GenerateIcon"), tag: 2)
        nav3.tabBarItem = UITabBarItem(title: "밈's", image: UIImage(named: "MemesIcon"), tag: 3)
        nav4.tabBarItem = UITabBarItem(title: "보관함", image: UIImage(named: "StorageIcon"), tag: 4)

        setViewControllers([nav1,nav2,nav3,nav4], animated: false)
    }
    

}
