package com.example.dmake.model;

import com.example.dmake.constant.DMakerConstant;
import com.example.dmake.exception.DeveloperErrorCode;
import com.example.dmake.exception.DeveloperMakerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;


@AllArgsConstructor
@Getter
public enum DeveloperLevel {

    NEW("신입 개발자", years -> years == 0),
    JUNIOR("주니어 개발자", years -> years <= DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS),
    JUNGNIOR("중니어 개발자", y -> y > DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS || y < DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS),
    SENIOR("시니어 개발자", y -> y >= DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS);


    private final String description;
    private final Function<Integer, Boolean> validateFunction;

    public void validateExperienceYears(Integer years) {
        if (!validateFunction.apply(years)) {
            throw new DeveloperMakerException(DeveloperErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

}
