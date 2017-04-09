package com.knowroaming.service.Utils;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by cerokuo on 02/04/2017.
 */
public final class DataEntryUtils {
    private static final Logger LOGGER = Logger.getLogger(DataEntryUtils.class.getName() );

    public static final Pattern VALID_NAME_REGEX = Pattern.compile("^[a-zA-Z]+$");
    public static final Pattern VALID_PHONE_NUMBER = Pattern.compile("^[0-9-]+$");
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static boolean isValidUserName(String name) {
        return VALID_NAME_REGEX.matcher(name).find();
    }

    public static boolean isValidEmail(String email) {
        //email could be also checked with a more complex regexp.
        String[] checkAt = email.split("@");
        if(checkAt.length == 2) {
            if(checkAt[1].split("\\.").length == 2) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidPhoneNumber(String phone) {
        return VALID_PHONE_NUMBER.matcher(phone).find();

    }

    public static Timestamp convertStringDateToTimestamp(String stringDate) {
        try {
            DateFormat formatter =  new SimpleDateFormat(DATE_FORMAT);
            Date date = formatter.parse(stringDate);
            Timestamp timeStampDate = new Timestamp(date.getTime());
            return timeStampDate;
        } catch (ParseException e) {
            LOGGER.log(Level.ALL,"Error trying to Parse the String date, format not correct.");
            throw new InvalidParameterException("Date format error: "+ e);
        }
    }

    public static boolean isDateBiggerThan(Timestamp startDateFormated, Timestamp endDateFormated) {
        return startDateFormated.after(endDateFormated);
    }
}

