package com.example.CRUD_Operation.serviceImpl;

import com.example.CRUD_Operation.entity.Employee;
import com.example.CRUD_Operation.repository.EmployeeRepository;
import com.example.CRUD_Operation.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Override
    public ResponseEntity<?> addEmployee(Employee employee) {
        Optional<Employee> emailExist=employeeRepository.findByEmail(employee.getEmail());
        if(emailExist.isPresent())
        {
            return ResponseEntity.ok("Email already registered");
        }
        Employee employees=employeeRepository.save(employee);
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<?> getAllEmployee() {
       List<Employee> employees=employeeRepository.findAll();
       if(employees.isEmpty())
       {
           ResponseEntity.ok("Employee are not present");
       }
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<?> deleteEmployee(Integer employeeId) {
        Optional<Employee> idExist=employeeRepository.findById(employeeId);
        if(idExist.isEmpty())
        {
            return ResponseEntity.ok("Id does not exist");
        }
        employeeRepository.deleteById(employeeId);
        return ResponseEntity.ok("Employee data deleted successfully");
    }

    @Override
    public ResponseEntity<?> updateEmployee(Employee employee,Integer employeeId) {
        Optional<Employee> existId=employeeRepository.findById(employeeId);
            if(existId.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            Optional<Employee> existEmail=employeeRepository.findByEmail(employee.getEmail());
            if(existEmail.isPresent() && !employee.getEmployeeId().equals(employeeId))
            {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }
            Employee existingEmployee=existId.get();
        existingEmployee.setName(employee.getName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setJobRole(employee.getJobRole());
        existingEmployee.setSalary(employee.getSalary());
        employeeRepository.save(existingEmployee);

        return ResponseEntity.status(HttpStatus.OK).body(existingEmployee);
    }
}
