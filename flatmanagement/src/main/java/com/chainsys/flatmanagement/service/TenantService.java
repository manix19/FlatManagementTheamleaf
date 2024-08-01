package com.chainsys.flatmanagement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chainsys.flatmanagement.dao.DashboardDao;
import com.chainsys.flatmanagement.dao.TenantDao;
import com.chainsys.flatmanagement.model.EbBill;
import com.chainsys.flatmanagement.model.FlatDetails;
import com.chainsys.flatmanagement.model.Tenant;

@Service
public class TenantService {
	
	@Autowired
	TenantDao tenantDao; 
	
	@Autowired
	DashboardDao dashboardDao;
	
	public EbBill addEbBill(int id, int unit) throws Exception {
        int price;
        String status;
        if (unit > 100) {
            price = unit * 40;
            status = "no";
        } else if (unit > 400) {
            price = unit * 50;
            status = "no";
        } else {
            price = 0;
            status = "yes";
        }
        return tenantDao.addEbBill(id, price, status);
    }
	 public List<Map<String, String>> flatHouses() {
	        // Retrieve all flat details and tenants
	        List<FlatDetails> flatDetails = dashboardDao.getAllFlatDetails();
	        List<Tenant> tenants = tenantDao.getAllTenants();

	        // Get a list of occupied rooms from tenants
	        List<String> occupiedRooms = tenants.stream()
	            .map(Tenant::getRoomNo)
	            .collect(Collectors.toList());

	        System.out.println(flatDetails);
	        System.out.println("Occupied Rooms: " + occupiedRooms);

	        // Generate available room details excluding occupied rooms
	        return flatDetails.stream()
	            .flatMap(flat -> IntStream.rangeClosed(1, flat.getNoOfRooms())
	                .mapToObj(roomNumber -> {
	                    String room = flat.getFlatFloor() + String.valueOf((char) ('A' + roomNumber - 1));
	                    // Only include the room if it is not occupied
	                    if (!occupiedRooms.contains(room)) {
	                        Map<String, String> info = new HashMap<>();
	                        info.put("room", room);
	                        info.put("floor", String.valueOf(flat.getFlatFloor()));
	                        info.put("type", flat.getFlatType()); // Use the existing flatType field
	                        return info;
	                    }
	                    return null; // Filter out the occupied rooms
	                })
	                .filter(Objects::nonNull) // Remove null entries (occupied rooms)
	            )
	            .collect(Collectors.toList());
	    }
}
