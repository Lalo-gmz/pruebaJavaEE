import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPaciente } from '../paciente.model';
import { PacienteService } from '../service/paciente.service';

@Component({
  standalone: true,
  templateUrl: './paciente-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PacienteDeleteDialogComponent {
  paciente?: IPaciente;

  constructor(
    protected pacienteService: PacienteService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pacienteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
