import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { MenuItem } from 'src/app/interface/menu-item';
import * as Highcharts from 'highcharts';
import { Router } from 'src/app/interface/router';

@Component({
  selector: 'app-menu-items',
  templateUrl: './menu-items.component.html',
  styleUrls: ['./menu-items.component.scss']
})
export class MenuItemsComponent implements OnInit {

  @Input() position: number = 0;
  @Input() Highcharts: typeof Highcharts = Highcharts; // required
  @Input() router: Router = new Router("", -1);

  option: MenuItem = new MenuItem();

  constructor() {
  }

  ngOnInit(): void {
    
  }
}