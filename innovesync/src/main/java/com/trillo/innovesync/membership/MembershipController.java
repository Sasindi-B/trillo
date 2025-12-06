package com.trillo.innovesync.membership;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trillo.innovesync.auth.Role;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @GetMapping("/packages")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ResponseEntity<List<MembershipPackageResponse>> getAvailablePackages() {
        return ResponseEntity.ok(membershipService.getAvailablePackages());
    }

    @PostMapping("/purchase")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ResponseEntity<MembershipResponse> purchaseMembership(
            @Valid @RequestBody PurchaseMembershipRequest request) {
        // Validate role from JWT token
        Role userRole = getCurrentUserRole();
        membershipService.validateRoleIsBusinessOwner(userRole);

        MembershipResponse response = membershipService.purchaseMembership(
                request.getBusinessOwnerId(),
                request.getPackageType());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{businessOwnerId}/current")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ResponseEntity<MembershipResponse> getCurrentMembership(@PathVariable String businessOwnerId) {
        // Validate role from JWT token
        Role userRole = getCurrentUserRole();
        membershipService.validateRoleIsBusinessOwner(userRole);

        return membershipService.getCurrentMembership(businessOwnerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<List<MembershipResponse>> getAllMemberships() {
        return ResponseEntity.ok(membershipService.getAllMemberships());
    }

    private Role getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().isEmpty()) {
            throw new IllegalStateException("User not authenticated");
        }

        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        String authorityString = authority.getAuthority();

        // Extract role from "ROLE_BUSINESS_OWNER" format
        if (authorityString.startsWith("ROLE_")) {
            String roleName = authorityString.substring(5);
            try {
                return Role.valueOf(roleName);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Unknown role: " + roleName);
            }
        }

        throw new IllegalStateException("Invalid authority format: " + authorityString);
    }
}
