package com.example.dmake.service;

import com.example.dmake.code.StatusCode;
import com.example.dmake.dto.CreateDeveloper;
import com.example.dmake.dto.DeveloperDetailDto;
import com.example.dmake.dto.DeveloperDto;
import com.example.dmake.dto.EditDeveloper;
import com.example.dmake.exception.DeveloperErrorCode;
import com.example.dmake.exception.DeveloperMakerException;
import com.example.dmake.model.Developer;
import com.example.dmake.model.DeveloperLevel;
import com.example.dmake.model.RetiredDeveloper;
import com.example.dmake.repository.DeveloperRepository;
import com.example.dmake.repository.RetiredDeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final DeveloperRepository developerRepository;
    private final EntityManager em;
    private final RetiredDeveloperRepository retiredDeveloperRepository;
    //ACID
    //Atomic
    //Consistency
    //Isolation
    //Durability

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        try{
            //business logic start
            Developer developer = Developer.builder()
                    .developerLevel(request.getDeveloperLevel())
                    .developerSkillType(request.getDeveloperSkillType())
                    .experienceYears(request.getExperienceYears())
                    .memberId(request.getMemberId())
                    .name(request.getName())
                    .age(request.getAge())
                    .statusCode(StatusCode.EMPLOYED)
                    .build();

            developerRepository.save(developer);


            return CreateDeveloper.Response.fromEntity(developer);
            //business logic end
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {

        validateDeveloperLevel(request.getDeveloperLevel(),request.getExperienceYears());

        developerRepository.findByMemberId(request.getMemberId()).ifPresent(developer -> {
                    throw new DeveloperMakerException(DeveloperErrorCode.DUPLICATED_MEMBER_ID);
                }
        );

    }

    private void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        if (developerLevel == DeveloperLevel.SENIOR && experienceYears < 10) {
            throw new DeveloperMakerException(DeveloperErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        if (developerLevel == DeveloperLevel.JUNIOR && (experienceYears < 3 || experienceYears > 10)) {
            throw new DeveloperMakerException(DeveloperErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNIOR && experienceYears > 4) {
            throw new DeveloperMakerException(DeveloperErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDeveloperByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream()
                .map(DeveloperDto::fromEntity).collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId).map(DeveloperDetailDto::fromEntity).orElseThrow(()->
            new DeveloperMakerException(DeveloperErrorCode.NO_DEVELOPER));
    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId , EditDeveloper.Request request) {
        validateEditDeveloperLevel(request,memberId);
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(()->new DeveloperMakerException(DeveloperErrorCode.NO_DEVELOPER));
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(developer);
    }

    private void validateEditDeveloperLevel(EditDeveloper.Request request, String memberId) {
        validateDeveloperLevel(request.getDeveloperLevel(),request.getExperienceYears());

    }


    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        //EMPLOYED -> RETIRED
        // save into RetiredDeveloper
        Developer developer = developerRepository.findByMemberId(memberId).orElseThrow(()->
                new DeveloperMakerException(DeveloperErrorCode.NO_DEVELOPER));
        developer.setStatusCode(StatusCode.RETIRED);
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .developerSkillType(developer.getDeveloperSkillType())
                .developerLevel(developer.getDeveloperLevel())
                .build();
        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDetailDto.fromEntity(developer);
    }
}
