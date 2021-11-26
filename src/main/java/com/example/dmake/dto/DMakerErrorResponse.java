package com.example.dmake.dto;


import com.example.dmake.exception.DeveloperErrorCode;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DMakerErrorResponse {
    private DeveloperErrorCode errorCode;
    private String errorMessage;
}
