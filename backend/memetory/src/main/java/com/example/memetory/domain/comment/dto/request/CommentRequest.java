package com.example.memetory.domain.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class CommentRequest {
	private String content;
	private Long memesId;
	private String email;

	public void setMemesIdAndEmail(Long memesId, String email) {
		this.memesId = memesId;
		this.email = email;
	}
}
