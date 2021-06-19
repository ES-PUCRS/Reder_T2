import { Component, Input, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';
import { Router } from 'src/app/interface/router';

@Component({
  selector: 'app-selector',
  templateUrl: './selector.component.html',
  styleUrls: ['./selector.component.scss']
})
export class SelectorComponent implements OnInit {

  @Input() component: string = "";
  @Input() position: number = 0;
  @Input() Highcharts: typeof Highcharts = Highcharts; // required
  @Input() router: Router = new Router("", -1);

  constructor() { }

  ngOnInit(): void {
    return;
  }

}
