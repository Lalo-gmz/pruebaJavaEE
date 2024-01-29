import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IConsultorio } from '../consultorio.model';
import { ConsultorioService } from '../service/consultorio.service';

@Component({
  standalone: true,
  templateUrl: './consultorio-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ConsultorioDeleteDialogComponent {
  consultorio?: IConsultorio;

  constructor(
    protected consultorioService: ConsultorioService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.consultorioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
