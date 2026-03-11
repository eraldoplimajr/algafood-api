package com.algaworks.algafood.core.validation;

import org.springframework.http.MediaType;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

	private String[] allowed;

	@Override
	public void initialize(FileContentType constraintAnnotation) {
		this.allowed = constraintAnnotation.allowed();
	}

	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {

		return multipartFile.getContentType().contains(MediaType.IMAGE_JPEG_VALUE) || multipartFile.getContentType().contains(MediaType.IMAGE_PNG_VALUE);

	}


	public boolean isValid(String[] strings, ConstraintValidatorContext constraintValidatorContext) {
		List<String> list = Arrays.asList(strings);

		boolean b = list.stream()
				.allMatch(s -> s.contains(MediaType.IMAGE_JPEG_VALUE) || s.contains(MediaType.IMAGE_PNG_VALUE));

		return b;
	}


}
