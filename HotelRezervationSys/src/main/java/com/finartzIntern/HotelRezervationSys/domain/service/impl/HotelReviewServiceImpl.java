package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.ConflictException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.InvalidRequestException;
import com.finartzIntern.HotelRezervationSys.domain.exceptions.ResourceNotFoundException;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.CreateHotelReviewRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.request.UpdateHotelReviewRequestDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewListResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.dtos.response.HotelReviewSummaryResponseDto;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.HotelReviews;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Reservation;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.ReservationStatus;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.HotelReviewRepository;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.Hotel;
import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.repository.ReservationRepository;
import com.finartzIntern.HotelRezervationSys.domain.repository.UserRepository;
import com.finartzIntern.HotelRezervationSys.domain.service.HotelReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class HotelReviewServiceImpl implements HotelReviewService {

    private final HotelReviewRepository hotelReviewRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public HotelReviewServiceImpl(HotelReviewRepository hotelReviewRepository, HotelRepository hotelRepository, UserRepository userRepository, ReservationRepository reservationRepository){
        this.hotelReviewRepository = hotelReviewRepository;
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("error.hotel.not.found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "error.user.not.found"));
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
                        "error.hotel.review.not.found"));

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
                        "error.hotel.review.not.found"));

        hotelReviewRepository.delete(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HotelReviewListResponseDto> getPublicReviewsByHotelId(Long hotelId, Pageable pageable) {
        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("error.hotel.not.found");
        }

        return hotelReviewRepository.findByHotel_IdOrderByCreatedAtDesc(hotelId, pageable)
                .map(HotelReviewListResponseDto::from);
    }

    @Override
    @Transactional(readOnly = true)
    public HotelReviewSummaryResponseDto getReviewSummaryByHotelId(Long hotelId) {
        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("error.hotel.not.found");
        }

        Double averageRating = hotelReviewRepository.findAverageRatingByHotelId(hotelId);
        Long totalReviews = hotelReviewRepository.countByHotel_Id(hotelId);

        Map<Integer, Long> distribution = new HashMap<>();
        distribution.put(5, 0L);
        distribution.put(4, 0L);
        distribution.put(3, 0L);
        distribution.put(2, 0L);
        distribution.put(1, 0L);

        List<Object[]> ratingCounts = hotelReviewRepository.countReviewsByRatingForHotel(hotelId);

        for (Object[] row : ratingCounts) {
            Integer rating = (Integer) row[0];
            Long count = (Long) row[1];
            distribution.put(rating, count);
        }

        return new HotelReviewSummaryResponseDto(
                averageRating == null ? 0.0 : averageRating,
                totalReviews,
                distribution
        );
    }

    @Override
    @Transactional
    public HotelReviewResponseDto createReviewForReservation(
            Long reservationId,
            CreateHotelReviewRequestDto request,
            User currentUser
    ) {
        if (currentUser == null) {
            throw new AccessDeniedException("error.authentication.required");
        }

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("error.reservation.not.found"));

        if (!ReservationStatus.COMPLETED.equals(reservation.getStatus())) {
            throw new InvalidRequestException("error.reservation.not.completed");
        }

        Long reservationOwnerId = reservation.getBooking().getUser().getId();

        if (!reservationOwnerId.equals(currentUser.getId())) {
            throw new AccessDeniedException("error.reservation.review.forbidden");
        }

        if (hotelReviewRepository.existsByReservationId(reservationId)) {
            throw new ConflictException("error.hotel.review.already.exists");
        }

        HotelReviews review = new HotelReviews();
        review.setHotel(reservation.getHotel());
        review.setUser(currentUser);
        review.setReservationId(reservation.getId());
        review.setRating(request.rating());
        review.setComment(request.comment());

        HotelReviews savedReview = hotelReviewRepository.save(review);

        return HotelReviewResponseDto.from(savedReview);
    }
}
