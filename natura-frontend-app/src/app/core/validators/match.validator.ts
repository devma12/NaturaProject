import { UntypedFormGroup } from '@angular/forms';

export function matchValidator(form: UntypedFormGroup) { 
    const condition = form.get('new').value !== form.get('confirm').value;
    return condition ? { mismatch: true } : null;
}