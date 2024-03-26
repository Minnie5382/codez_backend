package nob.codez.config.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),

    // user
    INVALID_USER(false,2010,"우리 DB에 그런 유저 없음."),
    INVALID_ID_PASSWORD(false, 2011, "아이디, 비밀번호 틀림"),
    ALREADY_LOGGED_IN_USER(false, 2012, "이미 이 세션으로는 로그인 했음."),
    NOT_LOGGED_IN_USER(false, 2013, "로그인 먼저 하셈."),

    // join
    USER_DUPLICATED_EMAIL(false, 2020, "이메일 중복!!"),
    USER_DUPLICATED_NICKNAME(false, 2021, "닉네임 중복!!"),

    // file
   FILE_NOT_IMAGE(false, 2030, "이미지 파일만 올려야됨"),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
    INVALID_PROBLEM(false, 3010, "그런 문제 없음."),

    // [Quest]
    QUESTS_IS_EMPTY(false,3030,"퀘스트 목록이 비어있습니다."),
    QUESTS_IS_NOT_CLEAR(false,3031,"퀘스트 완료 조건이 충족되지 않았습니다."),

    // [Level]
    EXP_IS_NOT_MAX(false, 3032, "경험치가 부족합니다"),


    //[problem]
    SOKET_CONNECT_FAIL(false,3060,"소켓 연결 실패."),
    CODE_COMPILE_FAIL(false,3061,"전달 받은 코드의 컴파일이 실패했습니다"),

    /**
     * 4000 : Database, Server 오류
     */
    FILE_UPLOAD_FAIL(false, 4001, "파일 업로드에 실패했습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}