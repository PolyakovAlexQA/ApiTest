package domain;

import lombok.Value;

@Value
public class UserInfo {

        private String login;
        private String password;
        private String status;
    }

