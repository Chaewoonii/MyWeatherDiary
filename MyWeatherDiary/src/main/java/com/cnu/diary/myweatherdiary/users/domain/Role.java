package com.cnu.diary.myweatherdiary.users.domain;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    public static Role getRole(int num){
        Role role = null;
        switch (num){
            case 1:
                role = ROLE_USER;
            case 2:
                role = ROLE_ADMIN;
        }
        return role;
    }
}
