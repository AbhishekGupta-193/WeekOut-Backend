package com.weekout.backend.Controllers;
import com.weekout.backend.DTOs.*;
import com.weekout.backend.Model.Plan;
import com.weekout.backend.Model.User;
import com.weekout.backend.Services.PlanService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping ("/create")
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
        Plan createdPlan = planService.createPlan(plan);
        return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
    }

    // GET /api/plans/get → Get all public plans with optional filters
    @GetMapping("/get")
    public ResponseEntity<List<Plan>> getAllPublicPlans(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String location // Assuming location filters meetupPoint or something similar
    ) {
        List<Plan> plans = planService.getPublicPlans(type, date, tags, location);
        return ResponseEntity.ok(plans);
    }

    // GET /api/plans/get/{id} → Get plan details by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable UUID id) {
        Plan plan = planService.getPlanById(id);
        return ResponseEntity.ok(plan);
    }

    // PUT /api/plans/update/{id} → Update a plan (host only)
    @PutMapping("/update/{id}")
    public ResponseEntity<Plan> updatePlan(@PathVariable UUID id, @RequestBody Plan updatedPlan) {
        Plan plan = planService.updatePlan(id, updatedPlan);
        return ResponseEntity.ok(plan);
    }

    // DELETE /api/plans/delete/{id} → Delete a plan (host only)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable UUID id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}

