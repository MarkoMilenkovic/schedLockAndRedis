package com.mile.mile.scheduler_lock;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerLockDao extends JpaRepository<SchedulerLockEntity, Integer> {

    SchedulerLockEntity findByName(String lockName);
}
