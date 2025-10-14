package School.Management.System;
import java.time.YearMonth;

/**
 * represent the teacher salary and payment record
 */

public class Teacher extends Person{
    private int salary;
    private YearMonth latestPayment;

    public Teacher(int id, String name,int salary) {
        super(id, name);
        this.salary = salary;
        this.latestPayment = null;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }


    public YearMonth getLatestPayment() {
        return latestPayment;
    }

    public void setLatestPayment(YearMonth latestPaid) {
        this.latestPayment = latestPaid;
    }

    public String toString() {
        return getName() + " total salary is " + getSalary();
    }

}
