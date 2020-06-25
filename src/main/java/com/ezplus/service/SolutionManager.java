package com.ezplus.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import javax.annotation.PostConstruct;
import java.math.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezplus.models.Solution;
import com.ezplus.models.SolutionRepo;


@Service
public class SolutionManager {
	final private static BigInteger LP = new BigInteger("66555444443333333");
	final private static BigInteger MP = new BigInteger("535006138814359");
	final private static long MX = 65_219_089_126_663_911L;
	private BigInteger seed = new BigInteger("535006138814359");
	
	private List<Solution> prepared = new LinkedList<Solution>();
	Random r = new Random();
	
	@Autowired
	SolutionRepo Srepo;
	
	@PostConstruct
	public void init() {
		List<Solution> seeds =  Srepo.getStatus("Seed");
		prepared = Srepo.getStatus("Ready");
		if( seeds.size() == 1) {
			seed = new BigInteger(""+seeds.get(0).getsolution_range());
			prepared.add(seeds.get(0));
		}
		updatePrepared();
	}

	public void updatePrepared() {
		long l;
		
		if( prepared.size()>= 1)
			prepared.get(prepared.size()-1).setStatus("Ready");
		
		while(prepared.size()<10) {
			l = nextSeed();
			prepared.add(new Solution(l));
		}
		
		prepared.get(9).setStatus("Seed");
		prepared = Srepo.saveAll(prepared);
	}
	
	private long nextSeed() {
		seed = seed.multiply(MP).mod(LP);
		return seed.longValueExact() % MX;
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
	
	public Solution processSolution(Solution s ) {
		try {
			Solution ex = Srepo.findById(s.getsolution_range()).get();
			ex.setTime(s.getTime());
			ex.setResult(s.getResult());
			ex.setStatus("Finished");
			return Srepo.saveAndFlush(ex);
		} catch (NoSuchElementException e) {
			s.setStatus("Finished");
			return Srepo.saveAndFlush(s);
		}
	}
	
	public List<Solution> getPrepared() {
		return prepared;
	}

	public List<Solution> getProgress() {
		return Srepo.findAll();	
	}

}
