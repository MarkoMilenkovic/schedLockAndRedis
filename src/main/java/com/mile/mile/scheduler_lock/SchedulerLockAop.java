package com.mile.mile.scheduler_lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.Date;

@Component
@Aspect
public class SchedulerLockAop {

    @Autowired
    private SchedulerLockDao schedulerLockDao;

    @Around(value = "@annotation(schedulerLock)")
    public void invoke(ProceedingJoinPoint proceedingJoinPoint, SchedulerLock schedulerLock) {
        String lockName = schedulerLock.lockName();
        SchedulerLockEntity lockEntity = schedulerLockDao.findByName(lockName);
        if (lockEntity == null) {
            lockEntity = new SchedulerLockEntity();
            lockEntity.setName(lockName);

            lockEntity.setLockedUntil(addMinutesToCurrentDate(1));
            proceed(proceedingJoinPoint, lockEntity);
        } else {
//            if (lockEntity.getLockedUntil().after(Calendar.getInstance().getTime())) {
////                do nothing
//                return;
//            }
            if (lockEntity.getLockedUntil().before(Calendar.getInstance().getTime())) {
                lockEntity.setLockedUntil(addMinutesToCurrentDate(1));
                proceed(proceedingJoinPoint, lockEntity);
            }
        }
    }

    private void proceed(ProceedingJoinPoint proceedingJoinPoint, SchedulerLockEntity lockEntity) {
        try {
            schedulerLockDao.save(lockEntity);
            proceedingJoinPoint.proceed();
            schedulerLockDao.delete(lockEntity);
        } catch (DataIntegrityViolationException e) {
            System.out.println("vec postoji");
        } catch (Throwable throwable) {
            System.out.println("EXCEPTION OCCURED");
            schedulerLockDao.delete(lockEntity);
        }
    }

    private Date addMinutesToCurrentDate(int minutes) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, minutes);
        return instance.getTime();
    }

//    public static void main(String[] args) {
//        Calendar instance = Calendar.getInstance();
//        instance.add(Calendar.MINUTE, 30);
//        Date time = instance.getTime();
//        System.out.println(time);
//    }

}
