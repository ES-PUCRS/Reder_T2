import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-context-menu',
  templateUrl: './context-menu.component.html',
  styleUrls: ['./context-menu.component.scss']
})
export class ContextMenuComponent implements OnInit {
  menuOptions: Array<string> = ["configure_module","connect_router","create_module","delete_router"];

  @Input() x = 0;
  @Input() y = 0;
  @Input() router: any;

  constructor() {

  }

  ngOnInit(): void {
  }

}
