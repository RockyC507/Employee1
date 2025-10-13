package exampledomain;

import java.util.ArrayList;

public class Manager extends Employee implements RegularStaff{
  private static int baseBonus=100000;
  private String depName;
  protected ArrayList employees;
  public Manager(String name,String ssn,double salary,String depName){
    super(name,ssn,salary);
    this.depName=depName;
    employees = new ArrayList();
  }
  public String getDeptName(){
    return depName;
  }
  public boolean addEmployee(Employee e){
    if (employees.contains(e))
      return false;
    employees.add(e);
    return true;
  }
  public boolean removeEmployee(Employee e){
    if (!employees.contains(e))
      return false;
    employees.remove(e);
      return true;
  }
  public double getPay(){
    return getsalary()+employees.size()*2000;
  }
  public double getBonus(){
    return baseBonus* calcPerMultiplier();
  }
  public String getStaffDetails(){
    StringBuilder sb= new StringBuilder();
    if (!employees.isEmpty()){
        sb.append("\n管理員工:");
        for (Object obj : employees){
          if (obj instanceof Employee){
            Employee e =(Employee)obj;
            sb.append(String.format("%s(%d)",e.getName(),e.getEmpId()));
          }
      
        }
    }
       return sb.toString();
  }
  
  @Override
  public String toString() {
    return super.toString()+"\n管理部門"+
    this.depName+
    getStaffDetails();
  }
 
}
