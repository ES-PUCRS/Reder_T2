import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  constructor(private http:HttpClient) { }

  public agentEndpoint: string = "http://localhost:4201";
  public BackendEndpoint: string = "http://localhost:1010/API";

  public restGet() {
    return this.http.get(this.BackendEndpoint);
  }

  public restPost() {
    return this.http.post(this.agentEndpoint, { title: 'Angular POST Request Example' });
  }

  // public restPut() {
  //   return this.http.put(this.agentEndpoint);
  // }

}
