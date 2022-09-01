package com.javaproject.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaproject.takeout.common.R;
import com.javaproject.takeout.entity.Employee;
import com.javaproject.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     *  Employee login
      * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // MD5
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // check username in database
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if (emp == null) return R.error("Login failed");
        if (!emp.getPassword().equals(password)) return R.error("Login failed");

        // check employee status
        if (emp.getStatus() == 0) return R.error("Account locked");

        // login success and save to session
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

    }

    /**
     * logout
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // clear userid from session
        request.getSession().removeAttribute("employee");
        return R.success("logout success");
    }

    /**
     * add new employee
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("add member : {}", employee.toString());

        // set initial password and md5
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());

        // get current user
        //Long empId = (Long) request.getSession().getAttribute("employee");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("add success");
    }

    /**
     * employee info
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        // offset constructor
        Page pageInfo = new Page(page, pageSize);

        // condition constructor
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        // add filter
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);

        // add rank
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // execute
        employeeService.page(pageInfo, queryWrapper);
        
        return R.success(pageInfo);
    }

    /**
     * change employee info based on id
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());

        //Long empId = (Long) request.getSession().getAttribute("employee");
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(empId);

        long id = Thread.currentThread().getId();
        log.info("current thread is {}", id);

        employeeService.updateById(employee);
        return R.success("modification success");
    }

    /**
     * query employee in database
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("query in database..");
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("not found");
    }
    
}
