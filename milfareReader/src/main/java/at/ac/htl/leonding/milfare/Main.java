package at.ac.htl.leonding.milfare;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class Main {
    MifareReader reader;

    enum State {
        initial,
        cardFound,
        cardNotFound,
        idFound,
        card,
        end
    }

    String lastIdSend = "";
    State state = State.initial;

    public void run(String[] args) throws IOException {
        String id = "";
        findReader();
        while (state != State.end) {

            switch (state) {
                case initial:
                    reader.open();
                    setState(State.cardNotFound);
                    break;
                case cardNotFound:
                    if (id == null) {
                        id = "";
                    }
                    boolean cardFound = reader.enquiryCard();
                    if (cardFound) {
                        setState(State.cardFound);
                    }
                    break;
                case cardFound:
                    id = reader.anticollision();
                    if (id != null) {
                        setState(State.idFound);
                    }
                    break;
                case idFound:
                    if (!id.equals(lastIdSend)) {
                        try {
                            TagLoader tagLoader = new TagLoader();
                            tagLoader.sendData("http://141.147.28.105/api/nfccard/create", id);
                            System.out.println("data is send");
                            reader.close();
                            setState(State.initial);
                        }catch (Exception e){
                           e.printStackTrace();
                        }

                    }
                    setState(State.cardNotFound);
                    break;
                default:
                    System.out.println("Falscher Zustand");
                    break;
            }
        }
    }


    public static void main(String[] args) {
        var main = new Main();
        try {
            main.run(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void findReader() {
        reader = MifareReader.findReader();
        System.out.println("Reader: " + reader.toString());
        reader.open();
        reader.beep();
        reader.close();
    }


    private void setState(State state) {
        this.state = state;
        System.out.println("setting state" + state.name());
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
        System.out.println(isOpen ? "open" : "closed");
        reader.close();
        return isOpen;
    }

}
