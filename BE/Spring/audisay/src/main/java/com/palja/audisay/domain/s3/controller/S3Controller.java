package com.palja.audisay.domain.s3.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palja.audisay.domain.s3.dto.S3ResponseDto;
import com.palja.audisay.domain.s3.service.S3Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Tag(name = "S3 파일", description = "S3 파일 업로드/다운로드 API")
public class S3Controller {

	private final S3Service awsS3Service;

	@GetMapping("/presigned/upload")
	@Operation(summary = "파일 업로드", description = "구현 중...")
	public ResponseEntity<S3ResponseDto> getPresignedUrlToUpload(
		@RequestParam(value = "filename") String fileName) throws IOException {
		return new ResponseEntity<>(awsS3Service.getPresignedUrlToUpload(fileName), HttpStatus.OK);
	}

	@GetMapping("/presigned/download")
	@Operation(summary = "파일 다운로드", description = "3분 간 유효한 다운로드 링크 반환")
	@Parameters({
			@Parameter(name = "filename", description = "파일 명", example = "temp/audisay.png")
	})
	public ResponseEntity<S3ResponseDto> getPresignedUrlToDownload(
		@RequestParam(value = "filename") String fileName) throws IOException {
		return new ResponseEntity<>(awsS3Service.getPresignedUrlToDownload(fileName), HttpStatus.OK);
	}
}