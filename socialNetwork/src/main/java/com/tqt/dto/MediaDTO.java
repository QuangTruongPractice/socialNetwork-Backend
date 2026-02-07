package com.tqt.dto;

import com.tqt.pojo.PostMedia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {
    private String url;
    private String type;

    public MediaDTO(PostMedia pm) {
        this.url = pm.getUrl();
        this.type = pm.getType();
    }
}
