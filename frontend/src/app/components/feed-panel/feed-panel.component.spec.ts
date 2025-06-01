import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeedPanelComponent } from './feed-panel.component';

describe('FeedPanelComponent', () => {
  let component: FeedPanelComponent;
  let fixture: ComponentFixture<FeedPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedPanelComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FeedPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
