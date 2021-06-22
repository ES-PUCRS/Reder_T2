import { Component, OnInit, ViewChild } from '@angular/core';
import { MatMenuTrigger } from '@angular/material/menu';
import { Connection } from 'src/app/interface/connection';
import { Router } from 'src/app/interface/router';
import { ShareDataService } from 'src/app/services/share-data.service';
import { BackendService } from '../../services/backend.service';
import { AgentService } from '../../services/agent.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})

export class HomeComponent implements OnInit {
  constructor(private backendService: BackendService, private agentService: AgentService, private shareDataService: ShareDataService) {
  }

  @ViewChild('trigger', { static: true })
  trigger: MatMenuTrigger | undefined;
  contextMenu: boolean = false;
  contextMenuY: number = -1;
  contextMenuX: number = -1;
  router: Router | undefined;
  selectedRouter: Router | undefined;

  selectedOption: string = "no Option selected";
  routerCount: number = 0;
  routers: Array<Router> = [];

  ngOnInit(): void {
    this.routerCount = window.localStorage.length;
    this.shareDataService.routers.subscribe((routers) => this.routers = routers);
  }

  diselectRouter() {
    this.selectedRouter = undefined;
  }

  open(event: MouseEvent, router: any): void {
    this.contextMenuX = event.clientX
    this.contextMenuY = event.clientY
    this.router = router;
    this.contextMenu = true;
  }

  selectRouter(router: any) {
    this.selectedRouter = router;
  }

  disableContextMenu(): void {
    this.contextMenu = false;
  }

  async sendPost() {
      const result = await this.agentService.callRouter()
      let port: number = result as number;
      window.localStorage.setItem("Router " + (1 + this.routerCount), port.toString());
      let name = "Router " + (1 + this.routerCount);
      let router = new Router(name, port);

      let connectionList: Connection[] = [];
      this.shareDataService.connections.subscribe((Connection) => connectionList = Connection);

      this.routers.push(router);
      connectionList.push(new Connection(router, router))

      this.shareDataService.update_router(this.routers);
      this.shareDataService.update_connection(connectionList);

      this.shareDataService.routers.subscribe((_router_list) => console.log(_router_list));

      this.routerCount = window.localStorage.length + 1;
  }
}
