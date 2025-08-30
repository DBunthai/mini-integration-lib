package mini.integration.lib.module.util;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneLib {

    public static PhoneNumberUtil phoneNumberUtil() {
        return PhoneNumberUtil.getInstance();
    }

    public static Phonenumber.PhoneNumber phoneNumber() {
        return new Phonenumber.PhoneNumber();
    }

}
