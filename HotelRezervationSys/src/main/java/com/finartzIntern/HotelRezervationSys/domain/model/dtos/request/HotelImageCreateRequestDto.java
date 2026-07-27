package com.finartzIntern.HotelRezervationSys.domain.model.dtos.request;

import lombok.Data;
import java.util.List;

@Data
public class HotelImageCreateRequestDto {
    private List<ImageItem> images;

    @Data
    public static class ImageItem {
        private String url;
        private Long categoryId;
        private Integer displayOrder;
    }
}