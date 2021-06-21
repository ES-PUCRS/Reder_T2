import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Connection } from '../interface/connection';
import { Router } from '../interface/router';

@Injectable({
  providedIn: 'root'
})
export class ShareDataService {

  private router_list = new BehaviorSubject<Router[]>([]);
  private currentRouterList = this.router_list.asObservable();

  private connection_list = new BehaviorSubject<Connection[]>([]);
  private currentConnectionList = this.connection_list.asObservable();


  constructor() {
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
  }
}
