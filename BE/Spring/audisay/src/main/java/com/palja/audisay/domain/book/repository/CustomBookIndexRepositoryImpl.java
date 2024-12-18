package com.palja.audisay.domain.book.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;

import com.palja.audisay.domain.book.dto.SearchAfterValues;
import com.palja.audisay.domain.book.dto.SearchSort;
import com.palja.audisay.domain.book.dto.request.SearchPaginationReqDto;
import com.palja.audisay.domain.book.entity.BookIndex;
import com.palja.audisay.global.util.StringUtil;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomBookIndexRepositoryImpl implements CustomBookIndexRepository {

	@Value("${spring.elasticsearch.min.score}")
	private float minScore;

	private final ElasticsearchOperations elasticsearchOperations;

	public SearchHits<BookIndex> searchPublishedBooks(SearchPaginationReqDto searchPaginationReqDto) {
		NativeQueryBuilder queryBuilder = NativeQuery.builder();

		setSearchQuery(queryBuilder, searchPaginationReqDto.getKeyword());
		setPageSize(queryBuilder, searchPaginationReqDto.getPageSize());
		SearchSort userSort = createSortCondition(searchPaginationReqDto.getSortBy(),
			searchPaginationReqDto.getSortOrder());
		setSortConditions(queryBuilder, userSort);
		setSearchAfter(queryBuilder, searchPaginationReqDto.getLastSearchId(), userSort);

		return elasticsearchOperations.search(queryBuilder.build(), BookIndex.class);
	}

	private void setSearchQuery(NativeQueryBuilder queryBuilder, String keyword) {
		if (StringUtil.isEmpty(keyword)) {
			queryBuilder.withQuery(q -> q.matchAll(m -> m));
		} else {
			Query query = new MultiMatchQueryBuilder()
				.addField("title", 3.0f)      // 부스팅 값을 문자열로 직접 지정
				.addField("author", 2.0f)
				.addField("publisher", 1.0f)
				.setTieBreaker(0.3)
				.build(keyword);
			queryBuilder.withQuery(query)
				.withMinScore(minScore); // 검색 최소 일치도
		}
	}

	private void setPageSize(NativeQueryBuilder queryBuilder, int pageSize) {
		queryBuilder.withMaxResults(pageSize);
	}

	private SearchSort createSortCondition(String sortBy, String sortOrder) {
		return SearchSort.setSort(sortBy, sortOrder);
	}

	private void setSortConditions(NativeQueryBuilder queryBuilder, SearchSort userSort) {
		queryBuilder
			.withSort(Sort.by(userSort.sortOrder(), userSort.sortBy()))
			.withSort(Sort.by(Sort.Direction.DESC, "bookId"));
	}

	private void setSearchAfter(NativeQueryBuilder queryBuilder, String lastSearchId, SearchSort userSort) {
		if (lastSearchId != null) {
			SearchAfterValues searchAfter = SearchAfterValues.parse(userSort.sortBy(), lastSearchId);
			queryBuilder.withSearchAfter(searchAfter.values());
		}
	}

	// 멀티 매칭 빌더
	private static class MultiMatchQueryBuilder {
		private final List<String> fieldsWithBoost = new ArrayList<>();
		private TextQueryType queryType = TextQueryType.BestFields;  // 기본값
		private Double tieBreaker;

		// 기본 필드 추가 메서드
		public MultiMatchQueryBuilder addField(String field, float boost) {
			fieldsWithBoost.add("%s^%f".formatted(field, boost));
			return this;
		}

		// 검색 타입 설정
		public MultiMatchQueryBuilder setQueryType(TextQueryType type) {
			this.queryType = type;
			return this;
		}

		// tie_breaker 설정
		public MultiMatchQueryBuilder setTieBreaker(Double tieBreaker) {
			this.tieBreaker = tieBreaker;
			return this;
		}

		public Query build(String keyword) {
			return Query.of(q -> q
				.multiMatch(m -> m
					.query(keyword)
					.fields(fieldsWithBoost)
					.type(queryType)
					.tieBreaker(tieBreaker)
				)
			);
		}
	}
}
