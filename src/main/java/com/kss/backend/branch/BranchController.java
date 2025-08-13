package com.kss.backend.branch;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kss.backend.branch.dto.BranchCreateDto;
import com.kss.backend.util.Api;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * BranchController
 */
@RestController
@RequestMapping(Api.Routes.BRANCH)
@PreAuthorize("hasAnyRole('ADMIN','AGENT')")
@Tag(name = "Branch Endpoints")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<Branch> create(@Valid @RequestBody BranchCreateDto dto) {
        Branch branch = branchService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(branch);
    }

    @GetMapping
    ResponseEntity<List<Branch>> findAll() {
        var branches = branchService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(branches);
    }

    @GetMapping("/{id}")
    ResponseEntity<Branch> findById(@PathVariable UUID id) {
        var branch = branchService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(branch);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    ResponseEntity<Void> delete(@RequestParam UUID id) {
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
