package com.finartzIntern.HotelRezervationSys.domain.model.dtos.response;

import java.util.Map;

public record HotelReviewSummaryResponseDto(Double averageRating, Long totalReviews, Map<Integer,Long> distribution) {
}
