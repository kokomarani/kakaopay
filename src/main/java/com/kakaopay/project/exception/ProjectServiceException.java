package com.kakaopay.project.exception;

@SuppressWarnings("serial")
public class ProjectServiceException extends ProjectException {
	public ProjectServiceException(String resultCode, String defaultMsg){
		super(resultCode, defaultMsg);
	}
}
