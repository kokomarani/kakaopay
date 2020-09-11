package com.kakaopay.project.vo.response;

import lombok.Data;

@Data
public class APIResponse<T> {
    private String code;
    private String message;
}
