package com.example.demo.DAO;
import java.sql.*;
public interface TodoDAO {
    public void createTasksTable() ;
    public void deleteTask(String taskName);
    public void insertTask(String task, boolean completed);
    public void updateTaskCompleted(String taskName, boolean currentCompletedStatus) ;

    Connection getConnection();

    void closeConnection();
}

