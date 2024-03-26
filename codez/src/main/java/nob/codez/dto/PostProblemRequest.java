package nob.codez.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import nob.codez.enums.Difficulty;
import nob.codez.enums.ProblemType;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostProblemRequest {
    @Schema(example = "DP 문제 !!!")
    private String title;
    @Schema(example = "아래와 같이 5와 사칙연산만으로 12를 표현할 수 있습니다.\n" +
            "\n" +
            "12 = 5 + 5 + (5 / 5) + (5 / 5)\n" +
            "12 = 55 / 5 + 5 / 5\n" +
            "12 = (55 + 5) / 5\n" +
            "\n" +
            "5를 사용한 횟수는 각각 6,5,4 입니다. 그리고 이중 가장 작은 경우는 4입니다.\n" +
            "이처럼 숫자 N과 number가 주어질 때, N과 사칙연산만 사용해서 표현 할 수 있는 방법 중 N 사용횟수의 최솟값을 return 하도록 solution 함수를 작성하세요.\n" +
            "\n" +
            "제한사항\n" +
            "N은 1 이상 9 이하입니다.\n" +
            "number는 1 이상 32,000 이하입니다.\n" +
            "수식에는 괄호와 사칙연산만 가능하며 나누기 연산에서 나머지는 무시합니다.\n" +
            "최솟값이 8보다 크면 -1을 return 합니다.")
    private String content;
    @Schema(example = "NORMAL")
    private Difficulty difficulty;
    @Schema(example = "1000")
    private int timeLimit;
    @Schema(example = "20")
    private int exp;
    @Schema(example = "DP")
    private ProblemType problemType;
    @Schema(example = "class Solution {\n" +
            "    public int solution(int N, int number) {\n" +
            "        int answer = 0;\n" +
            "        return answer;\n" +
            "    }\n" +
            "}")
    private String inputFormatJava;
    @Schema(example = "[\n" +
            "\t\t{\n" +
            "\t\t\t\"caseNumber\": 1,\n" +
            "\t\t\t\"input\": \"[1, 2, 3, 4, 5]\",\n" +
            "\t\t\t\"output\": \"15\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"caseNumber\": 2,\n" +
            "\t\t\t\"input\": \"[10, 20, 30]\",\n" +
            "\t\t\t\"output\": \"60\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"caseNumber\": 3,\n" +
            "\t\t\t\"input\": \"[-1, -2, -3, -4, -5]\",\n" +
            "\t\t\t\"output\": \"-15\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"caseNumber\": 4,\n" +
            "\t\t\t\"input\": \"[0, 0, 0, 0, 0]\",\n" +
            "\t\t\t\"output\": \"0\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"caseNumber\": 5,\n" +
            "\t\t\t\"input\": \"[100, 200, 300]\",\n" +
            "\t\t\t\"output\": \"600\"\n" +
            "\t\t}\n" +
            "\t]")
    private List<TestcaseDto> testcaseDtoList;

}
