package com.example.employeeManagementcrud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface employeeRepository extends JpaRepository<employee, Long> {

    // Custom query methods for Employee

    // Find employees by department
    List<employee> findByDepartment(String department);

    // Find employees by position
    List<employee> findByPosition(String position);

    // Find employee by email (unique)
    employee findByEmail(String email);

    // Find employees by salary greater than given amount
    List<employee> findBySalaryGreaterThan(Double salary);

    // Find employees by salary between min and max
    List<employee> findBySalaryBetween(Double minSalary, Double maxSalary);

    // Find employees by last name
    List<employee> findByLastName(String lastName);

    // Find employees by first name containing keyword (case-insensitive)
    List<employee> findByFirstNameContainingIgnoreCase(String keyword);

    // Find employees by department and position
    List<employee> findByDepartmentAndPosition(String department, String position);

    // Find employees hired after a specific date
    List<employee> findByHireDateAfter(LocalDate date);
}