package at.ac.htl.leonding.milfare;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class Main {
    static MifareReader reader;
    enum State {
        initial,
        cardFound,
        cardNotFound,
        end
    }
    static State state = State.initial;

    public static void main(String[] args) throws IOException {
        String id= "";
        findReader();
        while(state != State.end) {

            switch (state){
                case initial:
                     reader.open();
                     state = State.cardNotFound;
                     break;
                case cardNotFound:
                    if(id == null){
                        id = "";
                    }
                    if(!id.equals("")){
                        TagLoader tagLoader = new TagLoader();
                        tagLoader.sendData("http://152.70.175.53/api/nfccard/create", id);
                        System.out.println("data is send");
                        break;
                    }
                    id = reader.anticollision();
                    if(id != null){
                        state = State.cardFound;
                    }
                case cardFound:
                    if(reader.enquiryCard()){
                        var cardstate = reader.enquiryCard(); // if card is present enquiryCard is false
                        state = State.cardNotFound;
                    }
                    break;
                default:
                    System.out.println("Falsche Zustand");
                    break;
            }
        }
    }

    private static void findReader() {
        reader = MifareReader.findReader();
        System.out.println("Reader: " + reader.toString());
        reader.open();
        reader.beep();
        reader.close();
    }

/*    byte[] prot ={
        "enquiry_module": "\x03\x12\x00\x15",
                'enquiry_module_return': '\x02\x12\x14',
                'active_buzzer': '\x02\x13\x15',
                'enquiry_card': pack('BBBB', 03, 02, 00, 05),
                'enquiry_cards_return': '\x03\x02\x01\x06',
        'enquiry_no_card_found': '\x02\x01\x03',
        'enquiry_all_cards': '\x03\x02\x01\x05',
                'anticollision' : '\x02\x03\x05\x00',
                'select_card' : '\x02\x04\x06"}*/

    public static boolean portIsOpen(SerialPort comPort) {
        var reader = new MifareReader(comPort);
        reader.open();
        var isOpen = reader.hello();
        System.out.println(isOpen? "open" : "closed");
        reader.close();
        return isOpen;
    }

}
