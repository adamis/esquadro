import { Component, OnInit } from '@angular/core';
import { Router,NavigationEnd  } from '@angular/router';
import { environment } from './../environments/environment';

// declare ga as a function to access the JS code in TS
declare var gtag: Function;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {

	constructor(private router: Router) {
		
		gtag('js', new Date());
		gtag('config', environment.analitycs);
		
	 }
    ngOnInit() {
    }

    exibindoNavbar() {
      return (this.router.url !== '/login' && this.router.url !== '/');
    }
}
