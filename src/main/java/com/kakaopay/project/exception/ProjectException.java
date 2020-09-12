package com.kakaopay.project.exception;

@SuppressWarnings("serial")
public abstract class ProjectException extends RuntimeException {
	private String resultCode;		// 결과코드 변수
	private String defaultMsg;		// 처리결과 메시지 변수

	protected ProjectException(String resultCode, String defaultMsg) {
		this.resultCode = resultCode;
		this.defaultMsg = defaultMsg;
	}

	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getDefaultMsg() {
		return defaultMsg;
	}
	public void setDefaultMsg(String defaultMsg) {
		this.defaultMsg = defaultMsg;
	}
}
