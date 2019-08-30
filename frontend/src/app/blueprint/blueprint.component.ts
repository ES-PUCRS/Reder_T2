import { Component, OnInit, ViewChild, TemplateRef, ViewContainerRef } from '@angular/core';
//import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Overlay, OverlayRef } from '@angular/cdk/overlay';
import { TemplatePortal } from '@angular/cdk/portal';
import { fromEvent, Subscription } from 'rxjs';
import { take, filter } from 'rxjs/operators';
import { state } from '@angular/animations';

@Component({
  selector: 'app-blueprint',
  templateUrl: './blueprint.component.html',
  styleUrls: ['./blueprint.component.css']
})
export class BlueprintComponent implements OnInit {
  
  @ViewChild('stateMenu', {static: false}) stateMenu: TemplateRef<any>;
  overlayRef: OverlayRef | null;
  sub: Subscription;

  currentState: state = null;
  newStateName: string = '';
  sigma: string[] = [];
  states: state[] = [];
  word: string[] = [];

  constructor(
    public overlay: Overlay,
    public viewContainerRef: ViewContainerRef
  ) {}

  open({ x, y }: MouseEvent, state) {
    console.log(x, y);
    this.close();
    const positionStrategy = this.overlay.position()
      .flexibleConnectedTo({ x, y })
      .withPositions([
        {
          originX: 'end',
          originY: 'bottom',
          overlayX: 'end',
          overlayY: 'top',
        }
      ]);
      
  this.overlayRef = this.overlay.create({
    positionStrategy,
    scrollStrategy: this.overlay.scrollStrategies.close()
  });

  this.overlayRef.attach(
    new TemplatePortal(
      this.stateMenu,
      this.viewContainerRef,
      { $implicit: state } ));

  this.sub = fromEvent<MouseEvent>(document, 'click')
    .pipe(
      filter(event => {
        const clickTarget = event.target as HTMLElement;
        return !!this.overlayRef && !this.overlayRef.overlayElement.contains(clickTarget);
      }),
      take(1)
    ).subscribe(() => this.close())
  }


  ngOnInit() {
  }

  newState() {
    let a: state = {
      id: this.newStateName,
      final: false,
      transition: {key: null, state: null}
    };
    this.newStateName = '';
    this.states.push(a);
  }

  delete(state) {
    this.states.splice(this.states.indexOf(state), 1);
    this.close();
  }
  rename(state) {
    this.states.splice(this.states.indexOf(state), 1);
    this.close();
  }
  turn(state) {
    this.states.splice(this.states.indexOf(state), 1);
    this.close();
  }

  close() {
    this.sub && this.sub.unsubscribe();
    if (this.overlayRef) {
      this.overlayRef.dispose();
      this.overlayRef = null;
    }
  }
}

export interface state{
  id: string,
  final: boolean,
  transition: {key: string, state: state}
}