package com.example.dmake.service;

import com.example.dmake.code.StatusCode;
import com.example.dmake.constant.DMakerConstant;
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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DeveloperService {

    @Value("${developer.level.min.senior}")
    private final Integer minSeniorYear;
    private final DeveloperRepository developerRepository;
    //private final EntityManager em;
    private final RetiredDeveloperRepository retiredDeveloperRepository;
    //ACID
    //Atomic
    //Consistency
    //Isolation
    //Durability

    private Developer from(CreateDeveloper.Request request) {
        return Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .memberId(request.getMemberId())
                .name(request.getName())
                .age(request.getAge())
                .statusCode(StatusCode.EMPLOYED)
                .build();
    }

    @Transactional
    public CreateDeveloper.Response createDeveloper(@NonNull CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        try {
            return CreateDeveloper
                    .Response
                    .fromEntity(developerRepository
                            .save(from(request)));
            //business logic end
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private void validateCreateDeveloperRequest(@NonNull CreateDeveloper.Request request) {

        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        developerRepository.findByMemberId(request.getMemberId()).ifPresent(developer -> {
                    throw new DeveloperMakerException(DeveloperErrorCode.DUPLICATED_MEMBER_ID);
                }
        );
    }

    private void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {

        developerLevel.validateExperienceYears(experienceYears);

    }

    @Transactional(readOnly = true)
    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDeveloperByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream()
                .map(DeveloperDto::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return DeveloperDetailDto.fromEntity(getDeveloperByMemberId(memberId));
    }

    private Developer getDeveloperByMemberId(String memberId) {
       return developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DeveloperMakerException(DeveloperErrorCode.NO_DEVELOPER));
    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateEditDeveloperLevel(request);

        return DeveloperDetailDto.fromEntity(
                updatedDeveloperForm(request,getDeveloperByMemberId(memberId))
        );
    }

    private Developer updatedDeveloperForm(EditDeveloper.Request request, Developer developer) {
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return developer;
    }

    private void validateEditDeveloperLevel(EditDeveloper.Request request) {
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

    }


    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        //EMPLOYED -> RETIRED
        // save into RetiredDeveloper
        Developer developer = getDeveloperByMemberId(memberId);
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
