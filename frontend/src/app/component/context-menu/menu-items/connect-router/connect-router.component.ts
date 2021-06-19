import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'src/app/interface/menu-item';
import { MenuItemsComponent } from '../shared/menu-items.component';

@Component({
  selector: 'connect-router-option',
  templateUrl: '../shared/menu-items.component.html',
  styleUrls: ['../shared/menu-items.component.scss']
})
export class ConnectRouterComponent extends MenuItemsComponent implements OnInit {

  constructor() {
    super();
    const menuItem = new MenuItem();

    menuItem.name = "Connect Router";
    menuItem.operation = this.operator;
    menuItem.dropdownOpen = false;
    super.option = menuItem;
    
    const submenu1 = new MenuItem();
    submenu1.name = "Test";
    submenu1.operation = this.operator;
    submenu1.dropdown = [];
    submenu1.dropdownOpen = false;

    menuItem.dropdown = [submenu1];
  }

  ngOnInit(): void {
  }

  operator = () => {
    console.log('Connect to router TODO');
  }
}
