package com.discovery.employeescrud.repository;


import com.discovery.employeescrud.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}