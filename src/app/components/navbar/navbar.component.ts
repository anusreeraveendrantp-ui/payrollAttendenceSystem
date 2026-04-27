import { Component } from '@angular/core';
import { EmployeeService } from '../../services/employee.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  employee: any;

  constructor(private employeeService: EmployeeService, private router: Router) {}

  ngOnInit() {
    this.employee = this.employeeService.getEmployee();
    
  }
  goToAttendence(){
    this.router.navigate(['/attendance']);
  }
  goToPayroll(){
    this.router.navigate(['/payroll']);
  }
  goToLeave(){
    this.router.navigate(['/leave']);
  }
}
