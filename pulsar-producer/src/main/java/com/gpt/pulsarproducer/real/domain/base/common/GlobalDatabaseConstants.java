package com.gpt.pulsarproducer.real.domain.base.common;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalDatabaseConstants
{
    // AUDITING fields
    public static final String COL_ENTRY_USER = "entry_user";
    public static final String COL_ENTRY_DATE = "entry_dttm";
    public static final String COL_UPDATE_USER = "update_user";
    public static final String COL_UPDATE_DATE = "update_dttm";

    public static final int ENTRY_UPDATE_USER_LENGTH = 100;
    public static final int EMAIL_ADDR_LENGTH = 100;
    public static final int FIRST_NAME_LENGTH = 50;
    public static final int LAST_NAME_LENGTH = 50;
    public static final int MIDDLE_NAME_LENGTH = 50;
    public static final int USERNAME_LENGTH = 100;
    public static final int PHONE_NR_LENGTH = 10;
    public static final int USER_STATUS_LENGTH = 50;

    // user table columns
    public static final String COL_USER_ID = "user_id";
    public static final String COL_ROLE_ID = "role_id";
    public static final String COL_PRIVILEGE_ID = "privilege_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_EMAIL = "email";
    public static final String COL_NOTIFICATION_EMAIL = "notification_email";
    public static final String COL_MOBILE_PHONE_NUMBER = "phone_number";
    public static final String COL_USER_STATUS = "user_status";

    // error log table constants
    public static final String TBL_ERROR_LOG = "error_log";
    public static final String COL_ERROR_LOG_ID = "error_log_id";

    // general
    public static final String SYSTEM_ACCOUNT_USERNAME = "system";

    // sorting
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";
}
