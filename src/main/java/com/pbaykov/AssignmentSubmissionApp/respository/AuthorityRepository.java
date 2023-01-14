package com.pbaykov.AssignmentSubmissionApp.respository;

import com.pbaykov.AssignmentSubmissionApp.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
