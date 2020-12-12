package com.mile.mile.scheduler_lock;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerLockDao extends JpaRepository<SchedulerLockEntity, String> {

    SchedulerLockEntity findByName(String lockName);
}
