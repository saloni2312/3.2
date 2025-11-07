package org.example.springdi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /**
     * Defines a bean for the Course.
     * The method name "course" will be the default bean ID.
     */
    @Bean
    public Course course() {
        System.out.println("Creating Course bean...");
        return new Course();
    }

    /**
     * Defines a bean for the Student.
     * Spring sees that this method requires a 'Course' object as a parameter.
     * It automatically looks for a @Bean that provides a Course (i.e., the course() method above)
     * and injects it here. This is Dependency Injection.
     */
    @Bean
    public Student student(Course course) {
        System.out.println("Creating Student bean...");
        // Spring passes the 'course' bean created above
        return new Student(course);
    }
}