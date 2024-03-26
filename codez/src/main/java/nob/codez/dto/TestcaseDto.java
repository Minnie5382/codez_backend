package nob.codez.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TestcaseDto {
    private int caseNumber;
    private String input;
    private String output;
}