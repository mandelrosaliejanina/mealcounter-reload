import {Component, OnInit} from '@angular/core';
import {BackendService} from "../../service/backend.service";
import {WebSocket} from "../../service/util";

export interface Person {
  id: number;
  nfcCard: NfcCard;
  firstName: string;
  lastName: string;
  entryYear: number;
}

export interface NfcCard {
  nfcId: string;
  registerDateTime: Date;
}

export interface NfcCardInfo {
  nfcId: string;
  registerDateTime: number;
}

export interface Consumation {
  id: number;
  person: Person;
  date: Date;
  hasConsumed: boolean;
}


@Component({
  selector: 'app-meals-overview',
  templateUrl: './meals-overview.component.html',
  styleUrls: ['./meals-overview.component.css']
})

export class MealsOverviewComponent implements OnInit {

  consumations: Consumation[];
  displayedColumns: string[] = ['id', 'personId', 'date', 'hasConsumed'];
  private socket: WebSocket<Consumation[]> | null;


  constructor(private readonly backend: BackendService) {

    this.consumations = [];
    this.socket = null;
  }

  ngOnInit(): void {
    this.backend.get('consumation').then(value => {
      this.consumations = value as Consumation[];
      console.log(this.consumations)
    });
    this.socket = new WebSocket<Consumation[]>('ws://141.147.28.105/start-websocket/test');
    this.socket.connect();
    // this.socket.sendMessage({nfcId:'12', registerDateTime:new Date() });
    this.socket.messages.subscribe(data => {
      console.log(JSON.stringify(data))
    })
  }
}
