import {Component, OnInit} from '@angular/core';
import {MatToolbar} from '@angular/material/toolbar';
import {MatButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {NgIf} from '@angular/common';
import {AuthService} from '../../../../core/auth/auth.service';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [
    MatToolbar,
    MatButton,
    MatIcon,
    NgIf,
    RouterLink
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  user = JSON.parse(localStorage.getItem('user') ?? '{}');

  constructor(private readonly authService: AuthService) {
  }

  logout() {
    this.authService.logout();
  }

}
