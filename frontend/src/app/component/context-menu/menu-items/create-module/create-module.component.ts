import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'src/app/interface/menu-item';
import { BackendService } from 'src/app/services/backend.service';
import { MenuItemsComponent } from '../shared/menu-items.component';
import { ShareDataService } from 'src/app/services/share-data.service';
import { Router } from 'src/app/interface/router';

@Component({
  selector: 'create-module-option',
  templateUrl: '../shared/menu-items.component.html',
  styleUrls: ['../shared/menu-items.component.scss']
})
export class CreateModuleComponent extends MenuItemsComponent implements OnInit {


  constructor(private backendService:BackendService,private shareDataService: ShareDataService) {
    super();
    const menuItem = new MenuItem();

    menuItem.name = "Create Module";
    menuItem.operation = this.operator;
    menuItem.dropdown = [];
    menuItem.dropdownOpen = false;
    super.option = menuItem;
  }

  ngOnInit(): void {
  }

  operator = async ()  =>  {
    const result = await this.backendService.generate_module(this.router.port);

    let router_list: Router[] = [];
    this.shareDataService.routers.subscribe((_router_list) => {router_list = _router_list})
    let routerIndex = router_list.findIndex((element) => element.port === this.router.port)
    router_list[routerIndex].modules.push(Number(result.port))
    this.shareDataService.update_router(router_list);
    this.shareDataService.routers.subscribe((_router_list) => {console.log(_router_list)})

// PRECISA COLOCAR NO BEHAVIOR SUBJECT/DATA SERVICE
    // }
  }
}
