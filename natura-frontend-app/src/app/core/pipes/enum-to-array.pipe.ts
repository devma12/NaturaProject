import { PipeTransform, Pipe } from '@angular/core';

@Pipe({name: 'enumToArray'})
export class EnumToArrayPipe implements PipeTransform {

  transform(value) : Object {
    return Object.keys(value).filter(e => !isNaN(+e)).map(o => { return {index: +o, name: value[o]}});
  }

  getKey(items: any, value: number) : string {
    const found = Object.keys(items).filter(e => !isNaN(+e) && +e === value).map(o => { return {index: +o, name: items[o]}});
    if (found && found.length === 1) {
      let obj = found[0];
      return obj['name'];
    } else
      return undefined;
  }

}