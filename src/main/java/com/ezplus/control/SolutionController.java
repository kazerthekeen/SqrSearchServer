package com.ezplus.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ezplus.models.Solution;
import com.ezplus.service.SolutionManager;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class SolutionController {

	@Autowired
	SolutionManager sm;
	
	@GetMapping("progress")
	public List<Solution> getProgress(){
		return sm.getProgress();
	}
	
	@GetMapping("pending")
	public List<Solution> getPrepared(){
		return sm.getPrepared();
	}
	
	@PostMapping("next")
	public Solution getNext() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		request.getRemoteAddr();
		return sm.getNext(request.getRemoteAddr());
	}
	
	@PostMapping(path = "solution", consumes = "application/json")
	public Solution updateSolution(@RequestBody Solution s){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		s.setUser(request.getRemoteAddr());
		return sm.processSolution(s);
	} 
}
