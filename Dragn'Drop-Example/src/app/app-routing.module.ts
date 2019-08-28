import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { BlueprintComponent } from './blueprint/blueprint.component';


const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'blueprint', component: BlueprintComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
