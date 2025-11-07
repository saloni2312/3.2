package org.example.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class MainApp {

    public static void main(String[] args) {

        // Get the SessionFactory from our utility class
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // We will store the ID of the created student
        Integer studentId = null;

        // === 1. CREATE ===
        System.out.println("--- CREATING STUDENT ---");
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Student newStudent = new Student("John", "Doe", "john.doe@example.com");
            System.out.println("Saving: " + newStudent);

            // persist() is the JPA standard way to save a new entity
            session.persist(newStudent);

            tx.commit();

            // Get the auto-generated ID
            studentId = newStudent.getId();
            System.out.println("Student saved with ID: " + studentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // === 2. READ ===
        System.out.println("\n--- READING STUDENT ---");
        try (Session session = sessionFactory.openSession()) {
            // No transaction needed for a simple read

            // get() fetches the object by its Primary Key (ID)
            Student retrievedStudent = session.get(Student.class, studentId);

            if (retrievedStudent != null) {
                System.out.println("Retrieved: " + retrievedStudent);
            } else {
                System.out.println("Student not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // === 3. UPDATE ===
        System.out.println("\n--- UPDATING STUDENT ---");
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            // First, retrieve the student you want to update
            Student studentToUpdate = session.get(Student.class, studentId);

            if (studentToUpdate != null) {
                System.out.println("Updating email for: " + studentToUpdate.getFirstName());
                studentToUpdate.setEmail("john.doe.updated@example.com");

                // No need to call session.update() or merge()!
                // Hibernate's "Automatic Dirty Checking" will detect the change
                // and update the database when the transaction commits.
                tx.commit();
                System.out.println("Update complete.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // === 4. DELETE ===
        System.out.println("\n--- DELETING STUDENT ---");
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Student studentToDelete = session.get(Student.class, studentId);

            if (studentToDelete != null) {
                System.out.println("Deleting: " + studentToDelete);

                // remove() is the JPA standard way to delete
                session.remove(studentToDelete);

                tx.commit();
                System.out.println("Student deleted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // --- VERIFY DELETION (Read again) ---
        System.out.println("\n--- VERIFYING DELETE ---");
        try (Session session = sessionFactory.openSession()) {
            Student retrievedStudent = session.get(Student.class, studentId);
            if (retrievedStudent == null) {
                System.out.println("Student with ID " + studentId + " successfully deleted.");
            } else {
                System.out.println("Error: Student was not deleted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 5. Cleanly shut down the SessionFactory
        System.out.println("\nShutting down Hibernate...");
        HibernateUtil.shutdown();
        System.out.println("Done.");
    }
}