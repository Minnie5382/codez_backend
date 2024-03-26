package nob.codez.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetProblemsResponse {
    @Schema(example = "[\n" +
            "            {\n" +
            "                \"problemId\": 1,\n" +
            "                \"title\": \"총합 구하기\",\n" +
            "                \"solvingStatus\": \"NONE\",\n" +
            "                \"correctRate\": 0,\n" +
            "                \"difficulty\": \"EASY\",\n" +
            "                \"submitNum\": 0\n" +
            "            }\n" +
            "        ]")
    private List<ProblemInfo> problemList;
    @Schema(example = "0")
    private int totalPageNum; // 문제 수
}
