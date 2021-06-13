import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-router',
  templateUrl: './router.component.html',
  styleUrls: ['./router.component.scss']
})
export class RouterComponent implements OnInit {

  @Input()
  router_port: string = "";

  constructor() { }

  ngOnInit(): void {
  }

}
