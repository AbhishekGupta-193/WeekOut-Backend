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

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public Plan createPlan(Plan plan) {
        plan.setEntryMode(Plan.EntryMode.HOSTED);  // Host creates the plan

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

