package com.kss.backend.util;

/**
 * API
 */
public interface Api {
    String Version = "v1";

    public interface Routes {
        String BASE_URL = "/api/" + Version;

        String USERS = BASE_URL + "/users";
        String ADMIN = USERS + "/admin";
        String AGENT = USERS + "/agent";

        String BRANCH = BASE_URL + "/branch";
        String CUSTOMER = BASE_URL + "/customer";

    }
}
