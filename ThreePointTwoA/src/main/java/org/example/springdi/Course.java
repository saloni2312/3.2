package org.example.springdi;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Course {

    public String getCourseName() {
        return "Computer Science";
    }

    public void study() {
        System.out.println("Studying " + getCourseName() + "...");
    }
}