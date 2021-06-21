import { Component, OnInit } from '@angular/core';
import { Connection } from 'src/app/interface/connection';
import { MenuItem } from 'src/app/interface/menu-item';
import { Router } from 'src/app/interface/router';
import { ShareDataService } from 'src/app/services/share-data.service';
import { MenuItemsComponent } from '../shared/menu-items.component';

@Component({
  selector: 'disconnect-router-option',
  templateUrl: '../shared/menu-items.component.html',
  styleUrls: ['../shared/menu-items.component.scss']
})
export class DisconnectRouterComponent extends MenuItemsComponent implements OnInit {

  constructor(private shareDataService: ShareDataService) {
    super();
  }

  ngOnInit(): void {
    const menuItem = new MenuItem();

    menuItem.name = "Disconnect Router"
    menuItem.operation = this.operator;
    menuItem.dropdown = [];
    menuItem.dropdownOpen = false;

    menuItem.dropdown = this.setRouters();

    super.option = menuItem;
  }

  setRouters() {
    let connectionList: Connection[] = [];
    let menuItems: MenuItem[] = [];
    this.shareDataService.connections.subscribe((_connectionList) => connectionList = _connectionList);
    
    console.log(connectionList);
    connectionList.forEach(element => {
      if ((element.routerA.port === this.router.port && element.routerB.port === this.router.port)) return;
      if ((element.routerA.port === this.router.port || element.routerB.port === this.router.port)) {
        menuItems.push(new MenuItem());
        let index = menuItems.length - 1;
        let connectedRouter = element.routerA.port === this.router.port ? element.routerB : element.routerA;

        menuItems[index].name = `${connectedRouter.port}`;
        menuItems[index].operation = () => { this.subOperation(connectedRouter) };
        menuItems[index].dropdown = [];
        menuItems[index].dropdownOpen = false;
      }
    });

    return menuItems;
  }

  operator = () => {

  }

  subOperation(router: Router) {
    console.log("a");
    this.Highcharts.charts[0]?.series[0].data.forEach((conn) => {
      if (conn.options.from !== undefined && conn.options.to !== undefined)
        if ((conn.options.from === `${router.port}` && conn.options.to === `${this.router.port}`) ||
          (conn.options.to === `${router.port}` && conn.options.from === `${this.router.port}`)) {
          this.Highcharts.charts[0]?.series[0].removePoint(conn.index, false);
        }
    })
    let connection_list: Connection[] = [];
    let index = -1;
    this.shareDataService.connections.subscribe((conn_list) => { connection_list = conn_list });
    connection_list.forEach((element, idx) => {
      if ((element.routerA.port === this.router.port && element.routerB.port === router.port) ||
        (element.routerA.port === router.port && element.routerB.port === this.router.port)) {
        index = idx;
        return;
      }
    });
    connection_list.splice(index, 1)
    this.shareDataService.update_connection(connection_list);
  }
}
