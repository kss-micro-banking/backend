package com.kss.backend.user.agent;

import java.util.Set;

import com.kss.backend.branch.Branch;
import com.kss.backend.user.User;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

/**
 * Agent
 */
@Entity
@DiscriminatorValue("AGENT")
public class Agent extends User {

  @OneToMany
  @JoinColumn(name = "agent_branch_ids")
  private Set<Branch> branches;

  public Set<Branch> getBranches() {
    return branches;
  }

  public void setBranches(Set<Branch> branches) {
    this.branches = branches;
  }
}
