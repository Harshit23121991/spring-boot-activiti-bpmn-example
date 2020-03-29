package org.activiti;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GiroProcessRestController {
	
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;
    
  /*  @Autowired
    private ApplicantRepository applicantRepository;*/

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/giro-process", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String startGiroProcess() {

       /* Applicant applicant = new Applicant(data.get("name"), data.get("email"), data.get("phoneNumber"));
        applicantRepository.save(applicant);*/

       // Map<String, Object> vars = Collections.<String, Object>singletonMap("applicant", applicant);
        runtimeService.startProcessInstanceByKey("Giro");
        
        /*return "Process started. Number of currently running"
        + "process instances = "
        + runtimeService.createProcessInstanceQuery().count();*/
        
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
        	      .orderByProcessInstanceId()
        	      .desc()
        	      .list()
        	      .get(0);
        return pi.getId();
    }
    
    //@GetMapping("/get-tasks/{processInstanceId}")
    @RequestMapping(value = "/get-tasks/{processInstanceId}", method = RequestMethod.GET)
    public List<TaskRepresentation> getTasks(@PathVariable String processInstanceId) {
        List<Task> usertasks = taskService.createTaskQuery()
          .processInstanceId(processInstanceId)
          .list();

        return usertasks.stream()
          .map(task -> new TaskRepresentation(task.getId(), task.getName(), task.getProcessInstanceId()))
          .collect(Collectors.toList());
    }

   // @GetMapping("/complete-task-A/{processInstanceId}")
    @RequestMapping(value = "/complete-task-A/{processInstanceId}", method = RequestMethod.GET)
    public void completeTaskA(@PathVariable String processInstanceId) {
        Task task = taskService.createTaskQuery()
          .processInstanceId(processInstanceId)
          .singleResult();
        taskService.complete(task.getId());
        System.out.println("Task completed");
    }
    
    @RequestMapping(value = "/next-task/{processInstanceId}", method = RequestMethod.GET)
    public  List<Task> getNextTask(@PathVariable String processInstanceId) {
    	
    	/*Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskCandidateGroup("Legaldata")
                .singleResult();*/
    	
    	 List<Task> tasks = taskService.createTaskQuery()
                 .processInstanceId(processInstanceId)
                 .orderByTaskName().asc()
                 .list();
       // taskService.complete(task.getId());
       // System.out.println("Task completed");
    	return tasks;
    }

}
