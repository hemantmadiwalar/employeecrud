package com.discovery.employeescrud;

import com.discovery.employeescrud.entity.Employee;
import com.discovery.employeescrud.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;


@WebMvcTest
class EmployeescrudApplicationTests {
	

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService employeeService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	 void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{

		Employee employee = Employee.builder()
				.firstName("Hemant")
				.lastName("Madiwalar")
				.email("hemant@gmail.com")
				.build();
		given(employeeService.saveEmployee(any(Employee.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));

		// when - action or behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/api/employees/addemployee")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));

		// then - verify the result or output using assert statements
		response.andDo(print()).
				andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName",
						is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName",
						is(employee.getLastName())))
				.andExpect(jsonPath("$.email",
						is(employee.getEmail())));

	}

	@Test
	 void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception{
		// given - precondition or setup
		List<Employee> listOfEmployees = new ArrayList<>();
		listOfEmployees.add(Employee.builder().firstName("Hemant").lastName("Madiwalar").email("hemant@gmail.com").build());
		listOfEmployees.add(Employee.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
		given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/api/employees/getall"));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()",
						is(listOfEmployees.size())));

	}
	// positive scenario - valid employee id
	// JUnit test for GET employee by id REST API
	@Test
	 void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception{
		// given - precondition or setup
		long employeeId = 1L;
		Employee employee = Employee.builder()
				.firstName("Hemant")
				.lastName("Madiwalar")
				.email("hemant@gmail.com")
				.build();
		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/api/employees/getone/{id}", employeeId));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));

	}

	// negative scenario - valid employee id
	// JUnit test for GET employee by id REST API

	@Test
	 void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception{
		// given - precondition or setup
		long employeeId = 1L;
		Employee employee = Employee.builder()
				.firstName("Hemant")
				.lastName("Madiwalar")
				.email("hemant@gmail.com")
				.build();
		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/api/employees/getone/{id}", employeeId));

		// then - verify the output
		response.andExpect(status().isNotFound())
				.andDo(print());

	}
	// JUnit test for update employee REST API - positive scenario

	@Test
	 void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception{
		// given - precondition or setup
		long employeeId = 1L;
		Employee savedEmployee = Employee.builder()
				.firstName("Hemant")
				.lastName("Madiwalar")
				.email("hemant@gmail.com")
				.build();

		Employee updatedEmployee = Employee.builder()
				.firstName("Ram")
				.lastName("Jadhav")
				.email("ram@gmail.com")
				.build();
		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
		given(employeeService.updateEmployee(any(Employee.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(put("/api/employees/update/{id}", employeeId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedEmployee)));


		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
				.andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
	}

	// JUnit test for update employee REST API - negative scenario

	@Test
	 void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception{
		// given - precondition or setup
		long employeeId = 1L;
		Employee savedEmployee = Employee.builder()
				.firstName("Hemant")
				.lastName("Madiwalar")
				.email("hemant@gmail.com")
				.build();

		Employee updatedEmployee = Employee.builder()
				.firstName("Ram")
				.lastName("Jadhav")
				.email("ram@gmail.com")
				.build();
		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
		given(employeeService.updateEmployee(any(Employee.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(put("/api/employees/update/{id}", employeeId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedEmployee)));

		// then - verify the output
		response.andExpect(status().isNotFound())
				.andDo(print());
	}

	// JUnit test for delete employee REST API
	@Test
	 void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception{
		// given - precondition or setup
		long employeeId = 1L;
		willDoNothing().given(employeeService).deleteEmployee(employeeId);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(delete("/api/employees/delete/{id}", employeeId));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print());
	}


}
