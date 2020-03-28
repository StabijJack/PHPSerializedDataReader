package nl.famschneider.phpSerializedDataReader;

import java.util.HashMap;
import java.util.Map;

public class PHPSerializedDataReader {
    private final StringBuilder phpArraySerial;
    private final Map<String, Object> fieldMap;
    private Integer pointer;

    public PHPSerializedDataReader(String phpArraySerial) throws PHPSerializedDataReaderException {
        this.pointer = 0;
        this.phpArraySerial = new StringBuilder(phpArraySerial);
        this.fieldMap = new HashMap<>();
        fillArrayFieldStructure();
    }

    public Map<String, Object> getFieldMap() {
        return fieldMap;
    }

    private Map<String, Object> getArray(String[] options) throws PHPSerializedDataReaderException {
        Map<String, Object> fieldMap = this.fieldMap;
        for (int i = 0; i < options.length - 1; i++) {
            fieldMap = getOptionArray(options[i], fieldMap);
        }
        return fieldMap;
    }

    public boolean isOption(String option) {
        try {
            optionExists(option, fieldMap);
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
        return true;
    }

    public boolean isOption(String[] options) {
        if (arrayWithOneElement(options))return isOption(options[0]);
        try {
            return isOption(options[options.length - 1], getArray(options));
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    private boolean isOption(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        return optionExists(option, fieldMap);
    }

    public Boolean isOptionString(String option) throws PHPSerializedDataReaderException {
        return isOptionString(option, fieldMap);
    }

    public Boolean isOptionString(String[] options) throws PHPSerializedDataReaderException {
        if (arrayWithOneElement(options))return isOptionString(options[0]);
        return isOptionString(options[options.length - 1], getArray(options));
    }

    private Boolean isOptionString(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        optionExists(option, fieldMap);
        return getOption(option).getClass() == String.class;
    }

    public Boolean isOptionInteger(String option) throws PHPSerializedDataReaderException {
        return isOptionInteger(option, fieldMap);
    }

    public Boolean isOptionInteger(String[] options) throws PHPSerializedDataReaderException {
        if (arrayWithOneElement(options))return isOptionInteger(options[0]);
        return isOptionString(options[options.length - 1], getArray(options));
    }

    private Boolean isOptionInteger(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        optionExists(option, fieldMap);
        return getOption(option).getClass() == Integer.class;
    }

    public Boolean isOptionBoolean(String option) throws PHPSerializedDataReaderException {
        return isOptionBoolean(option, fieldMap);
    }

    public Boolean isOptionBoolean(String[] options) throws PHPSerializedDataReaderException {
        if (arrayWithOneElement(options))return isOptionBoolean(options[0]);
        return isOptionBoolean(options[options.length - 1], getArray(options));
    }

    private Boolean isOptionBoolean(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        optionExists(option, fieldMap);
        return getOption(option, fieldMap).getClass() == Boolean.class;
    }

    public Boolean isOptionArray(String option) throws PHPSerializedDataReaderException {
        return isOptionArray(option, fieldMap);
    }

    public Boolean isOptionArray(String[] options) throws PHPSerializedDataReaderException {
        if (arrayWithOneElement(options))return isOptionArray(options[0]);
        return isOptionArray(options[options.length - 1], getArray(options));
    }

    private Boolean isOptionArray(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        optionExists(option, fieldMap);
        return getOption(option, fieldMap).getClass() == HashMap.class;
    }

    public Object getOption(String option) throws PHPSerializedDataReaderException {
        return getOption(option, fieldMap);
    }

    public Object getOption(String[] options) throws PHPSerializedDataReaderException {
        if (arrayWithOneElement(options))return getOption(options[0]);
        return getOption(options[options.length - 1], getArray(options));
    }

    private Object getOption(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        optionExists(option, fieldMap);
        return fieldMap.get(option);
    }

    public String getOptionString(String option) throws PHPSerializedDataReaderException {
        return getOptionString(option, fieldMap);
    }

    public String getOptionString(String[] options) throws PHPSerializedDataReaderException {
        if (arrayWithOneElement(options))return getOptionString(options[0]);
        return getOptionString(options[options.length - 1], getArray(options));
    }

    private String getOptionString(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        if (isOptionString(option, fieldMap)) return (String) getOption(option, fieldMap);
        throw new PHPSerializedDataReaderException("Is not a String");
    }

    public Boolean getOptionBoolean(String option) throws PHPSerializedDataReaderException {
        return getOptionBoolean(option, fieldMap);
    }

    public Boolean getOptionBoolean(String[] options) throws PHPSerializedDataReaderException {
        if (arrayWithOneElement(options))return getOptionBoolean(options[0]);
        return getOptionBoolean(options[options.length - 1], getArray(options));
    }

    private Boolean getOptionBoolean(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        if (isOptionBoolean(option, fieldMap)) return (Boolean) getOption(option, fieldMap);
        throw new PHPSerializedDataReaderException(("is not a boolean"));
    }

    public Map<String, Object> getOptionArray(String option) throws PHPSerializedDataReaderException {
        return getOptionArray(option, fieldMap);
    }

    public Map<String, Object> getOptionArray(String[] options) throws PHPSerializedDataReaderException {
        if (arrayWithOneElement(options))return getOptionArray(options[0]);
        return getOptionArray(options[options.length - 1], getArray(options));
    }

    private Map<String, Object> getOptionArray(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        if (isOptionArray(option, fieldMap)) //noinspection unchecked
            return (Map<String, Object>) getOption(option, fieldMap);
        throw new PHPSerializedDataReaderException("Is not an array");
    }

// --Commented out by Inspection START (28-3-2020 12:59):
//    private boolean optionExists(String option) throws PHPSerializedDataReaderException {
//        return optionExists(option, fieldMap);
//    }
// --Commented out by Inspection STOP (28-3-2020 12:59)

// --Commented out by Inspection START (28-3-2020 12:59):
//    private boolean optionExists(String[] options) throws PHPSerializedDataReaderException {
//        return optionExists(options[options.length - 1], getArray(options));
//    }
// --Commented out by Inspection STOP (28-3-2020 12:59)

    @SuppressWarnings("SameReturnValue")
    private boolean optionExists(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        if (!fieldMap.containsKey(option)) {
            throw new PHPSerializedDataReaderException("Option does not exist");
        }
        return true;
    }

    private void fillArrayFieldStructure() throws PHPSerializedDataReaderException {
        pointer = 0;
        pointer++;//skip a
        pointer++; //skip :
        pointer++;//skip number
        if (phpArraySerial.charAt(pointer) >= '0' && phpArraySerial.charAt(pointer) <= '9') pointer++;
        if (phpArraySerial.charAt(pointer) >= '0' && phpArraySerial.charAt(pointer) <= '9') pointer++;
        pointer++; //skip :
        pointer++; //skip {
        while (phpArraySerial.charAt(pointer) != '}') {
            NameValuePair nameValuePair = handleSequenceOfFields();
            fieldMap.put((String) nameValuePair.Name, nameValuePair.Value);
        }
    }

    private Map<String, Object> getArrayData() throws PHPSerializedDataReaderException {
        Map<String, Object> fieldNames = new HashMap<>();
        pointer++; //skip :
        int arrayLength = Character.getNumericValue(phpArraySerial.charAt(pointer));
        pointer++;
        if (phpArraySerial.charAt(pointer) >= '0' && phpArraySerial.charAt(pointer) <= '9') {
            arrayLength = arrayLength * 10 + Character.getNumericValue(phpArraySerial.charAt(pointer));
            pointer++;
        }
        if (phpArraySerial.charAt(pointer) >= '0' && phpArraySerial.charAt(pointer) <= '9') {
            arrayLength = arrayLength * 10 + Character.getNumericValue(phpArraySerial.charAt(pointer));
            pointer++;
        }
        pointer++; //skip:
        pointer++; //skip {
        int numberFields = 0;
        while (numberFields < arrayLength) {
            NameValuePair nameValuePair = handleSequenceOfFields();
            fieldNames.put((String) nameValuePair.Name, nameValuePair.Value);
            numberFields++;
        }
        pointer++;//skip }
        return fieldNames;
    }

    private String getStringData() {
        pointer++; // skip :
        int stringLength = Character.getNumericValue(phpArraySerial.charAt(pointer));
        pointer++;
        if (phpArraySerial.charAt(pointer) >= '0' && phpArraySerial.charAt(pointer) <= '9') {
            stringLength = stringLength * 10 + Character.getNumericValue(phpArraySerial.charAt(pointer));
            pointer++;
        }
        if (phpArraySerial.charAt(pointer) >= '0' && phpArraySerial.charAt(pointer) <= '9') {
            stringLength = stringLength * 10 + Character.getNumericValue(phpArraySerial.charAt(pointer));
            pointer++;
        }
        pointer++; //skip:
        pointer++; //skip "
        String stringData = phpArraySerial.substring(pointer, pointer + stringLength);
        pointer += stringLength;
        pointer++; //skip "
        pointer++; //skip ;
        return stringData;
    }

    private Boolean getBooleanData() {
        pointer++;//skip :
        boolean booleanData;
        booleanData = phpArraySerial.charAt(pointer) != '0';
        pointer++;
        pointer++; //skip ;
        return booleanData;
    }

    private Integer getIntegerData() {
        pointer++;//skip :
        int integer = Character.getNumericValue(phpArraySerial.charAt(pointer));
        pointer++;
        while (phpArraySerial.charAt(pointer) != ';') {
            integer = integer * 10 + Character.getNumericValue(phpArraySerial.charAt(pointer));
            pointer++;
        }
        pointer++; //skip ;
        return integer;
    }

    private NameValuePair handleSequenceOfFields() throws PHPSerializedDataReaderException {
        pointer++; //skip s fieldName identifier
        String fieldName = getStringData();
        char type = phpArraySerial.charAt(pointer);
        pointer++; //skip value identifier
        if (type == 's') {
            return new NameValuePair(fieldName, getStringData());
        } else if (type == 'b') {
            return new NameValuePair(fieldName, getBooleanData());
        } else if (type == 'a') {
            return new NameValuePair(fieldName, getArrayData());
        } else if (type == 'i') {
            return new NameValuePair(fieldName, getIntegerData());
        }
        throw new PHPSerializedDataReaderException("not implemented type: " + type);
    }

    private Boolean arrayWithOneElement(String[] options) {
        return options.length == 1;
    }
}

class NameValuePair {
    final Object Name;
    final Object Value;

    public NameValuePair(Object name, Object value) {
        Name = name;
        Value = value;
    }
}




