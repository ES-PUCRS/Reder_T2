import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-context-menu',
  templateUrl: './context-menu.component.html',
  styleUrls: ['./context-menu.component.scss']
})
export class ContextMenuComponent implements OnInit {
  menuOptions: Array<String> = ["Connect Router", "Delete Router", "Configure Module", "New Module"];
  menuSubOptions: Array<String[]> = [["A", "B", "C"], [], ["A", "B", "C"], []];
  menuMethods: Array<any>;
  menuDropdownOpen: Array<boolean> = [false, false, false, false];

  @Input() x = 0;
  @Input() y = 0;
  @Input() router: any;

  constructor() {

    this.menuMethods = [
      () => { this.connectRouter(this.x) },
      () => { this.deleteRouter(this.y) },
      () => { this.configureModule(this.router) },
      () => { this.newModule(this.router) }
    ];

  }

  ngOnInit(): void {
  }

  connectRouter = function (test: any): void {
    alert("Connect Router");

    alert(test);
  }

  deleteRouter(test: any) {
    alert("Delete Router");

    alert(test);
  }

  configureModule(test: any) {
    alert("Configure Module");

    alert(test);
  }

  newModule(test: any) {
    alert("New Module");

    alert(test);
  }
}
