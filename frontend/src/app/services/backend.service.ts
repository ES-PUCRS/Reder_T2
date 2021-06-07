import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  constructor(private http:HttpClient) { }

  public agentEndpoint: string = "http://localhost:8080";

  public restGet() {
    return this.http.get(this.agentEndpoint);
  }

  public restPost() {
    return this.http.post(this.agentEndpoint, { title: 'Angular POST Request Example' });
  }

  // public restPut() {
  //   return this.http.put(this.agentEndpoint);
  // }

}
