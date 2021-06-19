import { Router } from "./router";

export class Connection {
    routerA: Router;
    routerB: Router;

    constructor(routerA: Router, routerB: Router) {
        this.routerA = routerA;
        this.routerB = routerB;
    }
}
