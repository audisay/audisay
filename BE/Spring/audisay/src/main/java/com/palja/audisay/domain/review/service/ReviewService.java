package com.palja.audisay.domain.review.service;

import com.palja.audisay.domain.book.entity.Book;
import com.palja.audisay.domain.book.service.BookService;
import com.palja.audisay.domain.member.entity.Member;
import com.palja.audisay.domain.member.service.MemberService;
import com.palja.audisay.domain.review.dto.MyPageReviewListResponseDto;
import com.palja.audisay.domain.review.dto.ReviewListResponseDto;
import com.palja.audisay.domain.review.dto.ReviewRequestDto;
import com.palja.audisay.domain.review.dto.ReviewResponseDto;
import com.palja.audisay.domain.review.entity.Review;
import com.palja.audisay.domain.review.repository.ReviewRepository;
import com.palja.audisay.global.exception.exceptions.ReviewBookDuplicatedException;
import com.palja.audisay.global.exception.exceptions.ReviewNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final MemberService memberService;
	private final BookService bookService;
	private final ReviewRepository reviewRepository;

	public ReviewListResponseDto getBookReviewsWithMemberReview(Long memberId, Long bookId, LocalDateTime lastUpdatedAt, Long lastReviewId, int pageSize) {
		Member member = memberService.validateMember(memberId);
		Book book = bookService.validatePublishedBook(bookId);

		// 첫 요청인지 확인
		boolean isFirstPage = (lastUpdatedAt == null && lastReviewId == null);
		// 본인의 리뷰 조회 (첫 요청일 때만 조회)
		ReviewResponseDto memberReviewDto = null;
		if (isFirstPage) {
			Review memberReview = reviewRepository.findMemberReviewForBook(member, book).orElse(null);
			memberReviewDto = (memberReview != null) ? ReviewResponseDto.toMemberReviewDto(memberReview) : null;
		}

		// cursor 값 설정
		LocalDateTime updatedAtCursor = (lastUpdatedAt != null) ? lastUpdatedAt : LocalDateTime.now();
		Pageable pageable = PageRequest.of(0, pageSize);
		List<Review> reviews = reviewRepository.findOtherReviewsWithCursor(book, member, updatedAtCursor, lastReviewId, pageable);
		List<ReviewResponseDto> reviewList = reviews.stream().map(ReviewResponseDto::toReviewListDto).toList();

		// 다음 커서 계산 (다음 페이지가 없다면 null 반환)
		LocalDateTime nextUpdatedAt = (reviews.size() < pageSize) ? null : reviews.getLast().getUpdatedAt();
		Long nextReviewId = (reviews.size() < pageSize) ? null : reviews.getLast().getReviewId();

		return ReviewListResponseDto.builder()
			.memberReview(memberReviewDto)
			.reviewList(reviewList)
				.lastUpdatedAt(nextUpdatedAt)
				.lastReviewId(nextReviewId)
			.build();
	}

	public MyPageReviewListResponseDto getMyReviewsAfterCursor(Long memberId, LocalDateTime lastUpdatedAt, Long lastReviewId, int pageSize) {
		Member member = memberService.validateMember(memberId);
		LocalDateTime updatedAtCursor = (lastUpdatedAt != null) ? lastUpdatedAt : LocalDateTime.now();

		Pageable pageable = PageRequest.of(0, pageSize);
		List<Review> reviews = reviewRepository.findReviewsWithCursor(member, updatedAtCursor, lastReviewId, pageable);
		List<ReviewResponseDto> reviewList = reviews.stream().map(ReviewResponseDto::toDto).toList();

		// 다음 커서 계산
		LocalDateTime nextUpdatedAt = (reviews.size() < pageSize) ? null : reviews.getLast().getUpdatedAt();
		Long nextReviewId = (reviews.size() < pageSize) ? null : reviews.getLast().getReviewId();

		return MyPageReviewListResponseDto.builder()
				.reviewList(reviewList)
				.lastUpdatedAt(nextUpdatedAt)
				.lastReviewId(nextReviewId)
				.build();
	}

	@Transactional
	public void createReview(Long memberId, ReviewRequestDto reviewRequestDto) {
		Member member = memberService.validateMember(memberId);
		Book book = bookService.validatePublishedBook(reviewRequestDto.getBookId());
		// 이미 리뷰가 존재하는지 확인
		if (reviewRepository.existsByMemberAndBook(member, book)) {
			throw new ReviewBookDuplicatedException(); // 중복된 책 예외 던짐
		}
		Review review = reviewRequestDto.toEntity(member, book);
		reviewRepository.save(review);
	}

	@Transactional
	public void updateReview(Long memberId, Long reviewId, ReviewRequestDto reviewRequestDto) {
		memberService.validateMember(memberId);
		Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);

		if (reviewRequestDto.getScore() != null) { // requestBody에 score가 존재할 때만 score 업데이트
			review.setScore(reviewRequestDto.getScore().byteValue());
		}
		if (reviewRequestDto.getContent() != null) { // requestBody에 content가 존재할 때만 score 업데이트
			review.setContent(reviewRequestDto.getContent());
		}
		reviewRepository.save(review);
	}

	@Transactional
	public void deleteReview(Long memberId, Long reviewId) {
		memberService.validateMember(memberId);
		int deletedCount = reviewRepository.deleteByReviewIdAndMemberMemberId(reviewId, memberId);
		if (deletedCount == 0) { // 리뷰가 존재하는지 확인
			throw new ReviewNotFoundException();
		}
	}

}