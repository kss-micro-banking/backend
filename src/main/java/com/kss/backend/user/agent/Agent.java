package com.kss.backend.user.agent;

import com.kss.backend.branch.Branch;
import com.kss.backend.user.User;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

/**
 * Agent
 */
@Entity
@DiscriminatorValue("AGENT")
public class Agent extends User {

    @OneToOne
    private Branch branch;

}
