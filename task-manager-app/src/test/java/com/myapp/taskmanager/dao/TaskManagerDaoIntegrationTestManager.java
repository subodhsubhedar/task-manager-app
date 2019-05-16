package com.myapp.taskmanager.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.myapp.taskmanager.entity.ParentTask;
import com.myapp.taskmanager.entity.Task;
import com.myapp.taskmanager.repository.ParentTaskManagerRepository;
import com.myapp.taskmanager.repository.TaskManagerRepository;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = true)
public class TaskManagerDaoIntegrationTestManager {

	@Autowired
	private TestEntityManager entityMngr;

	@Autowired
	private TaskManagerRepository taskManagerRepository;

	@Autowired
	private ParentTaskManagerRepository parentTaskManagerRepository;

	@Before
	public void setUp() {

		ParentTask p1 = new ParentTask(1L, "Home Loan Processing", null);
		ParentTask p2 = new ParentTask(2L, "Home Loan Closure", null);
		ParentTask p3 = new ParentTask(6L, "KYC", null);

		p1 = entityMngr.persist(p1);
		p2 = entityMngr.persist(p2);
		p3 = entityMngr.persist(p3);

		entityMngr.flush();

		// No parent
		Task t1 = new Task(0L, "Home Loan Processing", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, null, false);
		Task t2 = new Task(0L, "Home Loan Closure", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, null, false);

		// Parent as Home Loan Processing
		Task t3 = new Task(0L, "Receive Loan Application", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p1, false);
		Task t4 = new Task(0L, "Procure Customer Documents", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p1,
				false);
		Task t5 = new Task(0L, "KYC", LocalDate.now(), LocalDate.of(2019, 9, 30), 30, p1, false);
		Task t6 = new Task(0L, "Check Loan eligibility", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p1, false);

		// Parent as KYC
		Task t7 = new Task(0L, "Check criminal records", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p3, false);
		Task t8 = new Task(0L, "Check Politically Exposed Person", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p3,
				false);
		Task t9 = new Task(0L, "Check criminal records", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p3, false);
		Task t10 = new Task(0L, "Check ID Proofs", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p3, true);

		// Parent as Home Loan Closure
		Task t11 = new Task(0L, "Settle payments", LocalDate.now(), LocalDate.of(2019, 12, 30), 29, p2, false);
		Task t12 = new Task(0L, "Close Loan account", LocalDate.now(), LocalDate.of(2019, 12, 30), 29, p2, false);
		Task t13 = new Task(0L, "Archive as historical data", LocalDate.now(), LocalDate.of(2019, 12, 30), 29, p2,
				false);

		entityMngr.persist(t1);
		entityMngr.persist(t2);
		entityMngr.persist(t3);
		entityMngr.persist(t4);
		entityMngr.persist(t5);
		entityMngr.persist(t6);
		entityMngr.persist(t7);
		entityMngr.persist(t8);
		entityMngr.persist(t9);
		entityMngr.persist(t10);
		entityMngr.persist(t11);
		entityMngr.persist(t12);
		entityMngr.persist(t13);

		entityMngr.flush();
	}

	@Test
	public void testFindAllTasks_shouldReturnCorrectCount() {
		assertTrue(Integer.valueOf(13).equals(taskManagerRepository.findAll().size()));
	}

	@Test
	public void testFindAllParentTasks_shouldReturnCorrectCount() {
		assertTrue(Integer.valueOf(3).equals(parentTaskManagerRepository.findAll().size()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectDesc() {

		assertTrue("Check Politically Exposed Person"
				.equals(taskManagerRepository.findByTask("Check Politically Exposed Person").get().getTask()));
	}

	@Test
	public void testFindParentTaskById_shouldReturnCorrectTitle() {
		assertTrue("Home Loan Processing".equals(parentTaskManagerRepository.findById(1L).get().getParentTaskDesc()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectPriority() {
		assertTrue(
				Integer.valueOf(29).equals(taskManagerRepository.findByTask("Close Loan account").get().getPriority()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectParentTask() {
		assertTrue("Home Loan Closure".equals(
				taskManagerRepository.findByTask("Close Loan account").get().getParentTask().getParentTaskDesc()));
	}

	@Test
	public void testFindTaskById_shouldReturnCorrectTask() {
		assertTrue("Home Loan Closure".equals(taskManagerRepository.findById(2L).get().getTask()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectStartDate() {
		assertTrue(LocalDate.now().equals(taskManagerRepository.findByTask("Close Loan account").get().getStartDate()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectEndDate() {
		assertTrue(LocalDate.of(2019, 12, 30)
				.equals(taskManagerRepository.findByTask("Close Loan account").get().getEndDate()));
	}

	@Test
	@DirtiesContext
	public void testCreateTaskNoParent_shouldCreateNewTask() {

		Task t14 = new Task(0L, "Miscellanous", LocalDate.now(), LocalDate.of(2019, 12, 30), 29, null, false);
		assertTrue("Miscellanous".equals((taskManagerRepository.save(t14)).getTask()));
	}

	@Test
	@DirtiesContext
	public void testCreateTaskWithParent_shouldCreateNewTask() {
		ParentTask p2 = new ParentTask(2L, "Home Loan Closure", null);

		Task t14 = new Task(0L, "Closure- Miscellanous", LocalDate.now(), LocalDate.of(2019, 12, 30), 29, p2, false);

		assertTrue("Closure- Miscellanous".equals((taskManagerRepository.save(t14)).getTask()));
	}

	@Test
	@DirtiesContext
	public void testCreateTaskWithParent_shouldCreateNewTaskCorrectParent() {
		ParentTask p2 = new ParentTask(2L, "Home Loan Closure", null);

		Task t14 = new Task(0L, "Closure- Miscellanous", LocalDate.now(), LocalDate.of(2019, 12, 30), 29, p2, false);

		assertTrue("Home Loan Closure".equals((taskManagerRepository.save(t14)).getParentTask().getParentTaskDesc()));
	}

	@Test
	@DirtiesContext
	public void testDeleteTask_shouldDelete() {

		Task t = taskManagerRepository.findByTask("Settle payments").get();
		taskManagerRepository.delete(t);

		assertFalse(taskManagerRepository.findByTask("Settle payments").isPresent());
	}

	@Test
	@DirtiesContext
	public void testUpdateTask_shouldUpdateTask() {

		Task t = taskManagerRepository.findByTask("Check ID Proofs").get();

		t.setTask("Check ID Proofs - updated");

		t = taskManagerRepository.save(t);

		assertTrue("Check ID Proofs - updated".equals((t).getTask()));
	}

}