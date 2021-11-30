package com.example.dmake.controller;

import com.example.dmake.dto.DeveloperDto;
import com.example.dmake.model.DeveloperLevel;
import com.example.dmake.model.DeveloperSkillType;
import com.example.dmake.service.DeveloperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;

import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DMakerController.class)
//@MockBean(JpaMetamodelMappingContext.class)
class DMakerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeveloperService developerService;


    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype()
            ,StandardCharsets.UTF_8);

    @Test
    void getAllDevelopers() throws Exception {
        DeveloperDto newBieDev = DeveloperDto.builder()
                .developerLevel(DeveloperLevel.NEW)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(2)
                .memberId("mem1")
                .build();
        DeveloperDto seniorDev = DeveloperDto.builder()
                .developerLevel(DeveloperLevel.SENIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(12)
                .memberId("mem2")
                .build();
        given(developerService.getAllEmployedDevelopers())
                .willReturn(Arrays.asList(newBieDev,seniorDev));

        mockMvc.perform(get("/developers").contentType(contentType)).andExpect(status().isOk())
                .andExpect (jsonPath("$.[0].developerSkillType",is(DeveloperSkillType.FRONT_END.name())))
                .andExpect(jsonPath("$[1].developerLevel",is(DeveloperLevel.SENIOR.name()))).andDo(print());

    }

}