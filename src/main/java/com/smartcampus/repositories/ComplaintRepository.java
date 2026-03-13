package com.smartcampus.repositories;

import com.smartcampus.models.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByUserId(Long userId);

    List<Complaint> findByAssignedToId(Long assignedToId);

    List<Complaint> findByStatus(Complaint.Status status);

    List<Complaint> findByCategory(String category);
}
