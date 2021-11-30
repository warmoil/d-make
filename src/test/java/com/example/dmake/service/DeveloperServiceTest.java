package com.example.dmake.service;

import com.example.dmake.code.StatusCode;
import com.example.dmake.dto.CreateDeveloper;
import com.example.dmake.dto.DeveloperDetailDto;
import com.example.dmake.exception.DeveloperErrorCode;
import com.example.dmake.exception.DeveloperMakerException;
import com.example.dmake.model.Developer;
import com.example.dmake.model.DeveloperLevel;
import com.example.dmake.model.DeveloperSkillType;
import com.example.dmake.repository.DeveloperRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

  /*  @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;*/
    @InjectMocks
    private DeveloperService developerService;

    private final Developer developer = Developer.builder()
                .developerLevel(DeveloperLevel.NEW)
                .developerSkillType( DeveloperSkillType.FRONT_END)
                .experienceYears(3)
                .age(22)
                .memberId("memberId")
                .name("joy")
                .statusCode(StatusCode.EMPLOYED)
                .build();
    private final CreateDeveloper.Request devReq = CreateDeveloper.Request.builder()
            .developerLevel(DeveloperLevel.SENIOR)
            .developerSkillType( DeveloperSkillType.FRONT_END)
            .experienceYears(3)
            .age(22)
            .memberId("memberId")
            .name("joy")
            .build();


    @Test
    public void firstTest() {

        given(developerRepository.findByMemberId(anyString())).willReturn(Optional.of(Developer.builder()
                .developerLevel(DeveloperLevel.NEW)
                .developerSkillType( DeveloperSkillType.FRONT_END)
                .experienceYears(3)
                .age(22)
                .memberId("memberId")
                .name("joy")
                .statusCode(StatusCode.EMPLOYED)
                .build()));

        DeveloperDetailDto developerDetailDto = developerService.getDeveloperDetail("memberId");
        assertEquals(DeveloperLevel.NEW,developerDetailDto.getDeveloperLevel());
    }

    @Test
    void createDeveloperTest_success() {

        //given
        CreateDeveloper.Request request = CreateDeveloper.Request.builder()
                .memberId("123")
                .experienceYears(11)
                .developerLevel(DeveloperLevel.SENIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .age(33)
                .build();
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        given(developerRepository.save(any()))
                .willReturn(developer);
        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);

        //when
        CreateDeveloper.Response dev = developerService.createDeveloper(request);
        //then
        verify(developerRepository, times(1))
                .save(captor.capture());
        Developer saveDev = captor.getValue();
        assertEquals(DeveloperLevel.SENIOR, saveDev.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, saveDev.getDeveloperSkillType());

    }
 /*   @Test
    void createDeveloperTest_failed_with_duplicated() {

        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());

        //when

        //then
        DeveloperMakerException developerMakerException = assertThrows(DeveloperMakerException.class, () -> developerService.createDeveloper(devReq));

        assertEquals(DeveloperErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED , developerMakerException.getDeveloperErrorCode());

    }*/

    @Test
    void createDeveloperTest_fail_unmatched_level() {


        CreateDeveloper.Request request = devReq;

        request.setExperienceYears(1);
        System.out.println(request==devReq);
        DeveloperMakerException developerMakerException = assertThrows(DeveloperMakerException.class, () ->
                developerService.createDeveloper(request));
        assertEquals(DeveloperErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED,developerMakerException.getDeveloperErrorCode());
    }


}