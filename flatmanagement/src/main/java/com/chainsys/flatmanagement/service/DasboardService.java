package com.chainsys.flatmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chainsys.flatmanagement.dao.DashboardDao;
import com.chainsys.flatmanagement.model.FlatDetails;

@Service
public class DasboardService {
	@Autowired
    private DashboardDao dashboardDao;

    public void addFlat(FlatDetails flatDetails) {
    	dashboardDao.addFlat(flatDetails);
    }
    public void addMultipurposehalladd(FlatDetails flatDetails) {
    	dashboardDao.addMultipurposehalladd(flatDetails);
    }
    public List<FlatDetails> getAllFlatDetails() {
        return dashboardDao.getAllFlatDetails();
    }	
    
}
