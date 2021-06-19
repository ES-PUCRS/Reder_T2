import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'src/app/interface/menu-item';
import { Router } from 'src/app/interface/router';
import { ShareDataService } from 'src/app/services/share-data.service';
import { MenuItemsComponent } from '../shared/menu-items.component';

@Component({
  selector: 'connect-router-option',
  templateUrl: '../shared/menu-items.component.html',
  styleUrls: ['../shared/menu-items.component.scss']
})
  
export class ConnectRouterComponent extends MenuItemsComponent implements OnInit {

  constructor(private shareDataService: ShareDataService) {
    super();
    const menuItem = new MenuItem();

    menuItem.name = "Connect Router";
    menuItem.operation = this.operation;
    menuItem.dropdownOpen = false;
    super.option = menuItem;
    menuItem.dropdown = this.setRouters();

  }

  setRouters() {
    let routers: Router[] = [];
    let menuItems: MenuItem[] = [];
    this.shareDataService.routers.subscribe((routerList) => routers = routerList);

    routers.forEach(element => {
      // if(element.port !== super.){
      menuItems.push(new MenuItem());
      let index = menuItems.length - 1;
      menuItems[index].name = `${element.port}`;
      menuItems[index].operation = () => { this.subOperation(element) };
      menuItems[index].dropdown = [];
      menuItems[index].dropdownOpen = false;
      // }
    });

    return menuItems;
  }

  ngOnInit(): void {
  }

  operation = () => {

  }
  subOperation = (router: Router) => {
    console.log(router);

    this.Highcharts.charts[0]?.series[0].addPoint([router.port, this.router.port]);

  }
}
