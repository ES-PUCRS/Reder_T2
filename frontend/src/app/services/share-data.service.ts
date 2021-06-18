import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Router } from '../interface/router';

@Injectable({
  providedIn: 'root'
})
export class ShareDataService {

  private router_list = new BehaviorSubject<Router[]>([]);


  constructor() { }

  update() {

    this.router_list.next();
  }
}
