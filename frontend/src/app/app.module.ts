import { NgModule } from '@angular/core';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BlueprintComponent } from './blueprint/blueprint.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { DemoMaterialModule } from './app.material.module';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './home/home.component';
import { AppComponent } from './app.component';


@NgModule({
  declarations: [
    BlueprintComponent,
    HomeComponent,
    AppComponent
  ],
  imports: [
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MatNativeDateModule,
    DemoMaterialModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserModule,
    FormsModule
  ],
  entryComponents: [BlueprintComponent],
  bootstrap: [AppComponent],
  providers: []
})

export class AppModule { }