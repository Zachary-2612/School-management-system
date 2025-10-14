package School.Management.System;
import java.time.Year;
import java.util.HashMap;

/**
 * represent grade, the tuition fees the student need to pay
 */

public class Student extends Person{
    private int grade;
    private int feesPay;
    private int totalTuitionFees;
    private Year latestPayment;

    public Student( int id, String name,int grade, int feesPay, int totalTuitionFees) {
        super(id, name);
        this.grade = grade;
        this.feesPay = feesPay;
        this.totalTuitionFees = totalTuitionFees;
    }


    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }


    public void showPaymentStatus() {
        System.out.println(getId() + " " + getName() + " has paid " + getFeesPay() + "$" + " and the total fees is " + getTotalTuitionFees());
    }

    /**
     * update the tuition fees the student had paid
     * set the lastPayment to the student avoid duplicate payment at the same year
     *update the school income
     */

    public void updateFeesPay (int payfees) throws PaymentException {
        // show the current year
        Year year = Year.now();
        // find if pay at the same year
        if(getLatestPayment() != null && getLatestPayment().equals(year)) {
            System.out.println(getName() + " has paid the tuition fees this year");
            return;
        }
        // calculate the remaining fees
        int remain = totalTuitionFees - getFeesPay();
        // check the payment process
        if(payfees > remain) {
            throw new PaymentException("Exceed the fees you need to pay and the remaing fee is " + remain);
        } else if(payfees <= 0) {
            throw new PaymentException("Error pay with no money transfer");
        }
        else {
            feesPay += payfees;
            School.updateTotalMoneyEarned(payfees);
            School.addLog(getId() + " " + getName() + " pay " + payfees + "$");
            System.out.println(getId() + " " + getName() + " pay " + payfees + "$");
        }
        // if student pay all of the fees then set the latestPayment
        if(getFeesPay() == totalTuitionFees) {
            setLatestPayment(year);
            System.out.println("Student had paid all of the fees");
        }
    }

    public int getFeesPay() {
        return feesPay;
    }

    public int getTotalTuitionFees(){
        return totalTuitionFees;
    }

    public Year getLatestPayment() {
        return latestPayment;
    }


    public void setLatestPayment(Year latestPaid) {
        this.latestPayment = latestPaid;
    }


    public String toString() {
        return getName() + " in " + getGrade() + " grade should pay " + getTotalTuitionFees();
    }
}
