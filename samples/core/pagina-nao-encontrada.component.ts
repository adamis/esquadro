import { Component, OnInit } from '@angular/core';

@Component({
  template: `
		<div class="not-found">
		  <img src="../../assets/404.png/" width="400px" height="auto" class="imgfound">
		<div>
		`,
  styles: [`
  html {
	  height:100% !important;
	  width: 100% !important;
  }
  
  .not-found { height: 100% !important; display:flex; align-items: center; justify-content:center; margin-top:10%; padding-bottom:15%;}

  @media (hover:none) {
	  .imgfound {
		  width:200px !important;
	  }
  }
  
  `]
})
export class PaginaNaoEncontradaComponent {
}
