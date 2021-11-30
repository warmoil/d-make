package com.example.dmake.controller;

import com.example.dmake.dto.*;
import com.example.dmake.exception.DeveloperMakerException;
import com.example.dmake.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DMakerController {

    private final DeveloperService developerService;

    @GetMapping("/developers")
    public List<DeveloperDto> getAllDev() {

        return developerService.getAllEmployedDevelopers();
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDto getDetail(@PathVariable final String memberId) {
        return developerService.getDeveloperDetail(memberId);
    }
    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDevelopers(@Valid @RequestBody final CreateDeveloper.Request request) {
        log.info("request : {}",request);
        return developerService.createDeveloper(request);
    }

    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto editDeveloper(@PathVariable final String memberId , @Valid @RequestBody final EditDeveloper.Request request) {
        return developerService.editDeveloper(memberId, request);
    }
    @DeleteMapping ("/developer/{memberId}")
    public DeveloperDetailDto editDeveloper(@PathVariable final String memberId) {
        return developerService.deleteDeveloper(memberId);
    }



}
