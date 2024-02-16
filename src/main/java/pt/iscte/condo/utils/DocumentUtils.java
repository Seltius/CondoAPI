package pt.iscte.condo.utils;


import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class DocumentUtils {

    public byte[] stringToBytes(String string) {
        return string == null ? null : Base64.getDecoder().decode(string);
    }

}
