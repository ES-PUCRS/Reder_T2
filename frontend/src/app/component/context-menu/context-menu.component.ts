import { Component, Input, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';
import { Router } from 'src/app/interface/router';

@Component({
  selector: 'app-context-menu',
  templateUrl: './context-menu.component.html',
  styleUrls: ['./context-menu.component.scss']
})
export class ContextMenuComponent implements OnInit {
  menuOptions: Array<string> = ["configure_module","create_module","connect_router","disconnect_router","delete_router"];

  @Input() x = 0;
  @Input() y = 0;
  @Input() router: Router = new Router("", -1);
  @Input() Highcharts: typeof Highcharts = Highcharts; // required

  constructor() {

  }

  ngOnInit(): void {
    
  }

}
