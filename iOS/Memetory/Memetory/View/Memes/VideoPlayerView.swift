//
//  VideoPlayerView.swift
//  Memetory
//
//  Created by 이승진 on 2024/06/19.
//

import UIKit
import AVKit

class VideoPlayerView: UIView {

    var playerLayer: AVPlayerLayer? // 재생될 때 layer 잡아주기(크기)
    var playerLooper: AVPlayerLooper? // 반복재생 관련 객체
    var queuePlayer: AVQueuePlayer? // 먼저 들어온 영상 먼저, 나중에 들어온 영상 나중
    var urlStr: String

    init(frame: CGRect, urlStr: String) {
        self.urlStr = urlStr
        super.init(frame: frame)

        let videoFileURL = Bundle.main.url(forResource: urlStr, withExtension: "mp4")!
        let playItem = AVPlayerItem(url: videoFileURL)

        self.queuePlayer = AVQueuePlayer(playerItem: playItem)
        playerLayer = AVPlayerLayer()

        playerLayer!.player = queuePlayer
        playerLayer!.videoGravity = .resizeAspectFill

        self.layer.addSublayer(playerLayer!)

        playerLooper = AVPlayerLooper(player: queuePlayer!, templateItem: playItem)
        queuePlayer!.play()
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    public func cleanUP() {
        queuePlayer?.pause()
        queuePlayer?.removeAllItems()
        queuePlayer = nil
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        playerLayer!.frame = bounds
    }
}
