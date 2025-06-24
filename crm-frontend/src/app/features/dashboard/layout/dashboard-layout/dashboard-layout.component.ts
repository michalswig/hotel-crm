import { Component } from '@angular/core';
import {HeaderComponent} from '../header/header.component';
import {SidebarComponent} from '../sidebar/sidebar.component';
import {RouterOutlet} from '@angular/router';
import {FooterComponent} from '../footer/footer.component';

@Component({
  selector: 'app-dashboard-layout',
  imports: [
    HeaderComponent,
    SidebarComponent,
    RouterOutlet,
    FooterComponent
  ],
  templateUrl: './dashboard-layout.component.html',
  styleUrl: './dashboard-layout.component.scss'
})
export class DashboardLayoutComponent {

}
