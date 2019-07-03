package com.stanostrovskii;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.stanostrovskii.dao.DepartmentRepository;
import com.stanostrovskii.dao.EmployeeRepository;
import com.stanostrovskii.model.Department;
import com.stanostrovskii.model.Employee;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackages = "com.stanostrovskii")
public class RepositoryTests {
	
	private static final Logger log = LoggerFactory.getLogger(RepositoryTests.class);

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	DepartmentRepository departmentRepository;

	@Test
	public void testDeptRepo() throws Exception {
		departmentRepository.save(new Department("TestDepartment"));
		Optional<Department> deptOptional =
				departmentRepository.findByDepartmentName("TestDepartment");
		if(!deptOptional.isPresent()) fail();
		else assertEquals(deptOptional.get().getDepartmentName(), "TestDepartment");
	}
	
	@Test
	public void testEmployeeRepo() throws Exception {
		departmentRepository.save(new Department("EmployeeTestDepartment"));
		Optional<Department> optional = 
				departmentRepository.findByDepartmentName("EmployeeTestDepartment");
		if(!optional.isPresent())
		{
			log.error("Failed getting department by name");
			fail();
		}
		Department dept = optional.get();
		Employee employee = new Employee("Sven", "Erikson", "sven@abc.com", "1234567890");
		employee.setDept(dept);
		Employee savedEmployee = employeeRepository.save(employee);
		log.debug(savedEmployee.toString());
		Employee getEmployeeByLastName = employeeRepository.findByLastName("Erikson").get(0);
		assertEquals(savedEmployee, getEmployeeByLastName);
	}
	
}