package com.kakaopay.project.vo.response;


import com.kakaopay.project.entity.PickUpMoneyEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@RequiredArgsConstructor
public class PickUpMoneyResponse extends APIResponse {
    private PickUpMoneyEntity pickUpMoney;

}
