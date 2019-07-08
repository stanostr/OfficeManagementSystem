package com.stanostrovskii.model;

import java.util.Date;

/** Simplified object for transmitting tasks
 * @author Stan 
 */
public class TaskForm {
	private Long employeeId;
	private String taskName;
	private String description;
	private Date startDate;
	private Date dueDate;
	private boolean completed;
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String name) {
		this.taskName = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date endDate) {
		this.dueDate = endDate;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public static TaskForm fromTask(Task task)
	{
		TaskForm taskForm = new TaskForm();
		taskForm.setEmployeeId(task.getEmployee().getId());
		taskForm.setTaskName(task.getTaskName());
		taskForm.setDescription(task.getDescription());
		taskForm.setCompleted(task.isCompleted());
		taskForm.setStartDate(task.getStartDate());
		taskForm.setDueDate(task.getDueDate());
		return taskForm;
	}
	
	public Task toTask()
	{
		Task task = new Task();
		task.setTaskName(taskName);
		task.setDescription(description);
		task.setStartDate(startDate);
		task.setDueDate(dueDate);
		task.setCompleted(completed);
		return task;
	}
}
