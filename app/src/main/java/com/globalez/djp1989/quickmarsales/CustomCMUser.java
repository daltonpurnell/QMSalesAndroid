package com.globalez.djp1989.quickmarsales;

import com.cloudmine.api.CMUser;

/**
 * Created by djp1989 on 3/15/16.
 */
class CustomCMUser extends CMUser {
    private String usernameEmail;

    /**
     * Need a no-args constructor for proper deserialization
     */
    public CustomCMUser() {
        super();
    }
    public CustomCMUser(String email, String password, String usernameEmailAddress) {
        super(email, password);
        this.usernameEmail = usernameEmailAddress;

    }

    public String getUsernameEmail() {
        return usernameEmail;
    }
    public void setUsernameEmail(String usernameEmail1) {
        this.usernameEmail = usernameEmail1;
    }

}
