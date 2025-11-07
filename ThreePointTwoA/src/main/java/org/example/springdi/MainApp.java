package org.example.springdi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {

    public static void main(String[] args) {
        // 1. Initialize the Spring context using our Java-based configuration
        System.out.println("Initializing Spring Context...");
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println("Spring Context Initialized.");

        // 2. Retrieve the "student" bean from the context
        // Spring automatically names the bean after the @Bean method name
        Student myStudent = context.getBean("student", Student.class);

        // 3. Call the method on the bean to show the dependency was injected
        myStudent.startStudying();

        // 4. (Optional) Close the context
        ((AnnotationConfigApplicationContext) context).close();
    }
}
