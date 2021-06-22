import { Injectable } from '@angular/core';
import * as Highcharts from 'highcharts';
import { BehaviorSubject } from 'rxjs';
import { Connection } from '../interface/connection';
import { Router } from '../interface/router';

@Injectable({
  providedIn: 'root'
})
export class ShareDataService {
  Highcharts: typeof Highcharts = Highcharts; // required

  private router_list = new BehaviorSubject<Router[]>([]);
  private currentRouterList = this.router_list.asObservable();

  private connection_list = new BehaviorSubject<Connection[]>([]);
  private currentConnectionList = this.connection_list.asObservable();

  constructor() {
  }

  init() {
    // this.mock_data();
    let routers: Router[] = [];
    let objectKeys = Object.keys(window.localStorage);
    for (let i = 0; i < window.localStorage.length; i++) {
      let aux: any = window.localStorage.getItem(objectKeys[i]);
      routers.push(new Router(objectKeys[i], aux));
    }
    let connections: Connection[] = [];
    routers.forEach((router: Router) => {
      connections.push(new Connection(router, router));
    });
    this.update_router(routers);
    this.update_connection(connections);
  }


  get routers() {
    return this.currentRouterList;
  }

  get connections() {
    return this.currentConnectionList;
  }

  update_router(routers: Router[]) {
    this.router_list.next(routers);
  }

  update_connection(connections: Connection[]) {
    this.connection_list.next(connections);
    const auxArray: Highcharts.PointOptionsType[] = [];
    connections.forEach((connection) => {

      auxArray.push([connection.routerA.port, connection.routerB.port]);
    })
    this.Highcharts.charts[0]?.series[0].setData(auxArray);
  }

  //SHOULD BE REMOVED!!!!!!!!
  // mock_data() {
  //   localStorage.setItem("Router 1", "5031")
  //   localStorage.setItem("Router 2", "7109")
  //   localStorage.setItem("Router 3", "1455")
  //   localStorage.setItem("Router 4", "3362")
  // }
}
