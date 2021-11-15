package com.incentro.myservice.config;

public class TestCredentialsUtility {
    private static TestCredentialsUtility singleInstance = null;

    private TestCredentialsUtility() {
    }

    public static TestCredentialsUtility getInstance() {
        if (singleInstance == null) {
            singleInstance = new TestCredentialsUtility();
        }
        return singleInstance;
    }

    public StringBuilder administrator_user_1_token =  new StringBuilder();
    public StringBuilder member_user_2_token =  new StringBuilder();

    public final String ADMINISTRATOR_USER_1_USERNAME =  "user1@dev.com";
    public final String MEMEBER_USER_2_USERNAME =  "user2@dev.com";

    public String getAdministrator_user_1_token() {
        return administrator_user_1_token.toString();
    }

    public String getMember_user_2_token() {
        return member_user_2_token.toString();
    }
}
