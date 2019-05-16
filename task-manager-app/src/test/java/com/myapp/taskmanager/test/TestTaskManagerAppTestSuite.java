package com.myapp.taskmanager.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myapp.taskmanager.dao.TaskManagerDaoIntegrationTestManager;
import com.myapp.taskmanager.service.TaskManagerServiceIntegrationTestManager;
import com.myapp.taskmanager.service.TaskManagerServiceUnitTestManager;

@RunWith(Suite.class)
@SuiteClasses({ TaskManagerDaoIntegrationTestManager.class, TaskManagerServiceIntegrationTestManager.class,
		TaskManagerServiceUnitTestManager.class })

public class TestTaskManagerAppTestSuite {

	private static final Logger logger = LoggerFactory.getLogger(TestTaskManagerAppTestSuite.class);

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(TestTaskManagerAppTestSuite.class);
		int i = 1;

		for (Failure failure : result.getFailures()) {
			logger.error("###############  " + "TEST FAILURE : " + i + "  ###############");

			logger.error("\n" + failure.toString());
			failure.getException().printStackTrace();
			i++;
		}

		logger.debug("Test successful? " + result.wasSuccessful());

		logger.debug("\n###############  " + "END OF TEST, TOTAL RUNS : " + i + "  ###############");
	}

}
