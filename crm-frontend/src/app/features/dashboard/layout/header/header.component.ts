import {Component, OnInit} from '@angular/core';
import {MatToolbar} from '@angular/material/toolbar';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {NgIf} from '@angular/common';
import {AuthService} from '../../../../core/auth/auth.service';
import {RouterLink} from '@angular/router';
import {User, UserService} from '../../../../core/auth/user.service';

@Component({
  selector: 'app-header',
  imports: [
    MatToolbar,
    MatButton,
    MatIcon,
    NgIf,
    RouterLink,
    MatIconButton
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit{
  user: User | null = null;

  constructor(private readonly authService: AuthService, private  userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.user$.subscribe(user => this.user = user);
  }


  logout() {
    this.authService.logout();
  }

}
