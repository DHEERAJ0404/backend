package com.smartcampus.services;

import com.smartcampus.dto.ComplaintRequest;
import com.smartcampus.models.Complaint;
import com.smartcampus.models.User;
import com.smartcampus.repositories.ComplaintRepository;
import com.smartcampus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {
    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.smartcampus.repositories.CommentRepository commentRepository;

    public Complaint createComplaint(ComplaintRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Complaint complaint = new Complaint();
        complaint.setUser(user);
        complaint.setCategory(request.getCategory());
        complaint.setDescription(request.getDescription());
        complaint.setImageUrl(request.getImageUrl());
        complaint.setStatus(Complaint.Status.PENDING);
        complaint.setPriority(request.getPriority() != null ? request.getPriority() : "Medium");
        complaint.setLocation(request.getLocation() != null ? request.getLocation() : "General");

        return complaintRepository.save(complaint);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public List<Complaint> getUserComplaints(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return complaintRepository.findByUserId(user.getId());
    }

    public Complaint updateStatus(Long id, Complaint.Status status) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaint.setStatus(status);
        return complaintRepository.save(complaint);
    }

    public Complaint upvoteComplaint(Long id, String userEmail) {
        Complaint complaint = complaintRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        
        if (complaint.getUpvoters().contains(user)) {
            complaint.getUpvoters().remove(user); // Toggle
        } else {
            complaint.getUpvoters().add(user);
        }
        return complaintRepository.save(complaint);
    }

    public com.smartcampus.models.Comment addComment(Long id, com.smartcampus.dto.CommentRequest request, String userEmail) {
        Complaint complaint = complaintRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        
        com.smartcampus.models.Comment comment = new com.smartcampus.models.Comment();
        comment.setComplaint(complaint);
        comment.setUser(user);
        comment.setText(request.getText());
        
        return commentRepository.save(comment);
    }

    public void deleteComplaint(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaintRepository.delete(complaint);
    }
}
