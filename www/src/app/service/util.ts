import {webSocket, WebSocketSubject} from "rxjs/webSocket";
import {Subject} from "rxjs";

export const BASE_URL = 'localhost:8080';

export class WebSocket<T> {

  private socket: WebSocketSubject<T> | null;
  public messages: Subject<T>;
  public errorMessages: Subject<any>;

  constructor(public readonly endpointUrl: string) {
    this.socket = null;
    this.messages = new Subject();
    this.errorMessages = new Subject();
  }

  public sendMessage(message: T): void{
    if (this.socket?.closed){
      throw new SocketClosed();
    }
    this.socket?.next(message);
  }

  public connect(): void {
    this.socket = webSocket({
      url: this.endpointUrl,
      deserializer: msg => msg.data
    });
    this.socket.subscribe({
      next: msg => {
        this.messages.next(msg);
      },
      error: eMsg => {
        this.errorMessages.next(eMsg);
      }
    })
  }

  public close(): void {
    this.socket?.complete();
  }

  public get closed(): boolean | undefined{
    return this.socket?.closed;
  }
}

export class SocketClosed extends Error {}
