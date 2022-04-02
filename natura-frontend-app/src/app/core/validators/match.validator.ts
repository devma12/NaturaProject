import { FormGroup } from '@angular/forms';

export function matchValidator(form: FormGroup) { 
    const condition = form.get('new').value !== form.get('confirm').value;
    return condition ? { mismatch: true } : null;
}