import {Component, OnInit} from '@angular/core';
import {MatToolbar} from '@angular/material/toolbar';
import {MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {AuthService} from '../../../../core/auth/auth.service';
import {User, UserService} from '../../../../shared/services/user.service';

@Component({
  selector: 'app-header',
  imports: [
    MatToolbar,
    MatIcon,
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
