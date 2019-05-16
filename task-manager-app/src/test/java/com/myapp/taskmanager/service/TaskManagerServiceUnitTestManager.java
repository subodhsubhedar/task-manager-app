package com.myapp.taskmanager.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.myapp.taskmanager.entity.ParentTask;
import com.myapp.taskmanager.entity.Task;
import com.myapp.taskmanager.exception.TaskManagerServiceException;
import com.myapp.taskmanager.repository.ParentTaskManagerRepository;
import com.myapp.taskmanager.repository.TaskManagerRepository;

@RunWith(MockitoJUnitRunner.class)
public class TaskManagerServiceUnitTestManager {

	@InjectMocks
	private TaskManagerServiceImpl service;

	@Mock
	private TaskManagerRepository taskRepositoryMock;

	@Mock
	private ParentTaskManagerRepository parentTaskRepositoryMock;

	private List<Task> taskDs;

	private List<ParentTask> parentTaskDs;

	@Test
	public void whenSmokeTest_thenSuccess() {

		assertNotNull(service);
	}

	@Before
	public void setUp() {
		service = new TaskManagerServiceImpl();
		MockitoAnnotations.initMocks(this);

		parentTaskDs = new ArrayList<ParentTask>();

		ParentTask p1 = new ParentTask(1L, "Home Loan Processing", null);
		parentTaskDs.add(p1);

		ParentTask p2 = new ParentTask(2L, "Home Loan Closure", null);
		parentTaskDs.add(p2);

		ParentTask p3 = new ParentTask(6L, "KYC", null);
		parentTaskDs.add(p3);

		taskDs = new ArrayList<Task>();

		// No parent
		Task t01 = new Task(1L, "Home Loan Processing", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, null, false);
		taskDs.add(t01);
		Task t02 = new Task(2L, "Home Loan Closure", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, null, false);
		taskDs.add(t02);

		// Parent as Home Loan Processing
		Task t1 = new Task(4L, "Receive Loan Application", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p1, false);
		taskDs.add(t1);
		Task t2 = new Task(5L, "Procure Customer Documents", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p1,
				false);
		taskDs.add(t2);
		Task t3 = new Task(6L, "KYC", LocalDate.now(), LocalDate.of(2019, 9, 30), 30, p1, false);
		taskDs.add(t3);
		Task t4 = new Task(7L, "Check Loan eligibility", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p1, false);
		taskDs.add(t4);

		// Parent as KYC
		Task t5 = new Task(8L, "Check criminal records", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p3, false);
		taskDs.add(t5);
		Task t6 = new Task(9L, "Check Politically Exposed Person", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p3,
				false);
		taskDs.add(t6);
		Task t7 = new Task(10L, "Check criminal records", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p3, false);
		taskDs.add(t7);
		Task t8 = new Task(11L, "Check ID Proofs", LocalDate.now(), LocalDate.of(2019, 07, 30), 20, p3, true);
		taskDs.add(t8);

		// Parent as Home Loan Closure
		Task t9 = new Task(12L, "Settle payments", LocalDate.now(), LocalDate.of(2019, 12, 30), 29, p2, false);
		taskDs.add(t9);
		Task t10 = new Task(13L, "Close Loan account", LocalDate.now(), LocalDate.of(2019, 12, 30), 29, p2, false);
		taskDs.add(t10);
		Task t11 = new Task(14L, "Archive as historical data", LocalDate.now(), LocalDate.of(2019, 12, 30), 29, p2,
				false);
		taskDs.add(t11);

		p1.addSubTasks(t1);
		p1.addSubTasks(t2);
		p1.addSubTasks(t3);
		p1.addSubTasks(t4);

		p3.addSubTasks(t5);
		p3.addSubTasks(t6);
		p3.addSubTasks(t7);
		p3.addSubTasks(t8);

		p2.addSubTasks(t9);
		p2.addSubTasks(t10);
		p2.addSubTasks(t11);

	}

	@Test
	public void testFindAllParentTasks_shouldReturnCorrectCount() throws TaskManagerServiceException {
		when(parentTaskRepositoryMock.findAll()).thenReturn(parentTaskDs);

		assertTrue(Integer.valueOf(3).equals(service.findAllParenTasks().size()));
	}

