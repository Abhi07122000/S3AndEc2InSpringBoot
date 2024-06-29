package com.aws.Nimesa.api.v1.advice;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final int RANDOM_LOWER = 1000;
	private static final int RANDOM_UPPER = 9000;
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneralError(HttpServletRequest request, HttpServletResponse response,
			Exception ex) {
		String errorId = getErrorId();
		generateLogs(request, response, ex, errorId);
		return ResponseEntity.internalServerError().body(errorId);
	}

	public String getErrorId() {
		return "CashRich " + formatter.format(LocalDateTime.now())
				+ (new SecureRandom().nextInt(RANDOM_UPPER) + RANDOM_LOWER);
	}

	private String generateLogs(HttpServletRequest request, HttpServletResponse response, Exception ex,
			String errorId) {

		StringBuilder result = new StringBuilder("ERROR ID IS: " + errorId + " \n");
		result.append("### URL : (" + request.getMethod() + ") " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : "") + "\n");

		if (StringUtils.equalsAnyIgnoreCase(request.getMethod(), "POST", "PUT")) {
			try {
				result.append("### POST Data : "
						+ request.getReader().lines().collect(Collectors.joining(System.lineSeparator())) + "\n");
			} catch (IOException e1) {
			}
		}
		result.append("### Exception error : " + ExceptionUtils.getStackTrace(ex) + "\n");
		log.error(result.toString());
		return errorId;
	}
}
