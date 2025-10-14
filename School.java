package School.Management.System;
import java.io.*;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;

/**
 * represent the school management system
 * add students or teachers
 * record the financial and transition log
 */


public class School {
    private static int moneyEarned;
    private static int moneySpent;
    private static int overDraft;
    private static List<String> logTransaction;
    private Map<String, Teacher> teachers;
    private Map<String, Student> students;

    public School(Map<String, Teacher> teachers,Map<String, Student> students, int overDraft) {
        this.teachers = new HashMap<>();
        this.students = new HashMap<>();
        this.moneyEarned = 0;
        this.moneySpent = 0;
        logTransaction = new ArrayList<>();
        this.overDraft = overDraft;
    }

    // add teachers from the list
    public void addTeacher(List<Teacher> teacherList) {
        for(Teacher t : teacherList) {
            teachers.put(t.getName(), t);
        }
    }

    // add students from the list
    public void addStudent(List<Student> studentList) {
        for(Student s : studentList) {
            students.put(s.getName(), s);
        }
    }

    public Student getStudent(String name) {
        return students.get(name);
    }

    public Teacher getTeacher(String name) {
        return teachers.get(name);
    }

    // update the money earn by school
    public static void updateTotalMoneyEarned(int moneyEarn) {
        int balance = moneyEarned - moneySpent;


        if(balance < 0) {
            int debt = -balance;
            if(moneyEarn > debt) {
                overDraft += debt;
                moneyEarned += moneyEarn - debt;
            } else {
                overDraft += moneyEarn;
            }
        } else {
            moneyEarned += moneyEarn;
        }

    }

    public static void updateMoneySpent(int moneySpend) {
        moneySpent += moneySpend;
    }


    public int getMoneyEarned() {
        return moneyEarned;
    }

    public int getMoneySpent() {
        return moneySpent;
    }


    public int getBalance() {
        int balance = getMoneyEarned() - getMoneySpent() + overDraft;
        return balance;
    }

    // print the financial chart
    public void getFinancial() {
        LocalDateTime time = LocalDateTime.now();
        System.out.println("===============================================");
        System.out.println("                 FINANCIAL REPORT              ");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Time : " + time);
        System.out.println("Balance: " + getBalance() + "$");
        System.out.println("TotalIncome: " + getMoneyEarned() + "$");
        System.out.println("TotalSpent: " + getMoneySpent() + "$");
        System.out.println("OverDraft: " + overDraft + "$");
        System.out.println("==================================================");

        storeFinancial("financial.txt");
    }

    /*
    pay salary to the teacher and if paid then set the latestPayment avoid pay duplicate at the same month
     */
    public void paySalary(Teacher teacher) throws PaymentException{
        // get the current month
        YearMonth currentMonth = YearMonth.now();

        // check if the teacher get the pay at the same month
        if(teacher.getLatestPayment() != null && teacher.getLatestPayment().equals(currentMonth)) {
            System.out.println(teacher.getName() + " has get paid this month");
            return;
        }
        int balance = getBalance();
        // check the payment process
        if(balance + overDraft < teacher.getSalary()) {
            throw new PaymentException("School has not enough money to pay!!!");
        }
        if(balance < teacher.getSalary()) {
            int overdraftReduce;

            if(balance > 0) {
                overdraftReduce = teacher.getSalary() - balance;
            } else {
                overdraftReduce = teacher.getSalary();
            }
            // update the school money spent
            updateMoneySpent(teacher.getSalary());
            // update the overdraft
            overDraft -= overdraftReduce;
            addLog(teacher.getName() + " get " + teacher.getSalary() + "$ paid through " + overdraftReduce + " $ overdraft" );
        } else {
            updateMoneySpent(teacher.getSalary());
            addLog("pay " + teacher.getSalary() + " to teacher " + teacher.getName());
        }
        teacher.setLatestPayment(currentMonth);
        System.out.println(teacher.getName() + " get " + teacher.getSalary() + "$ paid");
    }

    // check if student paid all the tuition fees
    public void studentPayAll(Student student) {
        int remain = student.getTotalTuitionFees() - student.getFeesPay();
        if(remain == 0) {
            System.out.println(student.getName() + " has paid all the tuition fees");
        } else {
            System.out.println(student.getName() + " still has " + remain + "$ unpaid");
        }
    }

