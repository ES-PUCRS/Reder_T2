import { Component, Input, OnInit } from '@angular/core';
import { Connection } from 'src/app/interface/connection';
import { MenuItem } from 'src/app/interface/menu-item';
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
    this.Highcharts.charts[0]?.series[0].data.forEach((el) => console.log(el.options));
    this.Highcharts.charts[0]?.series[0].data.forEach((conn) => {
      if (conn.options.from !== undefined && conn.options.to !== undefined)
        if (conn.options.from === `${this.router.port}` || conn.options.to === `${this.router.port}`) {
          console.log(conn.options);
          conn.remove()
          // this.Highcharts.charts[0]?.series[0].removePoint(conn.index, false);
        }
    })
    let connection_list: Connection[] = [];
    // let index = -1;
    // this.shareDataService.connections.subscribe((conn_list) => { connection_list = conn_list });
    // connection_list.forEach((element, idx) => {
    //   if ((element.routerA.port === this.router.port && element.routerB.port === router.port) ||
    //     (element.routerA.port === router.port && element.routerB.port === this.router.port)) {
    //     index = idx;
    //     return;
    //   }
    // });
    // connection_list.splice(index, 1)
    // this.shareDataService.update_connection(connection_list);
    // this.Highcharts.charts[0]?.series[0].data.forEach((el) => console.log(el.options));

  }
}
