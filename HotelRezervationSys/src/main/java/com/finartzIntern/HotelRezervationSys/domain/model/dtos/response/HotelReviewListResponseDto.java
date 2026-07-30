package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.HotelReviews;

import java.time.LocalDateTime;

public record HotelReviewListResponseDto(String userName, Integer rating, String comment, LocalDateTime createdAt) {
    public static HotelReviewListResponseDto from(HotelReviews review) {
        String userName = review.getUser().getName();

        if (review.getUser().getSurname() != null) {
            userName = userName + " " + review.getUser().getSurname();
        }

        return new HotelReviewListResponseDto(
                userName,
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}
