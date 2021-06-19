import { Component, Input, OnInit } from '@angular/core';
import { MenuItem } from 'src/app/interface/menu-item';
import { MenuItemsComponent } from '../shared/menu-items.component';

@Component({
  selector: 'delete-router-option',
  templateUrl: '../shared/menu-items.component.html',
  styleUrls: ['../shared/menu-items.component.scss']
})
export class DeleteRouterComponent extends MenuItemsComponent implements OnInit {

  

  constructor() {
    super();
    const menuItem = new MenuItem();

    menuItem.name = "Delete Router"
    menuItem.operation = this.operator;
    menuItem.dropdown = [];
    menuItem.dropdownOpen = false;
    
    const submenu1 = new MenuItem();
    submenu1.name = "Test";
    submenu1.operation = this.operator;
    submenu1.dropdown = [];
    submenu1.dropdownOpen = false;

    const submenu2 = new MenuItem();
    submenu2.name = "Test";
    submenu2.operation = this.operator;
    submenu2.dropdown = [];
    submenu2.dropdownOpen = false;
    
    menuItem.dropdown = [submenu1,submenu2];
    
    super.option = menuItem;
  }

  ngOnInit(): void {
  }

  operator = () => {
    console.log('Delete Router TODO');
  }
}
