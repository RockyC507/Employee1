package exampledomain;

public class Director extends Manager {
  private static int baseBonus=500000;
  private double budget;
  public Director(String name,String ssn,double salary,Branch branch,String depName,double budget){
    super(name, ssn, salary,branch, depName);
    this.budget=budget;
  }
  public double getBudget(){
    return budget;
  }
  public double getBonus(){
    return baseBonus* calcPerMultiplier();
  }
  @Override
  public String toString() {
    return super.toString()+"\n管理預算:"+this.getBranch().getCurrency()+formatter.format(budget)+"元";
  }
  
  public double getPay(){
    return this.getsalary()+employees.size()*10000;
  }

}
