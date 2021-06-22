export class Router {
    name: string
    port: number
    modules: number[]

    constructor(name: string, port: number, modules?:number[]) {
        this.name = name;
        this.port = port;
        this.modules = modules || [];
    }
}
