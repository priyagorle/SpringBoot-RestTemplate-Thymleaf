package com.example.demoApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class EmpController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping
    public String getAllEmployees(Model model)
    {
        List<EmployeeEntity> list = restTemplate.exchange("http://localhost:9090/", HttpMethod.GET, getEntity(), List.class).getBody();
        model.addAttribute("employees", list);
        return "list-employees";
    }

    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String editEmployeeById(Model model, @PathVariable("id") Optional<Long> id)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        EmployeeEntity emp = new EmployeeEntity();
        if (id.isPresent()) {
            emp.setId(id.get());
            HttpEntity entity = new HttpEntity(emp, headers);
            emp = restTemplate.exchange("http://localhost:9090/edit", HttpMethod.POST, entity, EmployeeEntity.class).getBody();
        }
        model.addAttribute("employee", emp);
        return "add-edit-employee";
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteEmployeeById(Model model, @PathVariable("id") Long id)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        EmployeeEntity emp = new EmployeeEntity();
        emp.setId(id);
        HttpEntity entity = new HttpEntity(emp,headers);
        restTemplate.exchange("http://localhost:9090/delete", HttpMethod.POST, entity, String.class).getBody();
        return "redirect:/";
    }

    @RequestMapping(path = "/createEmployee", method = RequestMethod.POST)
    public String createOrUpdateEmployee(EmployeeEntity employee)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(employee,headers);
        restTemplate.exchange("http://localhost:9090/createEmployee", HttpMethod.POST, entity, EmployeeEntity.class);
        return "redirect:/";
    }

    public HttpEntity getEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return new HttpEntity<String>(headers);
    }
}
