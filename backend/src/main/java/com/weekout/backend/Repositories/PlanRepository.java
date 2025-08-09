package com.weekout.backend.Repositories;

import com.weekout.backend.Model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {
    // You can add query methods here if needed for filtering plans by type, date, tags, etc.
}

