package com.ezplus.models;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface SolutionRepo extends JpaRepository<Solution, Long> {
	
	@Query("From Solution s where s.status=:status")
	public List<Solution> getStatus(String status);
}
