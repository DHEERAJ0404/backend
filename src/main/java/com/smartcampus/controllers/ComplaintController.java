package com.smartcampus.controllers;

import com.smartcampus.dto.ComplaintRequest;
import com.smartcampus.models.Complaint;
import com.smartcampus.services.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public Complaint createComplaint(@RequestBody ComplaintRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return complaintService.createComplaint(request, email);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public List<Complaint> getMyComplaints() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return complaintService.getUserComplaints(email);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Complaint updateStatus(@PathVariable Long id, @RequestBody Complaint.Status status) {
        return complaintService.updateStatus(id, status);
    }

    @PostMapping("/{id}/upvote")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public Complaint upvoteComplaint(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return complaintService.upvoteComplaint(id, email);
    }

    @PostMapping("/{id}/comments")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public com.smartcampus.models.Comment addComment(
            @PathVariable Long id,
            @RequestBody com.smartcampus.dto.CommentRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return complaintService.addComment(id, request, email);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.noContent().build();
    }
}
