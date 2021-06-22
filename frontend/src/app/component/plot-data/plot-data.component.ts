import { Component, Input, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';
import HighchartsNetworkgraph from "highcharts/modules/networkgraph";
import { Connection } from 'src/app/interface/connection';
import { Router } from 'src/app/interface/router';
import { HighchartsControllerService } from 'src/app/services/highcharts-controller.service';
import { ShareDataService } from 'src/app/services/share-data.service';
declare var require: any


@Component({
  selector: 'app-plot-data',
  templateUrl: './plot-data.component.html',
  styleUrls: ['./plot-data.component.scss']
})
export class PlotDataComponent implements OnInit {
  Highcharts: typeof Highcharts = Highcharts; // required
  chartConstructor: string = 'chart'; // optional string, defaults to 'chart'
  contextMenu: boolean = false;
  contextMenuY: number = -1;
  contextMenuX: number = -1;
  router: Router = new Router("", -1);
  @Input() contextmenu = null;

  constructor(private shareDataService: ShareDataService) { }

  ngOnInit(): void {

    this.initialize();
  }

  initialize() {
    const that = this;
    const Draggable = require("highcharts/modules/draggable-points.js");
    Draggable(Highcharts);
    HighchartsNetworkgraph(Highcharts);
    Highcharts.chart('Highcharts',
      {
        chart: {
          type: 'networkgraph',
          animation: {
            duration: 0
          }
        },

        title: {
          text: ""
        },
        tooltip: {
          enabled: false
        },
        series: [{
          showInLegend: false,
          type: 'networkgraph',
          lineWidth: 0
        },
        ],
        xAxis: {
          max: 100,
          min: 0,

          title: {
            text: ""
          },
          lineWidth: 0,
          minorGridLineWidth: 0,
          lineColor: 'transparent',
          labels: {
            enabled: false
          },
          minorTickLength: 0,
          tickLength: 0
        },
        yAxis: {
          max: 100,
          min: 0,

          title: {
            text: ""
          },
          gridLineColor: 'transparent',
          lineWidth: 0,
          minorGridLineWidth: 0,
          lineColor: 'transparent',
          labels: {
            enabled: false
          },
          minorTickLength: 0,
          tickLength: 0
        },
        plotOptions: {
          networkgraph: {
            dataLabels: { enabled: true },
            keys: ['from', 'to'],
            layoutAlgorithm: {
              maxIterations: 0,
              friction: 0,
              repulsiveForce: () => { 0 },
              initialPositions: 'random'
            }
          },
          series: {
            states: {
              hover: {
                enabled: true
              },
              inactive: { enabled: false }
            },
            dragDrop: {
              draggableX: true,
              draggableY: true,
              dragMaxX: 100,
              dragMaxY: 100
            },
            point: {
              events: {
                drop: function (e) {
                },
                click: function (e: MouseEvent) {
                  let router = new Router("", -1);
                  router = that.getRouter(this.name)
                  that.open({ clientX: e.clientX, clientY: e.clientY } as MouseEvent, router);
                  e.stopPropagation();
                },
              }
            },
            marker: {
              symbol: "url(assets/router.png)"
            }
          }
        }
      }
    )
    this.shareDataService.Highcharts = this.Highcharts;
    this.shareDataService.init();
  }

  open({ clientX, clientY }: MouseEvent, router: Router): void {
    this.contextMenuX = clientX
    this.contextMenuY = clientY
    this.router = router;
    this.contextMenu = true;
  }

  disableContextMenu(): void {
    this.contextMenu = false;
  }

  getRouter(name: string): Router {
    let routerList: Router[] = [];
    this.shareDataService.routers.subscribe((_routerList) => routerList = _routerList);
    let router = routerList.find((_router) => {return `${name}` === `${_router.port}`;});
    if (router === undefined) router = new Router("", -1);
    return router;
  }

}
