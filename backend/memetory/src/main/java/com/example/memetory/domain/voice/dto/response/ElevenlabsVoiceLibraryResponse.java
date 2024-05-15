package com.example.memetory.domain.voice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ElevenlabsVoiceLibraryResponse {

    @JsonProperty("voices")
    private List<ElevenlabsVoiceResponse> voices;

    @JsonProperty("has_more")
    private boolean hasMore;

    @JsonProperty("last_sort_id")
    private String lastSortId;
}
