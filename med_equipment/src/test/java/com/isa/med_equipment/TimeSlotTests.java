package com.isa.med_equipment;

import com.isa.med_equipment.model.CompanyAdmin;
import com.isa.med_equipment.model.TimeSlot;
import com.isa.med_equipment.repository.TimeSlotRepository;
import com.isa.med_equipment.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(TestConfig.class)
@SpringBootTest
class TimeSlotTests {

	@Autowired
	private TimeSlotRepository timeSlotRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void reserve_AdminBecameReserved_Fails() {
		assertThrows(ObjectOptimisticLockingFailureException.class, () -> {

			TimeSlot timeslot1 = new TimeSlot();
			timeslot1.setStart(LocalDateTime.now());
			timeslot1.setIsFree(false);

			TimeSlot timeslot2 = new TimeSlot();
			timeslot2.setStart(LocalDateTime.now());
			timeslot2.setIsFree(false);

			ExecutorService executor = Executors.newFixedThreadPool(2);
			Future<?> future1 = executor.submit(() -> {
				CompanyAdmin admin = (CompanyAdmin) userRepository.findById(2L).orElseThrow();
				admin.addTimeSlot(timeslot1);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				timeSlotRepository.save(timeslot1);
				userRepository.save(admin);
			});

			executor.submit(() -> {
				CompanyAdmin admin = (CompanyAdmin) userRepository.findById(2L).orElseThrow();
				admin.addTimeSlot(timeslot2);
				timeSlotRepository.save(timeslot2);
				userRepository.save(admin);
			});

			try {
				future1.get();
			} catch (ExecutionException e) {
				System.out.println("Exception from thread " + e.getCause().getClass());
				throw e.getCause();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				executor.shutdown();
			}
		});
	}

	@Test
	public void reserve_TimeSlotBecameReserved_Fails() {
		assertThrows(ObjectOptimisticLockingFailureException.class, () -> {

			ExecutorService executor = Executors.newFixedThreadPool(2);
			Future<?> future1 = executor.submit(() -> {
				TimeSlot timeSlot = timeSlotRepository.findById(1L).orElseThrow();
				timeSlot.reserve();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				timeSlotRepository.save(timeSlot);
			});

			executor.submit(() -> {
				TimeSlot timeSlot = timeSlotRepository.findById(1L).orElseThrow();
				timeSlot.reserve();
				timeSlotRepository.save(timeSlot);
			});

			try {
				future1.get();
			} catch (ExecutionException e) {
				System.out.println("Exception from thread " + e.getCause().getClass());
				throw e.getCause();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				executor.shutdown();
			}
		});
	}
}
