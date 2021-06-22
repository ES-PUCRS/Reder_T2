import { Component, Input, OnInit } from '@angular/core';
import { Connection } from 'src/app/interface/connection';
import { MenuItem } from 'src/app/interface/menu-item';
import { Router } from 'src/app/interface/router';
import { ShareDataService } from 'src/app/services/share-data.service';
import { MenuItemsComponent } from '../shared/menu-items.component';

@Component({
  selector: 'delete-router-option',
  templateUrl: '../shared/menu-items.component.html',
  styleUrls: ['../shared/menu-items.component.scss']
})
export class DeleteRouterComponent extends MenuItemsComponent implements OnInit {



  constructor(private shareDataService: ShareDataService) {
    super();
    const menuItem = new MenuItem();

    menuItem.name = "Delete Router"
    menuItem.operation = this.operator;
    menuItem.dropdown = [];
    menuItem.dropdownOpen = false;

    menuItem.dropdown = [];

    super.option = menuItem;
  }

  ngOnInit(): void {
  }

  operator = () => {
    let connection_list: Connection[] = [];
    let aux_connection_list: Connection[] = [];
    let router_list : Router[] = [];
    let aux_router_list : Router[] = [];
    this.shareDataService.connections.subscribe((conn_list) => {
      connection_list = conn_list;
      aux_connection_list = conn_list
    });
    this.shareDataService.routers.subscribe((_router_list: Router[]) => {
      router_list = _router_list;
      aux_router_list = _router_list;
    });

    aux_connection_list.forEach((element, idx) => {
      if ((element.routerA.port === this.router.port || element.routerB.port === this.router.port)) {
        connection_list.splice(idx, 1)
      }
    });

    aux_router_list.forEach((element, idx) => {
      if (element.port === this.router.port) {
        router_list.splice(idx, 1);
      }
    });
    localStorage.removeItem(this.router.name);
    this.shareDataService.update_router(router_list);
    this.shareDataService.update_connection(connection_list);
  }
}
