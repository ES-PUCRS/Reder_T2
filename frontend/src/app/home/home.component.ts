import { Component, OnInit, ViewChild } from '@angular/core';
import {MatMenuModule, MatMenuTrigger} from '@angular/material/menu';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor() { }

  ngOnInit(): void { }

  @ViewChild('trigger', { static: true })
  trigger: MatMenuTrigger | undefined;

  menuOptions: Array<String> = ["option 1", "option 2", "option 3"];
  selectedOption: string = "no Option selected";
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
    this.menu?.openMenu();
  }

}
