package nl.famschneider.PHPSerializedDataReader;

import java.util.HashMap;
import java.util.Map;

public class PHPSerializedDataReader {
    private Integer pointer;
    private StringBuilder phpArraySerial;
    private Map<String, Object> fieldMap;

    public PHPSerializedDataReader(String phpArraySerial) {
        this.pointer = 0;
        this.phpArraySerial = new StringBuilder(phpArraySerial);
        this.fieldMap = new HashMap<>();
        fillArrayFieldStructure();
    }

    public Map<String, Object> getFieldMap() {
        return fieldMap;
    }

    private Map<String, Object> getArray(String[] options) throws PHPSerializedDataReaderExeption {
        Map<String, Object> fieldMap = this.fieldMap;
        for (int i = 0; i < options.length - 1; i++) {
            fieldMap = getOptionArray(options[i], fieldMap);
        }
        return fieldMap;
    }

    public boolean isOption(String option) {
        try {
            optionExists(option, fieldMap);
        } catch (PHPSerializedDataReaderExeption e) {
            return false;
        }
        ;
        return true;
    }

    public boolean isOption(String[] options) {
        try {
            return isOption(options[options.length - 1], getArray(options));
        } catch (PHPSerializedDataReaderExeption e) {
            return false;
        }
    }

    private boolean isOption(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderExeption {
        return optionExists(option, fieldMap);
    }

    public Boolean isOptionString(String option) throws PHPSerializedDataReaderExeption {
        return isOptionString(option, fieldMap);
    }

    public Boolean isOptionString(String[] options) throws PHPSerializedDataReaderExeption {
        return isOptionString(options[options.length - 1], getArray(options));
    }

    private Boolean isOptionString(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderExeption {
        optionExists(option, fieldMap);
        return getOption(option).getClass() == String.class;
    }

    public Boolean isOptionBoolean(String option) throws PHPSerializedDataReaderExeption {
        return isOptionBoolean(option, fieldMap);
    }

    public Boolean isOptionBoolean(String[] options) throws PHPSerializedDataReaderExeption {
        return isOptionBoolean(options[options.length - 1], getArray(options));
    }

    private Boolean isOptionBoolean(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderExeption {
        optionExists(option, fieldMap);
        return getOption(option, fieldMap).getClass() == Boolean.class;
    }

    public Boolean isOptionArray(String option) throws PHPSerializedDataReaderExeption {
        return isOptionArray(option, fieldMap);
    }

    public Boolean isOptionArray(String[] options) throws PHPSerializedDataReaderExeption {
        return isOptionArray(options[options.length - 1], getArray(options));
    }

    private Boolean isOptionArray(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderExeption {
        optionExists(option, fieldMap);
        return getOption(option, fieldMap).getClass() == HashMap.class;
    }

    public Object getOption(String option) throws PHPSerializedDataReaderExeption {
        return getOption(option, fieldMap);
    }

    public Object getOption(String[] options) throws PHPSerializedDataReaderExeption {
        return getOption(options[options.length - 1], getArray(options));
    }

    private Object getOption(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderExeption {
        optionExists(option, fieldMap);
        return fieldMap.get(option);
    }

    public String getOptionString(String option) throws PHPSerializedDataReaderExeption {
        return getOptionString(option, fieldMap);
    }

    public String getOptionString(String[] options) throws PHPSerializedDataReaderExeption {
        return getOptionString(options[options.length - 1], getArray(options));
    }

    private String getOptionString(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderExeption {
        if (isOptionString(option, fieldMap)) return (String) getOption(option, fieldMap);
        throw new PHPSerializedDataReaderExeption("Is not a String");
    }

    public Boolean getOptionBoolean(String option) throws PHPSerializedDataReaderExeption {
        return getOptionBoolean(option, fieldMap);
    }

    public Boolean getOptionBoolean(String[] options) throws PHPSerializedDataReaderExeption {
        return getOptionBoolean(options[options.length - 1], getArray(options));
    }

    private Boolean getOptionBoolean(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderExeption {
        if (isOptionBoolean(option, fieldMap)) return (Boolean) getOption(option, fieldMap);
        throw new PHPSerializedDataReaderExeption(("is not a boolean"));
    }

    public Map<String, Object> getOptionArray(String option) throws PHPSerializedDataReaderExeption {
        return getOptionArray(option, fieldMap);
    }

    public Map<String, Object> getOptionArray(String[] options) throws PHPSerializedDataReaderExeption {
        return getOptionArray(options[options.length - 1], getArray(options));
    }

    private Map<String, Object> getOptionArray(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderExeption {
        if (isOptionArray(option, fieldMap)) return (Map<String, Object>) getOption(option, fieldMap);
        throw new PHPSerializedDataReaderExeption("Is not an array");
    }

    private boolean optionExists(String option) throws PHPSerializedDataReaderExeption {
        return optionExists(option, fieldMap);
    }

    private boolean optionExists(String[] options) throws PHPSerializedDataReaderExeption {
        return optionExists(options[options.length - 1], getArray(options));
    }

    private boolean optionExists(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderExeption {
        if (fieldMap.containsKey(option) == false) {
            throw new PHPSerializedDataReaderExeption("Option does not exist");
        }
        return true;
    }

    private void fillArrayFieldStructure() {
        pointer = 0;
        int endPointer = phpArraySerial.length();
        pointer++;//skip a
        pointer++; //skip :
        pointer++;//skip number
        if (phpArraySerial.charAt(pointer) >= '0' && phpArraySerial.charAt(pointer) <= '9') pointer++;
        if (phpArraySerial.charAt(pointer) >= '0' && phpArraySerial.charAt(pointer) <= '9') pointer++;
        pointer++; //skip :
        pointer++; //skip {
        while (phpArraySerial.charAt(pointer) != '}') {
            pointer++;//skip s fieldname identifier
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
        return;
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
            pointer++; //skip s fieldname identifier
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
        Boolean booleanData;
        if (phpArraySerial.charAt(pointer) == '0') {
            booleanData = false;
        } else {
            booleanData = true;
        }
        pointer++;
        pointer++; //skip ;
        return booleanData;
    }

}




