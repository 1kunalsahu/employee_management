package com.example.employeeManagementcrud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeManagementController {

    @Autowired
    private employeeRepository employeeRepository;

    // 1. GET ALL EMPLOYEES
    @GetMapping
    public ResponseEntity<List<employee>> getAllEmployees() {
        try {
            List<employee> employees = employeeRepository.findAll();
            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. GET EMPLOYEE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<employee> getEmployeeById(@PathVariable("id") Long id) {
        Optional<employee> employeeData = employeeRepository.findById(id);

        if (employeeData.isPresent()) {
            return new ResponseEntity<>(employeeData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 3. CREATE NEW EMPLOYEE
    @PostMapping
    public ResponseEntity<employee> createEmployee(@RequestBody employee employee) {
        try {
            // Check if email already exists
            employee existingEmployee = employeeRepository.findByEmail(employee.getEmail());
            if (existingEmployee != null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            // Set current date as hire date if not provided
            if (employee.getHireDate() == null) {
                employee.setHireDate(LocalDate.now());
            }

            employee savedEmployee = employeeRepository.save(employee);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 4. UPDATE EMPLOYEE
    @PutMapping("/{id}")
    public ResponseEntity<employee> updateEmployee(@PathVariable("id") Long id, @RequestBody employee employee) {
        Optional<employee> employeeData = employeeRepository.findById(id);

        if (employeeData.isPresent()) {
            employee existingEmployee = employeeData.get();

            // Update fields if provided
            if (employee.getFirstName() != null) {
                existingEmployee.setFirstName(employee.getFirstName());
            }
            if (employee.getLastName() != null) {
                existingEmployee.setLastName(employee.getLastName());
            }
            if (employee.getEmail() != null) {
                // Check if new email is unique (if changed)
                if (!existingEmployee.getEmail().equals(employee.getEmail())) {
                    employee emailCheck = employeeRepository.findByEmail(employee.getEmail());
                    if (emailCheck != null) {
                        return new ResponseEntity<>(HttpStatus.CONFLICT);
                    }
                    existingEmployee.setEmail(employee.getEmail());
                }
            }
            if (employee.getDepartment() != null) {
                existingEmployee.setDepartment(employee.getDepartment());
            }
            if (employee.getPosition() != null) {
                existingEmployee.setPosition(employee.getPosition());
            }
            if (employee.getSalary() != null) {
                existingEmployee.setSalary(employee.getSalary());
            }
            if (employee.getHireDate() != null) {
                existingEmployee.setHireDate(employee.getHireDate());
            }
            if (employee.getPhoneNumber() != null) {
                existingEmployee.setPhoneNumber(employee.getPhoneNumber());
            }
            if (employee.getAddress() != null) {
                existingEmployee.setAddress(employee.getAddress());
            }

            employee updatedEmployee = employeeRepository.save(existingEmployee);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 5. DELETE EMPLOYEE
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable("id") Long id) {
        try {
            if (employeeRepository.existsById(id)) {
                employeeRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 6. DELETE ALL EMPLOYEES
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllEmployees() {
        try {
            employeeRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 7. GET EMPLOYEES BY DEPARTMENT
    @GetMapping("/department/{department}")
    public ResponseEntity<List<employee>> getEmployeesByDepartment(@PathVariable("department") String department) {
        try {
            List<employee> employees = employeeRepository.findByDepartment(department);
            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 8. GET EMPLOYEES BY POSITION
    @GetMapping("/position/{position}")
    public ResponseEntity<List<employee>> getEmployeesByPosition(@PathVariable("position") String position) {
        try {
            List<employee> employees = employeeRepository.findByPosition(position);
            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 9. GET EMPLOYEES BY SALARY RANGE
    @GetMapping("/salary-range")
    public ResponseEntity<List<employee>> getEmployeesBySalaryRange(
            @RequestParam("min") Double minSalary,
            @RequestParam("max") Double maxSalary) {
        try {
            List<employee> employees = employeeRepository.findBySalaryBetween(minSalary, maxSalary);
            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 10. SEARCH EMPLOYEES BY NAME
    @GetMapping("/search")
    public ResponseEntity<List<employee>> searchEmployeesByName(@RequestParam("name") String name) {
        try {
            List<employee> employees = employeeRepository.findByFirstNameContainingIgnoreCase(name);
            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}