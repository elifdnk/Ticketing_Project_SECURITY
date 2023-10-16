package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper,@Lazy UserService userService, UserMapper userMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @Override
    public List<TaskDTO> listAllTasks() {
        return taskRepository.findAll().stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void save(TaskDTO dto) {
       dto.setTaskStatus(Status.OPEN);
       dto.setAssignedDate(LocalDate.now());
//       Task task = taskMapper.convertToEntity(dto);
//       taskRepository.save(task);
        taskRepository.save(taskMapper.convertToEntity(dto));
    }

    @Override
    public void update(TaskDTO dto) {

        Optional<Task> task = taskRepository.findById(dto.getId());
        Task convertedTask = taskMapper.convertToEntity(dto);
        if(task.isPresent()){
            convertedTask.setTaskStatus(dto.getTaskStatus() == null ? task.get().getTaskStatus() : dto.getTaskStatus());
            convertedTask.setAssignedDate(task.get().getAssignedDate());
            taskRepository.save(convertedTask);
        }
    }

    @Override
    public void delete(Long id) {

        Optional<Task> foundTask = taskRepository.findById(id);
        if(foundTask.isPresent()){
            foundTask.get().setIsDeleted(true);
            taskRepository.save(foundTask.get());
        }
    }

    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()){
            return taskMapper.convertToDto(task.get());
        }
        return null;
    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO projectDTO) {

        //we convert to entity because we will search related task in repository
        Project project = projectMapper.convertToEntity(projectDTO);

        //we want to list which tasks will be deleted
        List<Task> tasks = taskRepository.findAllByProject(project);

        //we want to delete all tasks one by one
        tasks.forEach(task -> delete(task.getId()));

    }

    @Override
    public void completeByProject(ProjectDTO projectDTO) {
        //we convert to entity because we will search related task in repository
        Project project = projectMapper.convertToEntity(projectDTO);

        //we want to list which tasks will be completed
        List<Task> tasks = taskRepository.findAllByProject(project);


        //we want to update set status complete all tasks one by one
        //map(taskMapper::convertToDto) we use this because our update method needs DTO
        tasks.stream().map(taskMapper::convertToDto).forEach(taskDTO ->{
                    taskDTO.setTaskStatus(Status.COMPLETE);
                    update(taskDTO);
                });
    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {

        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO loggedInUser = userService.findByUserName(username);
        List<Task> tasks = taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status,userMapper.convertToEntity(loggedInUser));
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO loggedInUser = userService.findByUserName(username);
        List<Task> tasks = taskRepository.findAllByTaskStatusAndAssignedEmployee(status,userMapper.convertToEntity(loggedInUser));
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllNonCompletedByAssignedEmployee(UserDTO assignedEmployee) {
        List<Task> projects = taskRepository
                .findAllByTaskStatusIsNotAndAssignedEmployee(Status.COMPLETE,userMapper.convertToEntity(assignedEmployee));
        return projects.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }
}
