import {NgModule} from '@angular/core';


//Drag event ~ animations ~ materials
import {MatNativeDateModule, MatRippleModule} from '@angular/material/core';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatBottomSheetModule} from '@angular/material/bottom-sheet';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatDividerModule} from '@angular/material/divider';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatStepperModule} from '@angular/material/stepper';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatDialogModule} from '@angular/material/dialog';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import {MatSliderModule} from '@angular/material/slider';
import {MatRadioModule} from '@angular/material/radio';
import {MatInputModule} from '@angular/material/input';
import {MatChipsModule} from '@angular/material/chips';
import {MatTableModule} from '@angular/material/table';
import {ScrollingModule} from '@angular/cdk/scrolling';
import {MatBadgeModule} from '@angular/material/badge';
import {CdkStepperModule} from '@angular/cdk/stepper';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {MatMenuModule} from '@angular/material/menu';
import {MatSortModule} from '@angular/material/sort';
import {MatTabsModule} from '@angular/material/tabs';
import {MatTreeModule} from '@angular/material/tree';
import {CdkTableModule} from '@angular/cdk/table';
import {PortalModule} from '@angular/cdk/portal';
import {CdkTreeModule} from '@angular/cdk/tree';
import {A11yModule} from '@angular/cdk/a11y';


//RightClickDropDown
import { OverlayModule } from '@angular/cdk/overlay';

@NgModule({
  exports: [
    MatProgressSpinnerModule,
    MatAutocompleteModule,
    MatButtonToggleModule,
    MatBottomSheetModule,
    MatSlideToggleModule,
    MatProgressBarModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatExpansionModule,
    MatPaginatorModule,
    MatGridListModule,
    MatSnackBarModule,
    MatCheckboxModule,
    MatToolbarModule,
    MatTooltipModule,
    MatSidenavModule,
    MatStepperModule,
    CdkStepperModule,
    MatDividerModule,
    MatDialogModule,
    MatSliderModule,
    MatButtonModule,
    MatRippleModule,
    MatSelectModule,
    ScrollingModule,
    CdkTableModule,
    MatTableModule,
    DragDropModule,
    MatRadioModule,
    MatBadgeModule,
    MatChipsModule,
    MatInputModule,
    CdkTreeModule,
    MatCardModule,
    OverlayModule,
    MatIconModule,
    MatListModule,
    MatMenuModule,
    MatSortModule,
    MatTabsModule,
    MatTreeModule,
    PortalModule,
    A11yModule,
  ]
})

export class DemoMaterialModule {}