	@Test(expected = TaskManagerServiceException.class)
	public void testFindAllParentTasks_shouldThrowEx() throws TaskManagerServiceException {
		when(parentTaskRepositoryMock.findAll())
				.thenThrow(new RuntimeException("testFindAllParentTasks_shouldThrowEx"));

		service.findAllParenTasks().size();
	}

	@Test
	public void testFindAllTasks_shouldReturnCorrectCount() throws TaskManagerServiceException {

		when(taskRepositoryMock.findAll()).thenReturn(taskDs);

		assertTrue(Integer.valueOf(13).equals(service.findAllTasks().size()));
	}

	@Test(expected = TaskManagerServiceException.class)
	public void testFindAllTasks_shouldThrowEx() throws TaskManagerServiceException {
		when(taskRepositoryMock.findAll()).thenThrow(new RuntimeException("testFindAllTasks_shouldThrowEx"));

		service.findAllTasks().size();
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectDesc() throws TaskManagerServiceException {

		when(taskRepositoryMock.findByTask("Check criminal records")).thenReturn(Optional.of(taskDs.get(6)));

		assertTrue("Check criminal records".equals((service.getTask("Check criminal records")).getTask()));
	}

	@Test(expected = TaskManagerServiceException.class)
	public void testFindTaskByDesc_shouldThrowEx() throws TaskManagerServiceException {
		when(taskRepositoryMock.findByTask("Check criminal records"))
				.thenThrow(new RuntimeException("testFindTaskByDesc_shouldThrowEx"));

		service.getTask("Check criminal records");
	}

	@Test(expected = TaskManagerServiceException.class)
	public void testFindNonExistingTaskByDesc_shouldThrowEx() throws TaskManagerServiceException {
		when(taskRepositoryMock.findByTask("abc")).thenReturn(Optional.empty());

		service.getTask("abc");
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectPriority() throws TaskManagerServiceException {

		when(taskRepositoryMock.findByTask("Check Politically Exposed Person")).thenReturn(Optional.of(taskDs.get(7)));

		assertTrue(Integer.valueOf(20).equals((service.getTask("Check Politically Exposed Person")).getPriority()));
	}

	@Test
	public void testFindTaskById_shouldReturnTask() throws TaskManagerServiceException {

		when(taskRepositoryMock.findById(12L)).thenReturn(Optional.of(taskDs.get(10)));
		assertNotNull(service.getTaskById(12L));
	}

	@Test(expected = TaskManagerServiceException.class)
	public void testFindTaskByNonExistingId_shouldThrowEx() throws TaskManagerServiceException {

		when(taskRepositoryMock.findById(1123342L)).thenReturn(Optional.empty());
		service.getTaskById(1123342L);
	}

	@Test
	public void testFindParentTaskById_shouldReturnCorrectDesc() throws TaskManagerServiceException {

		when(parentTaskRepositoryMock.findById(6L)).thenReturn(Optional.of(parentTaskDs.get(2)));

		assertTrue("KYC".equals((service.getParentTaskById(6L).getParentTaskDesc())));
	}

	@Test(expected = TaskManagerServiceException.class)
	public void testFindParentTaskById_shouldThrowEx() throws TaskManagerServiceException {

		when(parentTaskRepositoryMock.findById(6L))
				.thenThrow(new RuntimeException("testFindParentTaskById_shouldThrowEx"));

		service.getParentTaskById(6L).getParentTaskDesc();

	}

	public void testFindParentTaskById_shouldRetNullAndPass() throws TaskManagerServiceException {

		when(parentTaskRepositoryMock.findById(101111L)).thenReturn(Optional.of(parentTaskDs.get(2)));

		assertNull((service.getParentTaskById(101111L).getParentTaskDesc()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectCompletionStatus() throws TaskManagerServiceException {

		when(taskRepositoryMock.findByTask("Check ID Proofs")).thenReturn(Optional.of(taskDs.get(9)));

		assertTrue(Boolean.valueOf(true).equals((service.getTask("Check ID Proofs")).getTaskComplete()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectStartDate() throws TaskManagerServiceException {

		when(taskRepositoryMock.findByTask("Check ID Proofs")).thenReturn(Optional.of(taskDs.get(9)));

		assertTrue(LocalDate.now().equals((service.getTask("Check ID Proofs")).getStartDate()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectEndDate() throws TaskManagerServiceException {

		when(taskRepositoryMock.findByTask("Check ID Proofs")).thenReturn(Optional.of(taskDs.get(9)));

		assertTrue(LocalDate.of(2019, 7, 30).equals((service.getTask("Check ID Proofs")).getEndDate()));
	}

	@Test
	public void testCreateTask_shouldCreateNewTask() throws TaskManagerServiceException {

		Task t = new Task(15L, "Address verification", LocalDate.now(), LocalDate.of(2019, 9, 30), 20,
				parentTaskDs.get(2), true);

		when(taskRepositoryMock.save(t)).thenReturn(t);
		assertTrue("Address verification".equals(service.createTask(t).getTask()));
	}

	@Test(expected = TaskManagerServiceException.class)
	public void testCreateTask_shouldThrowException() throws TaskManagerServiceException {

		Task t = new Task(15L, "Address verification", LocalDate.now(), LocalDate.of(2019, 9, 30), 20,
				parentTaskDs.get(2), true);

		when(taskRepositoryMock.save(t)).thenThrow(new RuntimeException("testCreateTask_shouldThrowException"));
		service.createTask(t).getTask();
	}

	@Test
	public void testUpdateTask_shouldRetUpdatedTask() throws TaskManagerServiceException {

		when(taskRepositoryMock.findById(10L)).thenReturn(Optional.of(taskDs.get(9)));

		Task t7 = new Task(10L, "Check criminal records - Updated", LocalDate.now(), LocalDate.of(2019, 11, 28), 20,
				parentTaskDs.get(2), true);

		when(taskRepositoryMock.save(taskDs.get(9))).thenReturn((t7));

		assertTrue("Check criminal records - Updated".equals(service.updateTask(t7).getTask()));
		assertTrue(LocalDate.of(2019, 11, 28).equals(service.updateTask(t7).getEndDate()));
		assertTrue((service.updateTask(t7).getTaskComplete()));

	}

	@Test(expected = TaskManagerServiceException.class)
	public void testUpdateTask_shouldThrowEx() throws TaskManagerServiceException {

		when(taskRepositoryMock.findById(10L)).thenReturn(Optional.of(taskDs.get(9)));

		Task t7 = new Task(10L, "Check criminal records - Updated", LocalDate.now(), LocalDate.of(2019, 11, 28), 20,
				parentTaskDs.get(2), true);

		when(taskRepositoryMock.save(taskDs.get(9))).thenThrow(new RuntimeException("testUpdateTask_shouldThrowEx"));

		service.updateTask(t7);

	}

	@Test
	public void testCreateTaskWithoutParentTask_shouldPass() throws TaskManagerServiceException {

		Task t = new Task(15L, "Miscellanous", LocalDate.now(), LocalDate.of(2019, 10, 30), 1, null, true);

		when(taskRepositoryMock.save(t)).thenReturn(t);

		assertNull((service.createTask(t).getParentTask()));
	}

	@Test
	public void testDeleteTask_shouldDelete() throws TaskManagerServiceException {

		when(taskRepositoryMock.findById(12L)).thenReturn(Optional.of(taskDs.get(10)));

		service.deleteTaskById((taskDs.get(10).getTaskId()));

		verify(taskRepositoryMock).delete(taskDs.get(10));
	}

	@Test(expected = TaskManagerServiceException.class)
	public void testDeleteTask_shouldThrowException() throws TaskManagerServiceException {

		when(taskRepositoryMock.findById(12L)).thenThrow(new RuntimeException("testDeleteTask_shouldThrowException"));

		service.deleteTaskById((taskDs.get(10).getTaskId()));
	}

	@Test(expected = TaskManagerServiceException.class)
	public void testDeleteInvalidTask_shouldThrowEx() throws TaskManagerServiceException {

		when(taskRepositoryMock.findById(12347858L)).thenReturn(Optional.empty());

		service.deleteTaskById(12347858L);
	}
}
