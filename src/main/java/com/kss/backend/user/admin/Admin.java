package com.kss.backend.user.admin;

import com.kss.backend.user.User;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Admin
 */
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    private boolean isSuperAdmin;

    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(boolean isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }
}
