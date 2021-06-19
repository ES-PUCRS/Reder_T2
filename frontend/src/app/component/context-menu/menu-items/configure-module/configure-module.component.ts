import { Component, Input, OnInit } from '@angular/core';
import { MenuItem } from 'src/app/interface/menu-item';
import { MenuItemsComponent } from '../shared/menu-items.component';

@Component({
  selector: 'configure-module-option',
  templateUrl: '../shared/menu-items.component.html',
  styleUrls: ['../shared/menu-items.component.scss']
})
export class ConfigureModuleComponent extends MenuItemsComponent implements OnInit {
  
  
  constructor() {
    super();
    const menuItem = new MenuItem();

    menuItem.name = "Configure Module";
    menuItem.operation = this.operator;
    menuItem.dropdown = [];
    menuItem.dropdownOpen = false;
    super.option = menuItem;
  }

  ngOnInit(): void {
  }

  operator = () => {
    console.log('Configure Module TODO');
  }
}
