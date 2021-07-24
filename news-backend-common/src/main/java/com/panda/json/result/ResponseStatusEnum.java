package com.panda.json.result;

public enum ResponseStatusEnum {
    SUCCESS(200, true, "Operation success！"),
    FAILED(500, false, "Operation failed！"),

    // 50x
    UN_LOGIN(501,false,"Please log in before proceeding！"),
    TICKET_INVALID(502,false,"The session is invalid, please log in again！"),
    NO_AUTH(503,false,"You do not have enough permissions to continue！"),
    MOBILE_ERROR(504,false,"SMS sending failed, please try again later！"),
    SMS_NEED_WAIT_ERROR(505,false,"The text message is sent too fast, please try again later！"),
    SMS_CODE_ERROR(506,false,"Verification code expired or does not match, please try again later！"),
    USER_FROZEN(507,false,"The user has been frozen, please contact the administrator！"),
    USER_UPDATE_ERROR(508,false,"User information update failed, please contact the administrator！"),
    USER_INACTIVE_ERROR(509,false,"Please go to [Account Settings] to modify the information and activate it before proceeding！"),
    FILE_UPLOAD_NULL_ERROR(510,false,"The file cannot be empty, please select a file and upload it！"),
    FILE_UPLOAD_FAILD(511,false,"File upload failed！"),
    FILE_FORMATTER_FAILD(512,false,"File image format is not supported！"),
    FILE_MAX_SIZE_ERROR(513,false,"Only support the upload of images below 500kb in size！"),
    FILE_NOT_EXIST_ERROR(514,false,"The file you are viewing does not exist！"),
    USER_STATUS_ERROR(515,false,"User status parameter error！"),
    USER_NOT_EXIST_ERROR(516,false,"User does not exist！"),
    FILE_CREATE_FAILD(511,false,"File create failed！"),

    // system level exception 54x
    SYSTEM_INDEX_OUT_OF_BOUNDS(541, false, "System error, array out of bounds！"),
    SYSTEM_ARITHMETIC_BY_ZERO(542, false, "System error, unable to divide by zero！"),
    SYSTEM_NULL_POINTER(543, false, "System error, null pointer！"),
    SYSTEM_NUMBER_FORMAT(544, false, "System error, abnormal number conversion！"),
    SYSTEM_PARSE(545, false, "System error, parsing exception！"),
    SYSTEM_IO(546, false, "System error, abnormal IO input and output！"),
    SYSTEM_FILE_NOT_FOUND(547, false, "System error, file not found！"),
    SYSTEM_CLASS_CAST(548, false, "System error, type coercion error！"),
    SYSTEM_PARSER_ERROR(549, false, "System error, parsing error！"),
    SYSTEM_DATE_PARSER_ERROR(550, false, "System error, date parsing error！"),

    // admin system exception 56x
    ADMIN_USERNAME_NULL_ERROR(561, false, "Administrator login name cannot be empty！"),
    ADMIN_USERNAME_EXIST_ERROR(562, false, "Administrator login already exists！"),
    ADMIN_NAME_NULL_ERROR(563, false, "The admin person in charge cannot be empty\n！"),
    ADMIN_PASSWORD_ERROR(564, false, "The password cannot be empty and the latter two input are inconsistent！"),
    ADMIN_CREATE_ERROR(565, false, "Failed to add administrator！"),
    ADMIN_PASSWORD_NULL_ERROR(566, false, "The password can not be blank！"),
    ADMIN_NOT_EXIST_ERROR(567, false, "The administrator does not exist or the password is wrong！"),
    ADMIN_FACE_NULL_ERROR(568, false, "Face information cannot be empty！"),
    ADMIN_FACE_LOGIN_ERROR(569, false, "Face recognition failed, please try again！"),
    CATEGORY_EXIST_ERROR(570, false, "The article category already exists, please change a category name！"),

    // Article center exception 58x
    ARTICLE_COVER_NOT_EXIST_ERROR(580, false, "Article cover does not exist, please select one！"),
    ARTICLE_CATEGORY_NOT_EXIST_ERROR(581, false, "Please select the correct article area！"),
    ARTICLE_CREATE_ERROR(582, false, "Failed to create article, please try again or contact the administrator！"),
    ARTICLE_QUERY_PARAMS_ERROR(583, false, "Article list query parameter error！"),
    ARTICLE_DELETE_ERROR(584, false, "Article deletion failed！"),
    ARTICLE_WITHDRAW_ERROR(585, false, "Failed to retract the article！"),
    ARTICLE_REVIEW_ERROR(585, false, "Article review error！"),
    ARTICLE_ALREADY_READ_ERROR(586, false, "Repeat article reading！"),
    ARTICLE_MONGO_FILE_ID_UPDATE_ERROR(587, false, "Article mongodb id update failed！"),

    // Face verification exception
    FACE_VERIFY_TYPE_ERROR(600, false, "The face verification type is incorrect！"),
    FACE_VERIFY_LOGIN_ERROR(601, false, "Face login failed！"),

    // Unknown system exception 555
    SYSTEM_ERROR(555, false, "The system is busy, please try again later！"),
    SYSTEM_OPERATION_ERROR(556, false, "The operation failed, please try again or contact the administrator"),
    SYSTEM_RESPONSE_NO_INFO(557, false, "");

    // response status
    private Integer status;
    // response success/failed
    private Boolean success;
    // response message
    private String msg;

    ResponseStatusEnum(Integer status, Boolean success, String msg) {
        this.status = status;
        this.success = success;
        this.msg = msg;
    }

    public Integer status() {
        return status;
    }
    public Boolean success() {
        return success;
    }
    public String msg() {
        return msg;
    }
}
