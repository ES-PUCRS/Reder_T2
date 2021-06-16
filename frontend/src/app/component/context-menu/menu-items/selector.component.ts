import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-selector',
  templateUrl: './selector.component.html',
  styleUrls: ['./selector.component.scss']
})
export class SelectorComponent implements OnInit {

  @Input() component: string = "";
  @Input() position: number = 0;

  constructor() { }

  ngOnInit(): void {
    console.log(this.position);
  }

}
