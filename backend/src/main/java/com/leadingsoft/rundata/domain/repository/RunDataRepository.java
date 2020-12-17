package com.leadingsoft.rundata.domain.repository;

import com.leadingsoft.rundata.domain.entity.RunDataEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RunDataRepository extends JpaRepository<RunDataEntity, String> {
	@Query(value = "SELECT * FROM rundata.run_data WHERE run_date >= DATE(?) ORDER BY run_steps desc", nativeQuery = true)
	List<RunDataEntity> findByMatchMonthAndMatchDay(@Param ("run_date") String eventDate);
}