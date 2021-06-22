import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  constructor(private http: HttpClient) { }

  public installModuleEndpoint: string = "http://localhost:<PORT>/API/install?object=module";
  public healthEndpoint:        string = "http://localhost:<PORT>/API/health";
  public wireEndpoint:          string = "http://localhost:<PORT>/API/wire?object=auto&target=<TARGET>";

  public generate_module(port: number) {
    const url = this.installModuleEndpoint.replace("<PORT>", `${port}`);
    return this.http.get<{ port: number }>(url).toPromise();
  }

  public attempt_connection(port: number, target:number) {
    const url = this.wireEndpoint.replace("<PORT>", `${port}`).replace("<TARGET>", `${target}`);
    return this.http.get<{ port: number }>(url).toPromise();
  }

}
