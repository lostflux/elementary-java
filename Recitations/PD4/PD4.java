
/* interfaces */
import java.util.List;
import java.util.Map;
import java.util.Set;

/* implementations (classes) */
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PD4 {
    public static void main(String[] args) {

        // Create Map
        Map<String, List<String>> courses = new HashMap<>();

        // Add yourself to Map, with zero courses.
        courses.put("Amittai", new LinkedList<>());
        System.out.println(courses);

        // Add CS-10 and CS-30 to your list of courses.
        courses.get("Amittai").add("CS-10");
        courses.get("Amittai").add("CS-30");
        System.out.println(courses);

        // Add friend, register them for CS-10 and Math-22
        List<String> f1courses = new ArrayList<>();
        f1courses.add("CS-10");
        f1courses.add("Math-22");
        courses.put("Friend 1", f1courses);
        System.out.println(courses);

        // Add second friend with Math 22
        courses.put("Friend 2", new ArrayList<>());
        courses.get("Friend 2").add("Math-22");
        System.out.println(courses);



        // Print all students
        System.out.println("=========\n\nAll students in the map:\n");
        courses.keySet().forEach(System.out::println);

        // alternatively,
        System.out.println("=========\n\nAll students in the map:\n");
        for (String student : courses.keySet()) {
            System.out.println(student);
        }



        // Print all classes
        System.out.println("=========\n\nAll classes in the map:\n");
        Set<String> uniqueClasses = new HashSet<>();
        courses.values().forEach(uniqueClasses::addAll);
        System.out.println(uniqueClasses);

        // alternatively
        System.out.println("=========\n\nAll classes in the map:\n");
        uniqueClasses = new HashSet<>();
        for (List<String> studentCourses : courses.values()) {
            for (String course : studentCourses) {
                uniqueClasses.add(course);
            }
        }
        System.out.println(uniqueClasses);



        // Print all students registered for CS-10
        System.out.println("=========\n\nStudents registered for CS-10:\n");
        courses.forEach((student, coursesRegistered) -> {
            if (coursesRegistered.contains("CS-10")) {
                System.out.println(student);
            }
        });

        // alternatively;
        System.out.println("=========\n\nStudents registered for CS-10:\n");
        for (String student : courses.keySet()) {
            if (courses.get(student).contains("CS-10")) {
                System.out.println(student);
            }
        }
    }
}
