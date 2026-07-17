package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.HotelReviews;

import java.time.LocalDateTime;

public record HotelReviewResponseDto(Long id,
                                     Long hotelId,
                                     Long userId,
                                     Long reservationId,
                                     Integer rating,
                                     String comment,
                                     LocalDateTime createdAt,
                                     LocalDateTime updatedAt) {

    public static HotelReviewResponseDto from(HotelReviews review) {
        return new HotelReviewResponseDto(
                review.getId(),
                review.getHotel().getId(),
                review.getUser().getId(),
                review.getReservationId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
