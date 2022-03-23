package at.ac.htl.leonding.milfare;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortIOException;
import com.fazecast.jSerialComm.SerialPortTimeoutException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * interface for M302 Mifare Cardreader.
 */
public class MifareReader {

    public OutputStream os;

    /**
     * serial port where the reader is connected to.
     */
    final private SerialPort port;

    public MifareReader(SerialPort port) {
        this.port = port;
    }

    /**
     * send enquiry if module exist to the reader.
     *
     * @return true if reader is present
     */
    public boolean hello() {
        var ok = false;
        System.out.println("checking: " + port.getSystemPortName());

        os = port.getOutputStream();
        var greeting = new byte[]{0x03, 0x12, 0x00, 0x15};
        var response = new byte[]{0x02, 0x12, 0x14};

        ok = communicateWithReader(os, greeting, response);

        return ok;
    }

    public static MifareReader findReader() {
        SerialPort openPort = null;
        var comPorts = SerialPort.getCommPorts();
        var serialPort = Arrays.stream(comPorts).filter(port -> portIsOpen(port)).findFirst().orElse(null);
        return serialPort != null ? new MifareReader(serialPort) : null;
    }

    public static boolean portIsOpen(SerialPort comPort) {
        var reader = new MifareReader(comPort);
        reader.open();
        var isOpen = reader.hello();
        System.out.println(isOpen ? "open" : "closed");
        reader.close();
        return isOpen;
    }

    public boolean beep() {
        os = port.getOutputStream();
        var ok = false;

        var greeting = new byte[]{0x02, 0x13, 0x15};
        var response = greeting;

        ok = communicateWithReader(os, greeting, response);
        return ok;
    }

    public boolean enquiryCard() {
        os = port.getOutputStream();
        var ok = false;

        var greeting = new byte[]{0x03, 0x02, 0x00, 0x05};
        var response = new byte[]{0x03, 0x02, 0x01, 0x06};

        ok = communicateWithReader(os, greeting, response);
        return ok;
    }

    public String anticollision() {
        os = port.getOutputStream();

        var greeting = new byte[]{0x02, 0x03, 0x05};
        var response = new byte[]{0x06, 0x03, 0x32, 0x78, 0x13, (byte) 0x0E8, (byte) 0x0AE};

        byte[] bytes = getId(os, greeting);
        return bytes != null ? encodeHexString(bytes) : null;
    }

    //TODO: prüfziffer überprüfen
    public byte[] getId(OutputStream os, byte[] greeting) {
        byte[] buffer = new byte[7];
        byte[] returnBuffer = new byte[6];

        // 6 bytes werden eingelsen sonst null zurück
        try {
            os.write(greeting);
            os.flush();

            InputStream in = port.getInputStream();

            for (int j = 0; j < 5; ++j) {
                byte ch = (byte) in.read();
                buffer[j] = ch;
                var hex = Integer.toHexString(ch);
                System.out.print(hex);
            }
            returnBuffer = Arrays.copyOfRange(buffer, 0, 6);

            System.out.println("----");
            in.close();
        } catch (SerialPortTimeoutException ex) {
            System.out.println("not found");
            ex.printStackTrace();
            returnBuffer = null;
        } catch (SerialPortIOException e) {
            System.out.println("failed ");
            e.printStackTrace();
            returnBuffer = null;
        } catch (Exception e) {
            e.printStackTrace();
            returnBuffer = null;
        }
        return returnBuffer;
    }

    public boolean communicateWithReader(OutputStream os, byte[] greeting, byte[] response) {
        var ok = false;
        byte[] buffer = new byte[response.length];

        try {
            os.write(greeting);
            os.flush();

            InputStream in = port.getInputStream();

            for (int j = 0; j < buffer.length; ++j) {
                byte ch = (byte) in.read();
                buffer[j] = ch;
                var hex = Integer.toHexString(ch);
                System.out.print(hex);
            }

            if (Arrays.equals(buffer, response)) {
                ok = true;
            }
            System.out.println("+++++");
            in.close();
        } catch (SerialPortTimeoutException ex) {
            ex.printStackTrace();
            System.out.println("not found");
        } catch (SerialPortIOException e) {
            e.printStackTrace();
            System.out.println("failed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    public void open() {
        port.openPort();
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 250, 250);
    }

    public void close() {
        port.closePort();
        System.out.println("closed");
    }

    public String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }

    public String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    @Override
    public String toString() {
        return "MifareReader{" +
                "port=" + port +
                '}';
    }

}
