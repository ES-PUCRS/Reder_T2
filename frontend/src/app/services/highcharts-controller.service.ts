import { Injectable } from '@angular/core';
import * as Highcharts from 'highcharts';
import { Connection } from '../interface/connection';
import { Router } from '../interface/router';
import { ShareDataService } from './share-data.service';

@Injectable({
  providedIn: 'root'
})
export class HighchartsControllerService {


  constructor(private shareDataService: ShareDataService) { }

  initialize() {

  }



  addPoint(connectedRouters: Router[]) {

  }


}
