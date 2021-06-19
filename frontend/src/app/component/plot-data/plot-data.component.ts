import { Component, Input, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';
import HighchartsNetworkgraph from "highcharts/modules/networkgraph";
import { Router } from 'src/app/interface/router';
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

  constructor() { }

  ngOnInit(): void {

    this.initialize();
  }

  initialize() {
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
          data: [
            ['Celtic', 'Balto-Slavic'],
            ['Celtic', 'Italic'],
            ['Proto Indo-European', 'Celtic'],
            ['Proto Indo-European', 'Italic'],
          ],
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
                  console.log(e);
                },
                click: ((e: MouseEvent) => {
                  this.open({ clientX: e.clientX, clientY: e.clientY } as MouseEvent, "");
                  e.stopPropagation();
                }).bind(this),

              }
            },
            marker: {
              symbol: "url(assets/router.png)"
            }
          }
        }
      }
    )   
  }
  open({clientX, clientY} :MouseEvent, router: any): void {
     this.contextMenuX = clientX
    this.contextMenuY = clientY
    this.router = router;
    this.contextMenu = true;
  }

  disableContextMenu(): void {
    this.contextMenu = false;
  }

  addData() {
    Highcharts.charts[0]?.series[0].addPoint(['Germanic', 'Celtic']);
  }

  
}