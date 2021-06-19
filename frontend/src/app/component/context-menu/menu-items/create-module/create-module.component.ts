import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'src/app/interface/menu-item';
import { MenuItemsComponent } from '../shared/menu-items.component';

@Component({
  selector: 'create-module-option',
  templateUrl: '../shared/menu-items.component.html',
  styleUrls: ['../shared/menu-items.component.scss']
})
export class CreateModuleComponent extends MenuItemsComponent implements OnInit {

  
  constructor() {
    super();
    const menuItem = new MenuItem();

    menuItem.name = "Create Module";
    menuItem.operation = this.operator;
    menuItem.dropdown = [];
    menuItem.dropdownOpen = false;
    super.option = menuItem;
  }

  ngOnInit(): void {
  }

  operator = () => {
    console.log('Create module TODO');
  }
}
