package com.myapp.taskmanager.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.taskmanager.entity.ParentTask;
import com.myapp.taskmanager.entity.Task;
import com.myapp.taskmanager.exception.TaskManagerServiceException;
import com.myapp.taskmanager.service.TaskManagerService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskManagerControllerTestManager {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private TaskManagerService taskMngrService;

	private static Set<Task> taskDs;

	private static Set<ParentTask> parentTaskDs;

	@BeforeClass
	public static void setUpDS() {

		parentTaskDs = new LinkedHashSet<ParentTask>();
		ParentTask p1 = new ParentTask(1L, "Home Loan Processing", null);
		parentTaskDs.add(p1);
		ParentTask p2 = new ParentTask(2L, "Home Loan Closure", null);
		parentTaskDs.add(p2);
		ParentTask p3 = new ParentTask(6L, "KYC", null);
		parentTaskDs.add(p3);

		taskDs = new LinkedHashSet<Task>();

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

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	@Test
	public void runSmokeTest() {
		assertNotNull(taskMngrService);

		assertNotNull(restTemplate);
	}

	/**
	 * 
	 * @return
	 */
	private TestRestTemplate getRestTemplateBasicAuth() {
		return restTemplate.withBasicAuth("subodh", "subodh123");
	}

	@Test
	public void testLoginNoAuth_shouldBe401() {
		ResponseEntity<String> response = restTemplate.getForEntity("/login", String.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	@Test
	public void testLoginCorrectAuth_shouldBeOk() {
		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/login", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetAllTasks_shouldReturnAllTasksCorrectly()
			throws TaskManagerServiceException, JsonProcessingException, JSONException {

		when(taskMngrService.findAllTasks()).thenReturn(taskDs);
		String expected = mapToJson(taskDs);

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/tasks", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		verify(taskMngrService, times(1)).findAllTasks();
	}

	@Test
	public void testGetAllParentTasks_shouldReturnAllCorrectly()
			throws TaskManagerServiceException, JsonProcessingException, JSONException {

		when(taskMngrService.findAllParenTasks()).thenReturn(parentTaskDs);
		String expected = mapToJson(parentTaskDs);

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/parent-tasks", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		verify(taskMngrService, times(1)).findAllParenTasks();
	}

	@Test
	public void testGetTaskInvalidTaskId_shouldReturnBadReq()
			throws TaskManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/task/A", String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testGetTaskMissingTaskId_shouldReturnNotFound()
			throws TaskManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/task/", String.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

	}

	@Test
	public void testGetTaskNonExistingTaskId_shouldReturnNotFound()
			throws TaskManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/task/999", String.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		assertTrue(response.getBody().contains("errors"));
		verify(taskMngrService, times(1)).getTaskById(999L);
	}

	@Test
	public void testGetTaskById_shouldReturnTaskCorrectly()
			throws TaskManagerServiceException, JsonProcessingException, JSONException {

		when(taskMngrService.getTaskById(1L)).thenReturn(taskDs.stream().findFirst().get());
		String expected = mapToJson(taskDs.stream().findFirst().get());

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/task/1", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		verify(taskMngrService, times(1)).getTaskById(1L);
	}

	@Test
	public void testPostTask_shouldCreateNewTask()
			throws TaskManagerServiceException, JsonProcessingException, JSONException {

		Task t = new Task(15L, "Perform enhanced due diligence", LocalDate.now(), LocalDate.of(2019, 9, 30), 1, null,
				false);

		when(taskMngrService.createTask(any(Task.class))).thenReturn(t);

		String expected = mapToJson(t);

		ResponseEntity<String> response = getRestTemplateBasicAuth().postForEntity("/task/add", t, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(taskMngrService, times(1)).createTask(any(Task.class));

	}

	@Test
	public void testPostTaskNullReqBody_shouldReturnUnsuppMediaType()
			throws TaskManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().postForEntity("/task/add", null, String.class);

		assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());

	}

	@Test
	public void testPutTask_shouldUpdateTask()
			throws TaskManagerServiceException, JsonProcessingException, JSONException {

		Task t = new Task(7L, "Check Loan eligibility- updated", LocalDate.now(), LocalDate.of(2019, 07, 30), 20,
				parentTaskDs.stream().findFirst().get(), false);

		when(taskMngrService.updateTask(any(Task.class))).thenReturn(t);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(mapToJson(t), headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/task/update", HttpMethod.PUT, entity,
				String.class);

		String expected = mapToJson(t);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(taskMngrService, times(1)).updateTask(any(Task.class));

	}

	@Test
	public void testDeleteTaskById_shouldDeleteTaskCorrectly()
			throws TaskManagerServiceException, JsonProcessingException, JSONException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/task/delete/1", HttpMethod.DELETE,
				entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(taskMngrService, times(1)).deleteTaskById(1L);
	}

	@Test
	public void testPostTaskInvalidMethOd_shouldReturnMethodNotAllowed()
			throws TaskManagerServiceException, JsonProcessingException, JSONException {
	
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(null, headers);

			ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/task/delete/1", HttpMethod.GET,
					entity, String.class);

			assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());


		}
}
