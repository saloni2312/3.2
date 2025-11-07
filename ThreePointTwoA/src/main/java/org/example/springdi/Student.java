package org.example.springdi;

public class Student {

    // The Student class has a dependency on the Course class
    private final Course course;

    // The dependency is injected via the constructor
    public Student(Course course) {
        System.out.println("Student bean created. Injecting Course bean.");
        this.course = course;
    }

    public void startStudying() {
        if (course != null) {
            course.study();
        } else {
            System.out.println("No course to study.");
        }
    }
}
