package com.chainsys.flatmanagement.dao.impl1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chainsys.flatmanagement.dao.DashboardDao;
import com.chainsys.flatmanagement.model.FlatDetails;

@Repository
public class DashBoadImpl implements DashboardDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addFlat(FlatDetails flatDetails) {
		String checkSql = "SELECT COUNT(*) FROM flat_details WHERE flat_floor = ? AND flat_type = ?";
	    int count = jdbcTemplate.queryForObject(checkSql,Integer.class,new Object[]{flatDetails.getFlatFloor(), flatDetails.getFlatType()});

	    if (count > 0) {
	        // SQL to update the flat details if it exists
	        String updateSql = "UPDATE flat_details SET owner_id = ?, no_of_rooms = ? WHERE flat_floor = ? AND flat_type = ?";
	        jdbcTemplate.update(updateSql, flatDetails.getOwnerId(), flatDetails.getNoOfRooms(), flatDetails.getFlatFloor(), flatDetails.getFlatType());
	    } else {
	        // SQL to insert new flat details if it does not exist
	        String insertSql = "INSERT INTO flat_details (owner_id, flat_type, flat_floor, no_of_rooms) VALUES (?, ?, ?, ?)";
	        jdbcTemplate.update(insertSql, flatDetails.getOwnerId(), flatDetails.getFlatType(), flatDetails.getFlatFloor(), flatDetails.getNoOfRooms());
	    }
	}
	public void addMultipurposehalladd(FlatDetails flatDetails) {
		String checkSql = "SELECT COUNT(*) FROM flat_details WHERE flat_floor = ? AND flat_type = ? ";
	    int count = jdbcTemplate.queryForObject(checkSql,Integer.class,new Object[]{flatDetails.getFlatFloor(),"4BHK"});

	    if (count > 0) {
	        // SQL to update the flat details if it exists
	        String updateSql = "UPDATE flat_details SET owner_id = ?, no_of_rooms = ? WHERE flat_floor = ? AND flat_type = ?";
	        jdbcTemplate.update(updateSql, flatDetails.getOwnerId(),1, flatDetails.getFlatFloor(),"4BHK");
	    } else {
	        // SQL to insert new flat details if it does not exist
	        String insertSql = "INSERT INTO flat_details (owner_id, flat_type, flat_floor, no_of_rooms,type) VALUES (?, ?, ?, ?,?)";
	        jdbcTemplate.update(insertSql, flatDetails.getOwnerId(),"4BHK", flatDetails.getFlatFloor(),1,"event hall");
	    }
	}
	public List<FlatDetails> getAllFlatDetails() {
        String sql = "SELECT id, owner_id, flat_type, flat_floor, no_of_rooms, type FROM flat_details WHERE type = 'house' ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            FlatDetails flatDetails = new FlatDetails();
            flatDetails.setId(rs.getInt("id"));
            flatDetails.setOwnerId(rs.getInt("owner_id"));
            flatDetails.setFlatType(rs.getString("flat_type"));
            flatDetails.setFlatFloor(rs.getInt("flat_floor"));
            flatDetails.setNoOfRooms(rs.getInt("no_of_rooms"));
            flatDetails.setType(rs.getString("type"));
            return flatDetails;
        });
    }
}
