package nl.famschneider.phpSerializedDataReader;

import java.util.HashMap;
import java.util.Map;

public class PHPSerializedDataReader {
    private final StringBuilder phpArraySerial;
    private final Map<String, Object> fieldMap;
    private Integer pointer;

    public PHPSerializedDataReader(String phpArraySerial) {
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
        return isOptionString(options[options.length - 1], getArray(options));
    }

    private Boolean isOptionString(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        optionExists(option, fieldMap);
        return getOption(option).getClass() == String.class;
    }

    public Boolean isOptionBoolean(String option) throws PHPSerializedDataReaderException {
        return isOptionBoolean(option, fieldMap);
    }

    public Boolean isOptionBoolean(String[] options) throws PHPSerializedDataReaderException {
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

    private void fillArrayFieldStructure() {
        pointer = 0;
        pointer++;//skip a
        pointer++; //skip :
        pointer++;//skip number
        if (phpArraySerial.charAt(pointer) >= '0' && phpArraySerial.charAt(pointer) <= '9') pointer++;
        if (phpArraySerial.charAt(pointer) >= '0' && phpArraySerial.charAt(pointer) <= '9') pointer++;
        pointer++; //skip :
        pointer++; //skip {
        while (phpArraySerial.charAt(pointer) != '}') {
            pointer++;//skip s fieldName identifier
            String fieldName = getStringData();
            //start value field
            char type = phpArraySerial.charAt(pointer);
            pointer++; //skip value identifier
            if (type == 's') {
                fieldMap.put(fieldName, getStringData());
            } else if (type == 'b') {
                fieldMap.put(fieldName, getBooleanData());
            } else if (type == 'a') {
                fieldMap.put(fieldName, getArrayData());
            }
        }
    }

    private Map<String, Object> getArrayData() {
        Map<String, Object> fieldNames = new HashMap<>();
        String fieldName;
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
            pointer++; //skip s fieldName identifier
            fieldName = getStringData();
            char type = phpArraySerial.charAt(pointer);
            pointer++; //skip value identifier
            if (type == 's') {
                fieldNames.put(fieldName, getStringData());
            } else if (type == 'b') {
                fieldNames.put(fieldName, getBooleanData());
            } else if (type == 'a') {
                fieldNames.put(fieldName, getArrayData());
            }
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

}




