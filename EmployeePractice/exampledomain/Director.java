package EmployeePractice.exampledomain;

public class Director extends Manager {
  private double budget;
  public Director(String name,String ssn,double salary,String depName,double budget){
    super(name, ssn, salary, depName);
    this.budget=budget;
  }
  public double getBudget(){
    return budget;
  }
  @Override
  public String toString() {
    return super.toString()+"\n管理預算:"+formatter.format(budget)+"元";
  }
  
}
