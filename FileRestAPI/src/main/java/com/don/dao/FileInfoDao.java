package com.don.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.don.model.FileInfo;

public interface FileInfoDao extends CrudRepository<FileInfo, Integer>, JpaRepository<FileInfo, Integer> {
	
	@Query(value = "SELECT f FROM FileInfo f WHERE HOUR(f.date) > (HOUR(current_timestamp)-1)")
	List<FileInfo> getLastHour();
	
	@Query(value = "SELECT f FROM FileInfo f WHERE f.name LIKE %?1%")
	List<FileInfo> searchByName(String name);
	
}
