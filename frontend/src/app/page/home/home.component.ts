import { Component, OnInit, ViewChild } from '@angular/core';
import { MatMenuTrigger } from '@angular/material/menu';
import { Router } from 'src/app/interface/router';
import { ShareDataService } from 'src/app/services/share-data.service';
import { BackendService } from '../../services/backend.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})

export class HomeComponent implements OnInit {
  constructor(private backendService: BackendService, private shareDataService: ShareDataService) {
  }
  routerCount: number = 0;
  routers: Array<Router> = [];

  ngOnInit(): void {
    this.routerCount = window.localStorage.length;
    this.shareDataService.routers.subscribe((routers) => this.routers = routers);
  }

  sendPost() {
    let res: any;
    this.backendService.restPost().subscribe(data => {
      let port: number = data as number;
      window.localStorage.setItem("Router " + (1 + this.routerCount), port.toString());
      let name = "Router " + (1 + this.routerCount);
      this.routers.push(new Router(name, port));
      console.log(this.routers);
      this.routerCount = window.localStorage.length + 1;
    })
  }
}
