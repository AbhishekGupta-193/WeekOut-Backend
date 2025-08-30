package com.weekout.backend.Services;

import com.weekout.backend.DTOs.*;
import com.weekout.backend.Model.Plan;
import com.weekout.backend.Model.User;
import com.weekout.backend.Repositories.PlanRepository;
import com.weekout.backend.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    public PlanService(PlanRepository planRepository, UserRepository userRepository) {
        this.planRepository = planRepository;
        this.userRepository = userRepository;
    }
//    public PlanService(PlanRepository planRepository) {
//        this.planRepository = planRepository;
//    }

//    public Plan createPlan(Plan plan) {
//        plan.setEntryMode(Plan.EntryMode.HOSTED);  // Host creates the plan
//
//        return planRepository.save(plan);
//    }

    public Plan createPlan(Plan plan, UUID hostUserId) {
        User host = userRepository.findById(hostUserId)
                .orElseThrow(() -> new RuntimeException("Host user not found"));

        plan.setHostUser(host);
        plan.setHostId(host.getId());
        plan.setEntryMode(Plan.EntryMode.HOSTED);

        return planRepository.save(plan);
    }

    // Add a user as joinedUser
    public Plan joinPlan(UUID planId, UUID userId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (plan.getJoinedUsers().size() >= plan.getMaxMembers()) {
            throw new RuntimeException("Plan is full");
        }

        // ✅ Ensure user is not already in the set
        if (!plan.getJoinedUsers().contains(user)) {
            plan.getJoinedUsers().add(user);
        }
        return planRepository.save(plan);
    }

    // Remove a user from joinedUsers
    public Plan leavePlan(UUID planId, UUID userId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        plan.getJoinedUsers().remove(user);
        return planRepository.save(plan);
    }

    // Get public plans with optional filters
    public List<Plan> getPublicPlans(String type, LocalDate date, List<String> tags, String location) {
        // TODO: Implement filtering logic - basic example below fetches all PUBLIC plans only

        // For now, simple example ignoring filters:
        return planRepository.findAll().stream()
                .filter(p -> p.getVisibility() == Plan.Visibility.PUBLIC)
                .filter(p -> type == null || (p.getType() != null && p.getType().equalsIgnoreCase(type)))
                .filter(p -> date == null || p.getDate().equals(date))
                .filter(p -> location == null ||
                        (p.getMeetupPoint() != null && p.getMeetupPoint().toLowerCase().contains(location.toLowerCase())))
                .filter(p -> {
                    if (tags == null || tags.isEmpty()) return true;
                    if (p.getTags() == null) return false;
                    return p.getTags().stream().anyMatch(tags::contains);
                })
                .collect(Collectors.toList());
    }

    // Get plan details by ID
    public Plan getPlanById(UUID id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
    }

    // Update plan (host only check must be done here)
    public Plan updatePlan(UUID id, Plan updatedPlan) {
        Plan existingPlan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        // TODO: Host-only check - you must verify updatedPlan.getHostId() or pass userId from auth/session

        existingPlan.setTitle(updatedPlan.getTitle());
        existingPlan.setType(updatedPlan.getType());
        existingPlan.setDate(updatedPlan.getDate());
        existingPlan.setTime(updatedPlan.getTime());
        existingPlan.setMeetupPoint(updatedPlan.getMeetupPoint());
        existingPlan.setDescription(updatedPlan.getDescription());
        existingPlan.setTags(updatedPlan.getTags());
        existingPlan.setMaxMembers(updatedPlan.getMaxMembers());
        existingPlan.setVisibility(updatedPlan.getVisibility());
        existingPlan.setHostUser(existingPlan.getHostUser()); // keep unchanged
        existingPlan.setHostId(existingPlan.getHostUser().getId());
        // Usually, you don't change createdAt or entryMode on update

        return planRepository.save(existingPlan);
    }

    // Delete plan (host only check must be done here)
    public void deletePlan(UUID id) {
        Plan existingPlan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        // TODO: Host-only check - verify if current user is host

        planRepository.delete(existingPlan);
    }

}

