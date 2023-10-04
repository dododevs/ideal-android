package revolver.ideal;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;
import java.util.List;

import revolver.ideal.io.exceptions.InvalidAPDUPayloadException;
import revolver.ideal.io.model.CommandAPDU;

public class TestService extends HostApduService {

    public static final byte[] STATUS_FAILED = {
            0x6F, 0x00
    };
    public static final byte[] STATUS_OK = {
            (byte) 0x90, 0x00
    };
    public static final byte[] STATUS_CLA_NOT_SUPPORTED = {
            0x6E, 0x00
    };
    public static final byte[] STATUS_INS_NOT_SUPPORTED = {
            0x6D, 0x00
    };

    public static final byte[] AID = {
            (byte) 0xD2, 0x76, 0x00, 0x00, (byte) 0x85, 0x01, 0x01
    };
    public static final List<String> AIDS =
            Arrays.asList("D2760000850101", "D2760000850100");

    public static final byte[] CC_ID = {
            (byte) 0xE1, 0x03
    };
    public static final byte[] NDEF_ID = {
            (byte) 0xE1, 0x04
    };
    public static final byte[] CC = {
            0x00, 0x0F, /* length of CC file (15) */
            0x20,       /* mapping version (2.0) */
            0x00, 0x3B, /* MLe, maximum R-APDU data size (59 bytes) */
            0x00, 0x34, /* MLc, maximum C-APDU data size (52 bytes) */

            /* begin TLV section */
            0x04,
            0x06,
            NDEF_ID[0], NDEF_ID[1], /* NDEF file identifier */
            0x00, 0x32,             /* NDEF maximum size (50 bytes) */
            0x00,                   /* read access without security */
            0x00,                   /* write access without security */
    };

    public static byte[] NDEF_MSG = new NdefMessage(
            NdefRecord.createUri("https://www.google.com/search?q=scemone")).toByteArray();

    public static final byte SELECT_INS = (byte) 0xA4;
    public static final byte SELECT_BY_NAME = (byte) 0x04;
    public static final byte SELECT_BY_ID = 0x00;

    public static final byte READ_BINARY_INS = (byte) 0xB0;

    public static final byte DEFAULT_CLA = 0x00;

    private byte[] selectedNDEFFile = null;

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        String apduString = Hex.encodeHexString(apdu);
        Log.d("processCommandApdu", "command: " + apduString);

        final CommandAPDU command;
        try {
            command = CommandAPDU.fromBytes(apdu);
        } catch (InvalidAPDUPayloadException e) {
            Log.e("processCommandApdu", "parsing of apdu payload failed", e);
            return STATUS_FAILED;
        }
        if (command.getApduClass() != DEFAULT_CLA) {
            Log.e("processCommandApdu", "apdu with unsupported class");
            return STATUS_CLA_NOT_SUPPORTED;
        }

        switch (command.getInstruction()) {
            case SELECT_INS:
                return processSelectCommand(command);
            case READ_BINARY_INS:
                return processReadBinaryCommand(command);
            default:
                Log.w("processApduCommand",
                        "unsupported instruction " + command.getInstruction());
                return STATUS_INS_NOT_SUPPORTED;
        }
    }

    private byte[] processSelectCommand(CommandAPDU command) {
        if (command.getParameter1() == SELECT_BY_NAME) {
            String aid = Hex.encodeHexString(command.getData());
            if (AIDS.contains(aid)) {
                return STATUS_OK;
            } else {
                Log.w("processSelectCommand", String.format("apdu with wrong aid %s", aid));
            }
        } else if (command.getParameter1() == SELECT_BY_ID) {
            if (Arrays.equals(command.getData(), CC_ID)) {
                selectedNDEFFile = CC_ID;
                return STATUS_OK;
            } else if (Arrays.equals(command.getData(), NDEF_ID)) {
                selectedNDEFFile = NDEF_ID;
                return STATUS_OK;
            } else {
                Log.w("processSelectCommand", "apdu with wrong file id");
            }
        }
        return STATUS_FAILED;
    }

    private byte[] processReadBinaryCommand(CommandAPDU command) {
        if (Arrays.equals(selectedNDEFFile, CC_ID)) {
            Log.d("processReadBinary", "sending CC");
            return formatResponseAPDU(CC, STATUS_OK);
        } else if (Arrays.equals(selectedNDEFFile, NDEF_ID)) {
            if (command.getCommandLength() == 2) {
                Log.d("processReadBinary", "sending NDEF length");
                return new byte[] {
                        (byte) ((NDEF_MSG.length >> 8) & 0xFF),
                        (byte) (NDEF_MSG.length & 0xFF),
                        STATUS_OK[0],
                        STATUS_OK[1]
                };
            } else if (command.getCommandLength() == NDEF_MSG.length) {
                Log.d("processReadBinary", "sending NDEF content");
                return formatResponseAPDU(NDEF_MSG, STATUS_OK);
            }
        }
        return STATUS_FAILED;
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d("onDeactivated", "reason: " + reason);
    }

    private static byte[] formatResponseAPDU(byte[] data, byte[] status) {
        byte[] response = new byte[data.length + status.length];
        System.arraycopy(data, 0, response, 0, data.length);
        System.arraycopy(status, 0, response, data.length, status.length);
        return response;
    }
}
