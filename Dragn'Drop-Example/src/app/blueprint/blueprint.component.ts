import { Component, OnInit } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-blueprint',
  templateUrl: './blueprint.component.html',
  styleUrls: ['./blueprint.component.css']
})
export class BlueprintComponent implements OnInit {

  numbers: number[] = [];
  otherNumbers: number[] = [];

  constructor() {
    for(let i = 0; i<10000; i++){
      this.numbers.push(i);
    }
  }

  ngOnInit() {
  }

  drop(event: CdkDragDrop<number[]>){
    if(event.previousContainer !== event.container){
      transferArrayItem(event.previousContainer.data, event.container.data, event.previousIndex, event.currentIndex)
    }else
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex)
  }  
}
