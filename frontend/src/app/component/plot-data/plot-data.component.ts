import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';
declare var require: any

@Component({
  selector: 'app-plot-data',
  templateUrl: './plot-data.component.html',
  styleUrls: ['./plot-data.component.scss']
})
export class PlotDataComponent implements OnInit {
  Highcharts: typeof Highcharts = Highcharts; // required
  // chart: Highcharts.Chart = Highcharts.chart("Highcharts", {});
  chartConstructor: string = 'chart'; // optional string, defaults to 'chart'
  // chartOptions: Highcharts.Options = {
    
    //   ]
    // };
    constructor() { }
    
    ngOnInit(): void {
    
    this.initialize();
  }

  initialize() {
    const Draggable = require("highcharts/modules/draggable-points.js");
    Draggable(Highcharts);
    Highcharts.chart('Highcharts',
      {
        chart: {
          // height: "100%",
          // width: "100%",

        },
        title: {
          text:""
        },
        tooltip: {
          enabled:false
        },
        series: [{
          showInLegend: false,
          data: [1, 2, 3],
          type: 'scatter',
          lineWidth: 0
          
        }],
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
          series: {
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
                }
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

  addData() {
    Highcharts.charts[0]?.series[0].addPoint({ x: 10, y: 20 });
  }

}