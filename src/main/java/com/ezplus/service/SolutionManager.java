package com.ezplus.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezplus.models.Solution;
import com.ezplus.models.SolutionRepo;

@Service
public class SolutionManager {
	private List<Solution> prepared = new LinkedList<Solution>();
	Random r = new Random();
	
	@Autowired
	SolutionRepo Srepo;
	
	@PostConstruct
	public void init() {
		updatePrepared();
	}

	public void updatePrepared() {
		long l;
		while(prepared.size()<10) {
			l = r.nextLong();
			if(l<0) {
				l *=-1;
			}
			if(!Srepo.existsById(l) ) {
				prepared.add(new Solution(l));
			}
		}
	}
	
	public Solution getNext(String id) {
		Solution s = prepared.remove(0);
		s.setUser(id);
		s.setStatus("Processing");
		s = Srepo.saveAndFlush(s);
		updatePrepared();
		if(prepared.size()<5) {
			updatePrepared();
		}
		return s;
	}
	
	public List<Solution> getNextN(int count, String id) {
		List<Solution> mp = new LinkedList<Solution>();
		Solution s;
		while(mp.size()< count) {
			s = getNext(id);
			mp.add(s);
		}
		return mp;
	}
	
	public void processSolution(Solution s ) {
		try {
			Solution ex = Srepo.findById(s.getRange()).get();
			ex.setTime(s.getTime());
			ex.setResult(s.getResult());
			ex.setStatus("Finished");
			Srepo.saveAndFlush(ex);
		} catch (NoSuchElementException e) {
			s.setStatus("Finished");
			Srepo.saveAndFlush(s);
		}
	}
	
	public List<Solution> getPrepared() {
		return prepared;
	}

	public List<Solution> getProgress() {
		return Srepo.findAll();	
	}
	/*
	public void save() {
		try(FileWriter fileWriter = new FileWriter("results.json")) {
			fileWriter.write();
		    fileWriter.close();
		} catch (IOException e) {
		    // Cxception handling
		}
	}
*/
}
