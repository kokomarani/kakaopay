package com.kakaopay.project.vo.response;


import com.kakaopay.project.entity.SprinkleMoneyEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@RequiredArgsConstructor
public class SprinkleMoneyResponse extends APIResponse {
    private SprinkleMoneyEntity sprinkleMoney;

}
