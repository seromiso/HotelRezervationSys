package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ConflictException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.CreateHotelReviewRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.UpdateHotelReviewRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.HotelReviews;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelReviewRepository;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.repository.UserRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HotelReviewServiceImpl implements HotelReviewService {

    private final HotelReviewRepository hotelReviewRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    public HotelReviewServiceImpl(HotelReviewRepository hotelReviewRepository, HotelRepository hotelRepository, UserRepository userRepository){
        this.hotelReviewRepository = hotelReviewRepository;
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<HotelReviewResponseDto> getReviewsByHotelId(Long hotelId) {
        return hotelReviewRepository.findByHotel_IdOrderByCreatedAtDesc(hotelId).stream().map(HotelReviewResponseDto::from).toList();
    }

    @Override
    public HotelReviewResponseDto getReviewById(Long reviewId) {
        HotelReviews review = hotelReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "error.hotel.review.not.found"));
        return HotelReviewResponseDto.from(review) ;
    }

    @Override
    public HotelReviewResponseDto getReviewByReservationId(Long reservationId) {
        return hotelReviewRepository.findByReservationId(reservationId).map(HotelReviewResponseDto::from).orElseThrow(() -> new ResourceNotFoundException("error.hotel.review.not.found"));
    }

    @Override
    public boolean hasReviewForReservation(Long reservationId) {
        return hotelReviewRepository.existsByReservationId(reservationId);
    }

    @Override
    public Double getAverageRatingByHotelId(Long hotelId) {
        Double averageRating = hotelReviewRepository.findAverageRatingByHotelId(hotelId);
        return averageRating == null ? 0.0 : averageRating;
    }

    @Override
    @Transactional
    public HotelReviewResponseDto createReview(Long hotelId, Long userId, Long reservationId, CreateHotelReviewRequestDto request) {
        if(hotelReviewRepository.existsByReservationId(reservationId)){

            throw new ConflictException("error.hotel.review.already.exists");
        }
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("error.hotel.not.found"+ hotelId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "error.user.not.found" + userId
                ));
        HotelReviews review = new HotelReviews();
        review.setHotel(hotel);
        review.setUser(user);
        review.setReservationId(reservationId);
        review.setRating(request.rating());
        review.setComment(request.comment());

        HotelReviews savedReview = hotelReviewRepository.save(review);

        return HotelReviewResponseDto.from(savedReview);
    }

    @Override
    @Transactional
    public HotelReviewResponseDto updateReview(Long reviewId, UpdateHotelReviewRequestDto request) {
        HotelReviews review = hotelReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "error.hotel.review.not.found" + reviewId
                ));

        review.setRating(request.rating());
        review.setComment(request.comment());

        HotelReviews updatedReview = hotelReviewRepository.save(review);

        return HotelReviewResponseDto.from(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        HotelReviews review = hotelReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "error.hotel.review.not.found" + reviewId
                ));

        hotelReviewRepository.delete(review);
    }
}
