import { Component, Input, OnInit } from '@angular/core';
import { Router } from 'src/app/interface/router';

@Component({
  selector: 'app-router',
  templateUrl: './router.component.html',
  styleUrls: ['./router.component.scss']
})
export class RouterComponent implements OnInit {

  @Input()
  router: Router | undefined;

  constructor() {
   }

  ngOnInit(): void {
  }

}
