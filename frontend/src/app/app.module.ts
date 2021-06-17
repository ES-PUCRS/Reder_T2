import { NgModule } from '@angular/core';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './page/home/home.component';
import { AppComponent } from './app.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterComponent } from './component/router/router.component';
import { ContextMenuComponent } from './component/context-menu/context-menu.component'
import { MatDividerModule } from '@angular/material/divider';
import { MenuItemsComponent } from './component/context-menu/menu-items/shared/menu-items.component';
import { SelectorComponent } from './component/context-menu/menu-items/selector.component';
import { DeleteRouterComponent } from './component/context-menu/menu-items/delete-router/delete-router.component';
import { ConfigureModuleComponent } from './component/context-menu/menu-items/configure-module/configure-module.component';
import { CreateModuleComponent } from './component/context-menu/menu-items/create-module/create-module.component';
import { ConnectRouterComponent } from './component/context-menu/menu-items/connect-router/connect-router.component';
import { ClickOutsideModule } from 'ng-click-outside';


@NgModule({
  declarations: [
    HomeComponent,
    AppComponent,
    RouterComponent,
    ContextMenuComponent,
    MenuItemsComponent,
    SelectorComponent,
    DeleteRouterComponent,
    ConfigureModuleComponent,
    CreateModuleComponent,
    ConnectRouterComponent
  ],
  imports: [
    ClickOutsideModule,
    MatButtonModule,
    MatDividerModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MatNativeDateModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserModule,
    FormsModule,
    DragDropModule,
    MatMenuModule,
    MatIconModule
  ],
  bootstrap: [AppComponent],
  providers: []
})

export class AppModule { }
