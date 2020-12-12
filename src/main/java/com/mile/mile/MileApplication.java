package com.mile.mile;

import com.mile.mile.scheduler_lock.SchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class MileApplication {

	public static void main(String[] args) {
		SpringApplication.run(MileApplication.class, args);
	}

//	@Scheduled(/*initialDelay = 1000,*/ fixedRate = 2000)
	@SchedulerLock(lockName = "MileApplication.scheduleJobEvery10Seconds")
	public void scheduleJobEvery10Seconds() throws Exception {
		System.out.println("usao: " + System.currentTimeMillis());
//		Thread.sleep(5000);
//		throw new Exception("ovo ti ne radi");
//		System.out.println("izasao: " + System.currentTimeMillis());
	}


}
