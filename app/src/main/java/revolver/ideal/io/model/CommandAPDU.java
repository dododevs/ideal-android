package revolver.ideal.io.model;

import java.util.Arrays;

import revolver.ideal.io.exceptions.InvalidAPDUPayloadException;

public class CommandAPDU {

    private final byte cla;
    private final byte ins;
    private final byte p1;
    private final byte p2;
    private byte lc;
    private byte[] data;
    private byte le;

    public CommandAPDU(byte cla, byte ins, byte p1, byte p2) {
        this.cla = cla;
        this.ins = ins;
        this.p1 = p1;
        this.p2 = p2;
    }

    public byte getApduClass() {
        return this.cla;
    }

    public byte getInstruction() {
        return this.ins;
    }

    public byte getParameter1() {
        return this.p1;
    }

    public byte getParameter2() {
        return this.p2;
    }

    public byte getCommandLength() {
        return this.lc;
    }

    public byte[] getData() {
        return data;
    }

    public byte getExpectedResponseLength() {
        return this.le;
    }

    public static CommandAPDU fromBytes(byte[] apdu) {
        if (apdu == null) {
            throw new InvalidAPDUPayloadException("payload array cannot be null");
        }
        if (apdu.length < 4) {
            throw new InvalidAPDUPayloadException(String.format(
                    "payload array is shorter than minimum length (%d)", apdu.length));
        }
        final CommandAPDU command = new CommandAPDU(
                apdu[0], apdu[1], apdu[2], apdu[3]
        );
        if (apdu.length >= 5) {
            command.lc = apdu[4];
        }
        command.data = Arrays.copyOfRange(apdu, 5, 5 + command.lc);
        if (apdu.length > 5 + command.lc) {
            command.le = apdu[5 + command.lc];
        }
        return command;
    }

}
