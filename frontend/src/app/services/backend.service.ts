import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  constructor(private http:HttpClient) { }

  public agentEndpoint: string = "http://localhost:8080";

  public restGet() {
    return this.http.get(this.agentEndpoint);
  }

}
