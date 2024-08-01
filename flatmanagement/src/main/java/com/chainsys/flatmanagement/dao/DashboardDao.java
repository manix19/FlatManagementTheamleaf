package com.chainsys.flatmanagement.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chainsys.flatmanagement.model.FlatDetails;

@Repository
public interface DashboardDao {
	void addFlat(FlatDetails flatDetails);
	List<FlatDetails> getAllFlatDetails();
	void addMultipurposehalladd(FlatDetails flatDetails);
}
