import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {

  navLinks: any[];
  activeLinkIndex = -1; 
  
  constructor(private router: Router) {
    this.navLinks = [
        {
            label: 'Profile',
            path: './profile',
            index: 0
        }, {
            label: 'Settings',
            path: './settings',
            index: 1
        }, {
            label: 'Herbarium',
            path: './herbarium',
            index: 2
        }, 
    ];
}
ngOnInit(): void {
  this.router.events.subscribe((res) => {
      this.activeLinkIndex = this.navLinks.indexOf(this.navLinks.find(tab => tab.link === '.' + this.router.url));
  });
}

}