    // add the transition log
    public static void addLog(String log) {
        // get the current time
        LocalDateTime time = LocalDateTime.now();

        // record the transition time
        String Log = "[" + time + "]" + log;

        logTransaction.add(Log);
        // store the log in the txt file make sure we can reuse next time
        storeTransition(Log);
    }

    // print the log
    public void showLog() {
        System.out.println("------------transaction record---------------");
        for(String s : logTransaction) {
            System.out.println(s);
        }
    }

    // store the teacher information  in the txt file
    public void storeTeacher(List<Teacher> teacher, String fileName) {
        String latestPaid = "";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("id,name,salary,latestPayment");
            bw.newLine();
            for(Teacher t : teacher) {
                if(t.getLatestPayment() != null) {
                    latestPaid = t.getLatestPayment().toString();
                }
                bw.write(t.getId() + "," + t.getName() + "," + t.getSalary() + "," + latestPaid);
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error to store " + e.getMessage());
        }
    }

    // load the teacher.txt file
    public List<Teacher> readTeacher(String fileName) {
        List<Teacher> teacherList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        String line;
        br.readLine();
        while((line = br.readLine()) != null) {
            String[] s = line.split(",");
            int id = Integer.parseInt(s[0]);
            int salary = Integer.parseInt(s[2]);
            String name = s[1];
            Teacher teacher = new Teacher(id,name,salary);
            if(s.length > 3 && !s[3].isEmpty()) {
                teacher.setLatestPayment(YearMonth.parse(s[3]));
            }
            teacherList.add(teacher);
            }
        } catch(Exception e) {
            System.out.println("Error read teacher " + e.getMessage());
        }
        return teacherList;
    }

    // store the student information in the txt file
    public void storeStudent(List<Student> student, String fileName) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("id,name,grade,feesPaid,totalFees,latestPayment");
            bw.newLine();
            for(Student s : student) {
                String lastPaid = "";
                if(s.getLatestPayment() != null) {
                    lastPaid = s.getLatestPayment().toString();
                }
                bw.write(s.getId() + "," + s.getName() + "," + s.getGrade() + "," + s.getFeesPay() + "," + s.getTotalTuitionFees() + "," + lastPaid);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error to store " + e.getMessage());
        }
    }

    // load the student.txt file
    public List<Student> readStudent(String fileName) {
        List<Student> studentList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine();
            while((line = br.readLine()) != null) {
                String[] s = line.split(",");
                int id = Integer.parseInt(s[0]);
                String name = s[1];
                int grade = Integer.parseInt(s[2]);
                int feesPay = Integer.parseInt(s[3]);
                int totalTuitionFees = Integer.parseInt(s[4]);
                Student student = new Student(id,name,grade,feesPay,totalTuitionFees);
                if(s.length > 5 && !s[5].isEmpty()) {
                    student.setLatestPayment(Year.parse(s[5]));
                }
                studentList.add(student);
            }
        } catch (IOException e) {
            System.out.println("Error to read " + e.getMessage());
        }
        return studentList;
    }

    // store the transition information in txt file
    public static void storeTransition(String log) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("transition.txt", true))) {
            bw.write(log);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error to store " + e.getMessage());
        }
    }
// load the transition.txt file
    public static List<String> readTransition(String fileName) {
        List<String> logs = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = br.readLine()) != null) {
                logs.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error to read " + e.getMessage());
        }
        logTransaction = logs;
        return logs;
    }

    // store the financial information in txt file
    public void storeFinancial(String fileName) {
        try(BufferedWriter br = new BufferedWriter(new FileWriter(fileName))) {
            br.write("moneyEarned,moneySpent,overDraft");
            br.newLine();
            br.write(moneyEarned + "," + moneySpent + "," + overDraft);
            br.newLine();
        }catch (IOException e) {
            System.out.println("Error to store " + e.getMessage());
        }
    }
// load the financial.txt file
    public void readFinancial(String fileName) {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine();
            String line;
            if((line = br.readLine()) != null) {
                String[] s = line.split(",");
                moneyEarned = Integer.parseInt(s[0]);
                moneySpent = Integer.parseInt(s[1]);
                overDraft = Integer.parseInt(s[2]);
            }
        }catch (IOException e) {
            System.out.println("Error to read " + e.getMessage());
        }
    }


}
