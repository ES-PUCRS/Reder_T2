import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'src/app/interface/menu-item';

@Component({
  selector: 'app-menu-items',
  templateUrl: './menu-items.component.html',
  styleUrls: ['./menu-items.component.scss']
})
export class MenuItemsComponent implements OnInit {

  option: MenuItem;
  constructor() {
    this.option = new MenuItem();
  }

  ngOnInit(): void {
  }
}