package com.kynguyen.shop_3hkt.Prevalent;

import com.kynguyen.shop_3hkt.Model.Role;
import com.kynguyen.shop_3hkt.Model.User;

public class Prevalent {
    public static User currentOnLineUsers;

    public static Role roleUser;
    public static final String Userphonekey = "UserPhone";
    public static final String Userpasswordkey = "UserPassword";

    public static User getCurrentOnLineUsers() {
        return currentOnLineUsers;
    }

    public static void setCurrentOnLineUsers(User currentOnLineUsers) {
        Prevalent.currentOnLineUsers = currentOnLineUsers;
    }
}
