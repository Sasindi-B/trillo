package com.trillo.innovesync.membership;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.trillo.innovesync.auth.Role;
import com.trillo.innovesync.businessowner.BusinessOwner;
import com.trillo.innovesync.businessowner.BusinessOwnerRepository;
import com.trillo.innovesync.exception.InvalidCredentialsException;

@Service
public class MembershipService {

    private static final int MEMBERSHIP_DURATION_DAYS = 30;

    private final MembershipRepository membershipRepository;
    private final BusinessOwnerRepository businessOwnerRepository;

    public MembershipService(MembershipRepository membershipRepository,
            BusinessOwnerRepository businessOwnerRepository) {
        this.membershipRepository = Objects.requireNonNull(membershipRepository, "membershipRepository");
        this.businessOwnerRepository = Objects.requireNonNull(businessOwnerRepository, "businessOwnerRepository");
    }

    public List<MembershipPackageResponse> getAvailablePackages() {
        return List.of(
                new MembershipPackageResponse(MembershipPackage.SILVER),
                new MembershipPackageResponse(MembershipPackage.GOLD));
    }

    public MembershipResponse purchaseMembership(String businessOwnerId, MembershipPackage packageType) {
        Objects.requireNonNull(businessOwnerId, "businessOwnerId");
        Objects.requireNonNull(packageType, "packageType");

        // Validate business owner exists
        BusinessOwner owner = businessOwnerRepository.findById(businessOwnerId)
                .orElseThrow(() -> new IllegalArgumentException("Business owner not found: " + businessOwnerId));

        // Check for existing membership
        Optional<Membership> existingOpt = membershipRepository.findByBusinessOwnerIdAndIsActiveTrue(businessOwnerId);

        if (existingOpt.isPresent()) {
            Membership existing = existingOpt.get();

            if (existing.getPackageType() == packageType) {
                // Same package - extend membership
                existing.extendMembership(MEMBERSHIP_DURATION_DAYS);
                existing.touchUpdatedAt();
                Membership saved = membershipRepository.save(existing);
                return new MembershipResponse(saved);
            } else if (packageType.isUpgradeFrom(existing.getPackageType())) {
                // Upgrading Silver -> Gold - start immediately
                existing.setPackageType(packageType);
                Instant now = Instant.now();
                existing.setStartDate(now);
                existing.setExpiryDate(now.plusSeconds(MEMBERSHIP_DURATION_DAYS * 24L * 60 * 60));
                existing.touchUpdatedAt();
                Membership saved = membershipRepository.save(existing);
                return new MembershipResponse(saved);
            } else {
                // Downgrading or invalid transition
                throw new IllegalArgumentException(
                        "Cannot downgrade from " + existing.getPackageType() + " to " + packageType
                                + ". Please wait for current membership to expire.");
            }
        } else {
            // New membership
            Instant now = Instant.now();
            Instant expiry = now.plusSeconds(MEMBERSHIP_DURATION_DAYS * 24L * 60 * 60);
            Membership membership = new Membership(businessOwnerId, packageType, now, expiry);
            Membership saved = membershipRepository.save(membership);
            return new MembershipResponse(saved);
        }
    }

    public Optional<MembershipResponse> getCurrentMembership(String businessOwnerId) {
        Objects.requireNonNull(businessOwnerId, "businessOwnerId");

        return membershipRepository.findByBusinessOwnerIdAndIsActiveTrue(businessOwnerId)
                .map(MembershipResponse::new);
    }

    public List<MembershipResponse> getAllMemberships() {
        return membershipRepository.findAllByIsActiveTrue().stream()
                .map(MembershipResponse::new)
                .collect(Collectors.toList());
    }

    public void validateRoleIsBusinessOwner(Role role) {
        if (role != Role.BUSINESS_OWNER) {
            throw new InvalidCredentialsException("Only business owners can access membership features");
        }
    }
}
