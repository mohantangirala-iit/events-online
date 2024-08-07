import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPersonType } from '../person-type.model';
import { PersonTypeService } from '../service/person-type.service';

@Component({
  standalone: true,
  templateUrl: './person-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PersonTypeDeleteDialogComponent {
  personType?: IPersonType;

  protected personTypeService = inject(PersonTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
