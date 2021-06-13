import { fn } from '@angular/compiler/src/output/output_ast';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-context-menu',
  templateUrl: './context-menu.component.html',
  styleUrls: ['./context-menu.component.scss']
})
export class ContextMenuComponent implements OnInit {
  menuOptions: Array<String> = ["option 1", "option 2", "option 3"];
  menuMethods: Array<() => void>;


  @Input() x = 0;
  @Input() y = 0;

  constructor() {

    this.menuMethods = [this.testA, this.testB, this.testC];

   }

  ngOnInit(): void {
  }

  testA = function(): void {
    alert("A");
  }

  testB() {
    alert("B");
  }

  testC() {
    alert("C");
  }
}
