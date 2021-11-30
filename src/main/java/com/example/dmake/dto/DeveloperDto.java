package com.example.dmake.dto;

import com.example.dmake.model.Developer;
import com.example.dmake.model.DeveloperLevel;
import com.example.dmake.model.DeveloperSkillType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DeveloperDto {
    private DeveloperLevel developerLevel;
    private DeveloperSkillType developerSkillType;
    private String memberId;
    private Integer experienceYears;

    public static DeveloperDto fromEntity(Developer developer) {
        return DeveloperDto.builder()
                .developerSkillType(developer.getDeveloperSkillType())
                .memberId(developer.getMemberId())
                .developerLevel(developer.getDeveloperLevel())
                .experienceYears(developer.getExperienceYears())
                .build();
    }

}
