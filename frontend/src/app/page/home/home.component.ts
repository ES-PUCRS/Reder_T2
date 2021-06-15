import { Component, OnInit, ViewChild } from '@angular/core';
import { MatMenuTrigger } from '@angular/material/menu';
import { Router } from 'src/app/interface/router';
import { BackendService } from '../../services/backend.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})

export class HomeComponent implements OnInit {
  constructor(private backendService: BackendService) {
  }

  @ViewChild('trigger', { static: true })
  trigger: MatMenuTrigger | undefined;
  contextMenu: boolean = false;
  contextMenuY: number = -1;
  contextMenuX: number = -1;
  router: Router | undefined;

  selectedOption: string = "no Option selected";
  routerCount: number = 0;
  routers: Array<Router> = [];

  ngOnInit(): void {
    this.routerCount = window.localStorage.length;
    let objectKeys = Object.keys(window.localStorage);
    for (let i = 0; i < window.localStorage.length; i++) {
      let aux: any = window.localStorage.getItem(objectKeys[i]);
      this.routers.push(new Router(objectKeys[i], aux));
    }
    // console.log(this.routers[0] instanceof Router);
  }

  open(event: MouseEvent, router: any): void {
    this.contextMenuX = event.clientX
    this.contextMenuY = event.clientY
    this.router = router;
    this.contextMenu = true;
  }

  disableContextMenu(): void {
    this.contextMenu = false;
  }

  sendPost() {
    let res: any;
    this.backendService.restPost().subscribe(data => {
      let port: number = data as number;
      window.localStorage.setItem("Router " + (1 + this.routerCount), port.toString());
      console.log(window.localStorage.length);
      let name = "Router " + (1 + this.routerCount);
      this.routers.push(new Router(name, port));
      console.log(this.routers);
      this.routerCount = window.localStorage.length + 1;
    })
  }
}
