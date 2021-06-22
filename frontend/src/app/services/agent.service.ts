import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AgentService {

  constructor(private http:HttpClient) { }

  public agentEndpoint: string = "http://localhost:4201";

  public callRouter() {
    return this.http.post(this.agentEndpoint, { title: 'Generate new router' }).toPromise();
  }

}
