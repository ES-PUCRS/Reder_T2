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
    

    super.option = menuItem;
  }

  ngOnInit(): void {
  }

  operator = () => {
    console.log('Delete Router TODO');
  }
}
