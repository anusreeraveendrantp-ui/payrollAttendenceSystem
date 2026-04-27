import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SlideBarComponent } from './slide-bar-component';

describe('SlideBarComponent', () => {
  let component: SlideBarComponent;
  let fixture: ComponentFixture<SlideBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SlideBarComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SlideBarComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
