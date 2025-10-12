package EmployeePractice.example;

import EmployeePractice.exampledomain.Admin;
import EmployeePractice.exampledomain.Director;
import EmployeePractice.exampledomain.Employee;
import EmployeePractice.exampledomain.Engineer;
import EmployeePractice.exampledomain.Manager;

public class EmployeeTest {
  public static void main(String[] args) {
    Employee[] emps=new Employee[5];
    emps[0]= new Employee("Sean","A123456789",50000);
    emps[1]= new Admin("Amy","B65789211",70000);
    emps[2]= new Engineer("David","C9988776655",80000);
    emps[3]= new Manager("Louis","D369854712",100000,"Tw Sales");
    emps[4]= new Director("Nicole","E1596358741",120000,"Global Sales",1000000);
    
    // for (int i=0;i<emps.length;i++)
    // System.out.println(emps[i]);
    // System.out.println("David 學會Java,Android");
    if (emps[2] instanceof Engineer){
      Engineer eng=(Engineer)emps[2];
      eng.addSkill("Java");
      eng.addSkill("Android");
    }
    emps[2].raisesalary(2000);
    System.out.println("部門分配............");
    if (emps[3] instanceof Manager){
      Manager m1=(Manager)emps[3];
      m1.addEmployee(emps[0]);
      m1.addEmployee(emps[1]);
      m1.addEmployee(emps[2]);
    }
    if (emps[4] instanceof Manager)
      ((Manager)emps[4]).addEmployee(emps[3]);
    for (int i=0;i<emps.length;i++)
      System.out.println(emps[i]);
  }
  
}
