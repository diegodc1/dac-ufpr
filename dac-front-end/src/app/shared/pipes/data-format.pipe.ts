import { Pipe, PipeTransform } from '@angular/core';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';

@Pipe({
  name: 'dataFormat',
  standalone: true,
})
export class DataFormatPipe implements PipeTransform {
  transform(value: Date | string, formato: string = 'EEEE, dd MMMM yyyy'): string {
    const date = new Date(value);
    return format(date, formato, { locale: ptBR });
  }
}
