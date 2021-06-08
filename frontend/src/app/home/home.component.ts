import { Component, OnInit, ViewChild } from '@angular/core';
import { MatMenuModule, MatMenuTrigger } from '@angular/material/menu';
import { BackendService } from '../services/backend.service';

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

  menuOptions: Array<String> = ["option 1", "option 2", "option 3"];
  selectedOption: string = "no Option selected";
  routerCount: number = 0;
  routers: Array<{name: string, port: string}> = [];

  onTextSelection(event: any):void{

    let menu = document.getElementById('matMenu');
    if(menu !== null){
      menu.style.display = '';
      menu.style.position = 'absolute';
      menu.style.left = event.pageX + 5 + 'px';
      menu.style.top = event.pageY + 5 + 'px';
      this.trigger?.openMenu();
    }
  }

  ngOnInit(): void {
    this.routerCount = window.localStorage.length;
    let objectKeys = Object.keys(window.localStorage);
    for(let i = 0; i < window.localStorage.length; i++){
      let aux: any = window.localStorage.getItem(objectKeys[i])
      this.routers.push({
        "name": objectKeys[i],
        "port": aux
    });
    }
    console.log(this.routers);
  }

  // onMenuClosed():void {
  //   var menu = document.getElementById('menuBtn');
  //       if (menu) {
  //           menu.style.display = 'none';
  //       }
  // }

  addTextTo(selectedOpn: any): void {
    this.selectedOption = selectedOpn + ' selected';
  }


  open({ x, y }: MouseEvent) {
    console.log(x, y);
    // this.menu?.openMenu();
  }

  getOnRouter(){
    let res: any;
    this.backendService.restGet().subscribe( data => {
      console.log(data);
    })
  }

  sendPost(){
    let res: any;
    this.backendService.restPost().subscribe( data => {
      console.log(data);
      let port: string = data.toString()
      window.localStorage.setItem("Router " +(1+this.routerCount), port);
      console.log(window.localStorage.length);
      this.routers.push({
        "name": "Router " +(1+this.routerCount),
        "port": port
      });
      console.log(this.routers);
      this.routerCount = window.localStorage.length + 1;
    })
  }

}
