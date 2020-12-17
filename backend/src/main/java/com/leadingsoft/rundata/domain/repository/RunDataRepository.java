package com.leadingsoft.rundata.domain.repository;

import com.leadingsoft.rundata.domain.entity.RunDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunDataRepository extends JpaRepository<RunDataEntity, String> {
}
