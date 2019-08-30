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
  
  currentState: state = null;
  newStateName: string = '';
  sigma: string[] = [];
  states: state[] = [];
  word: string[] = [];

  constructor() {}

  ngOnInit() {
  }

  newState(){
    let a: state = {
      id: this.newStateName,
      final: false,
      transition: {key: null, state: null}
    };
    this.newStateName = '';
    this.states.push(a);
    console.log(this.states);
  }
}

interface state{
  id: string,
  final: boolean,
  transition: {key: string, state: state}
}
