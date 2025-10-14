package School.Management.System;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        // initialize the school
        School school = new School(new HashMap<>(), new HashMap<>(), 500000);

        // create teacher and student list
        List<Teacher> teacherList = school.readTeacher("teacher.txt");
        List<Student> studentList = school.readStudent("student.txt");

        // add the student to the school
        school.addStudent(studentList);
        school.addTeacher(teacherList);

        // load the transition and financial file
        school.readTransition("transition.txt");
        school.readFinancial("financial.txt");

        // create a scanner for input
        Scanner scanner = new Scanner(System.in);


        boolean isRun = true;

        while(isRun) {
            printMenu();

            int option = scanner.nextInt();

            switch(option) {
                case 0:
                    isRun = exit();
                    break;

                case 1:
                    studentPay(school,studentList,scanner);
                    break;

                case 2:
                   payTeacherSlary(school,teacherList,scanner);
                    break;

                case 3:
                    showTransitionHistory(school);
                    break;

                case 4:
                    showStudentPayAll(school,scanner);
                    break;

                case 5:
                    showFinancial(school);
                    break;

                case 6:
                   addStudent(school,studentList,scanner);
                    break;

                case 7:
                    addTeacher(school,teacherList,scanner);

                    break;
                default:
                    System.out.println("Invalid option please enter again");
            }
        }
    }
    public static void printMenu() {
        System.out.println("==================WELCOME ACCESS TO THE SCHOOL MANAGEMENT SYSTEM======================");
        System.out.println("0: exit");
        System.out.println("1: search student and pay fees");
        System.out.println("2: pay salary to teacher");
        System.out.println("3: show transaction history");
        System.out.println("4: show if student pay all");
        System.out.println("5: show financial statement");
        System.out.println("6: add new students");
        System.out.println("7: add new teachers");
        System.out.println("=======================================================================================");
    }

    public static boolean exit() {
        System.out.println("You exit the system");
        return false;
    }

    public static void studentPay(School school, List<Student> studentList, Scanner scanner){
        scanner.nextLine();
        System.out.println("Please enter the name to search");
        String name = scanner.nextLine();
        Student student = school.getStudent(name);
        if (student != null) {
            System.out.println("student " + name + " is found");
            System.out.println("Please enter the amount you pay");
            int amount = scanner.nextInt();
            try {
                student.updateFeesPay(amount);
                school.storeStudent(studentList, "student.txt");
                school.storeFinancial("financial.txt");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Student " + name + " not Found");
        }
    }

    public static void payTeacherSlary(School school, List<Teacher> teacherList, Scanner scanner) {
        scanner.nextLine();
        System.out.println("Enter teacher's name to pay");
        String teacherName = scanner.nextLine();
        Teacher teacher = school.getTeacher(teacherName);
        if (teacher != null) {
            System.out.println("Teacher " + teacher.getName() + " is found");
            try {
                school.paySalary(teacher);
                school.storeTeacher(teacherList, "teacher.txt");
                school.storeFinancial("financial.txt");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Teacher " + teacherName + " not found");
        }
    }

    public static void showTransitionHistory(School school) {
        school.showLog();
        System.out.println();
        System.out.println("==============================================================");
    }

    public static void showStudentPayAll(School school, Scanner scanner) {
        scanner.nextLine();
        System.out.println("Please enter student's name");
        String studentName = scanner.nextLine();

        Student student1 = school.getStudent(studentName);
        if (student1 != null) {
            school.studentPayAll(student1);
        } else {
            System.out.println("Student " + studentName + " not Found");
        }
    }

    public static void showFinancial(School school) {
        school.getFinancial();
    }

    public static void addStudent(School school, List<Student> studentList, Scanner scanner) {
        System.out.println("Please enter the student id");
        int newId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Please enter the name");
        String newName = scanner.nextLine();
        System.out.println("Please enter the grade");
        int newGrade = scanner.nextInt();
        System.out.println("Please enter the totalFees");
        int newFees = scanner.nextInt();
        scanner.nextLine();

        Student newStudent = new Student(newId,newName,newGrade,0,newFees);
        studentList.add(newStudent);
        school.addStudent(studentList);
        school.storeStudent(studentList, "student.txt");
        System.out.println("You add student " + newName);
    }

    public static void addTeacher(School school, List<Teacher> teacherList, Scanner scanner) {
        System.out.println("Please enter the teacher id");
        int newId1 = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Please enter the name");
        String newName1 = scanner.nextLine();
        System.out.println("Please enter the salary");
        int salary = scanner.nextInt();
        scanner.nextLine();
        Teacher teacher1 = new Teacher(newId1,newName1,salary);
        teacherList.add(teacher1);
        school.addTeacher(teacherList);
        school.storeTeacher(teacherList, "teacher.txt");
        System.out.println("You add teacher " + newName1);
    }

}
