package exampledomain;

public class Admin extends Employee {
  private int hours=160;
  public Admin(String name,String ssn,double salary){
    super(name,ssn,salary);
  }
  public int getHours() {
    return hours;
  }
  public void setHours(int hours) {
    this.hours = hours;
  }
  public double getPay(){
    return this.getsalary()*hours/160;
  }
}
