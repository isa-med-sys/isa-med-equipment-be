package com.isa.med_equipment;

import com.isa.med_equipment.model.*;
import com.isa.med_equipment.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(TestConfig.class)
@SpringBootTest
class EquipmentTests {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TimeSlotRepository timeSlotRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private EquipmentRepository equipmentRepository;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Test
	public void reserve_EquipmentOutOfStock_Fails() {
		assertThrows(PessimisticLockingFailureException.class, () -> {

			ExecutorService executor = Executors.newFixedThreadPool(2);

			Future<?> future1 = executor.submit(() -> {
				transactionTemplate.execute(status -> {
					Reservation reservation = makeReservation(1L, 24L);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					reservationRepository.save(reservation);
					return null;
				});
			});

			Future<?> future2 = executor.submit(() -> {
				transactionTemplate.execute(status -> {
					Reservation reservation = makeReservation(14L, 25L);
					reservationRepository.save(reservation);
					return null;
				});
			});

			try {
				future2.get();
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
	public void reserve_EquipmentRemoved_Fails() {
		assertThrows(PessimisticLockingFailureException.class, () -> {

			Company company = companyRepository.findById(3L).orElseThrow();
			ExecutorService executor = Executors.newFixedThreadPool(2);

			Future<?> future1 = executor.submit(() -> {
				transactionTemplate.execute(status -> {
					Equipment equipment = equipmentRepository.findWithLockingById(5L).orElseThrow();
					company.getEquipment().clear();
					company.getEquipment().put(equipment, 0);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					equipmentRepository.save(equipment);
					return null;
				});
			});

			Future<?> future2 = executor.submit(() -> {
				transactionTemplate.execute(status -> {
					RegisteredUser user = (RegisteredUser) userRepository.findById(1L).orElseThrow();
					TimeSlot timeSlot = timeSlotRepository.findById(24L).orElseThrow();
					List<Equipment> equipment = equipmentRepository.findWithLockingAllByIdIn(List.of(5L));
					Reservation reservation = new Reservation();
					reservation.make(user, equipment, timeSlot);
					reservationRepository.save(reservation);
					return null;
				});
			});

			try {
				future2.get();
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

	private Reservation makeReservation(Long userId, Long timeSlotId) {
		RegisteredUser user = (RegisteredUser) userRepository.findById(userId).orElseThrow();
		Company company = companyRepository.findById(3L).orElseThrow();
		TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId).orElseThrow();
		List<Equipment> equipment = equipmentRepository.findWithLockingAllByIdIn(List.of(5L));

		checkEquipmentAvailability(company, equipment);

		Reservation reservation = new Reservation();
		reservation.make(user, equipment, timeSlot);

		return reservation;
	}


	private void checkEquipmentAvailability(Company company, List<Equipment> equipment) {
		for (Equipment equip : equipment) {
			int inStockQuantity = company.getEquipmentQuantityInStock(equip);
			int reservedQuantity = reservationRepository.getTotalReservedQuantity(equip, company.getId());

			int availableQuantity = inStockQuantity - reservedQuantity;
			if (availableQuantity <= 0) {
				throw new IllegalStateException("No equipment available: " + equip.getName());
			}
		}
	}
}
