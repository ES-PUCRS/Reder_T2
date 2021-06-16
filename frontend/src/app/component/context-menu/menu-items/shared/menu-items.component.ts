import { AfterViewInit, Component, Input, OnInit} from '@angular/core';
import { MenuItem } from 'src/app/interface/menu-item';

@Component({
  selector: 'app-menu-items',
  templateUrl: './menu-items.component.html',
  styleUrls: ['./menu-items.component.scss']
})
export class MenuItemsComponent implements OnInit, AfterViewInit {

  @Input() position: number = 0;
  
  option: MenuItem;

  constructor() {
    this.option = new MenuItem();
  }
  ngAfterViewInit(): void {

  }
  test() {
    return this.position * 29;
  }

  ngOnInit(): void {

  }
}