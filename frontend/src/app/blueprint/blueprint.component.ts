import { Component, OnInit } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { state } from '@angular/animations';
//ngStyle

@Component({
  selector: 'app-blueprint',
  templateUrl: './blueprint.component.html',
  styleUrls: ['./blueprint.component.css']
})
export class BlueprintComponent implements OnInit {
  
  transition = {key: 'value', state: state};
  alphabet: alphabet[] = [];
  word: alphabet[] = [];
  states: state[] = [];

  constructor() {}

  ngOnInit() {
  }
}

interface state{
  id: string,

}
interface alphabet{
  sigma: string;
}
