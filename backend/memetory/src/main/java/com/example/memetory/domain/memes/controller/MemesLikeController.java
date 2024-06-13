package com.example.memetory.domain.memes.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.like.service.LikeService;
import com.example.memetory.domain.memes.service.MemesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memes/like")
public class MemesLikeController {
	private final MemesService memesService;
	private final LikeService likeService;
}
