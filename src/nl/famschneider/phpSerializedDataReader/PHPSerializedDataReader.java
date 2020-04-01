package nl.famschneider.phpSerializedDataReader;

import java.io.*;
import java.util.*;

public class PHPSerializedDataReader {
    private final StringBuilder phpArraySerial;
    private Map<String, Object> fieldMap;
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

    public Set<String> getOptionNamesTopLevel() {
        return fieldMap.keySet();
    }

    public Set<String> getOptionNamesOf(String groupName) {
        return getOptionNamesOf(groupName, fieldMap);
    }

    public Set<String> getOptionNamesOf(String[] options) {
        if (arrayWithOneElement(options)) return getOptionNamesOf(options[0]);
        try {
            return getOptionNamesOf(options[options.length - 1], getArray(options));
        } catch (PHPSerializedDataReaderException ignored) {
        }
        return new HashSet<>();
    }

    private Set<String> getOptionNamesOf(String groupName, Map<String, Object> fieldMap) {
        try {
            return getOptionArray(groupName, fieldMap).keySet();
        } catch (PHPSerializedDataReaderException ignored) {
        }
        return new HashSet<>();
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
        if (arrayWithOneElement(options)) return isOption(options[0]);
        try {
            return isOption(options[options.length - 1], getArray(options));
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    private boolean isOption(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        return optionExists(option, fieldMap);
    }

    public Boolean isOptionString(String option) {
        try {
            return isOptionString(option, fieldMap);
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    public Boolean isOptionString(String[] options) {
        if (arrayWithOneElement(options)) return isOptionString(options[0]);
        try {
            return isOptionString(options[options.length - 1], getArray(options));
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    private Boolean isOptionString(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        optionExists(option, fieldMap);
        return getOption(option, fieldMap).getClass() == String.class;
    }

    public Boolean isOptionInteger(String option) {
        try {
            return isOptionInteger(option, fieldMap);
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    public Boolean isOptionInteger(String[] options) {
        if (arrayWithOneElement(options)) return isOptionInteger(options[0]);
        try {
            return isOptionInteger(options[options.length - 1], getArray(options));
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    private Boolean isOptionInteger(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        optionExists(option, fieldMap);
        return getOption(option).getClass() == Integer.class;
    }

    public Boolean isOptionDouble(String option) {
        try {
            return isOptionDouble(option, fieldMap);
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    public Boolean isOptionDouble(String[] options) {
        if (arrayWithOneElement(options)) return isOptionDouble(options[0]);
        try {
            return isOptionDouble(options[options.length - 1], getArray(options));
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    private Boolean isOptionDouble(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        optionExists(option, fieldMap);
        return getOption(option).getClass() == Double.class;
    }

    public Boolean isOptionNull(String option) {
        try {
            return isOptionNull(option, fieldMap);
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    public Boolean isOptionNull(String[] options) {
        if (arrayWithOneElement(options)) return isOptionNull(options[0]);
        try {
            return isOptionNull(options[options.length - 1], getArray(options));
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    private Boolean isOptionNull(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        optionExists(option, fieldMap);
        return getOption(option) == null;
    }

    public Boolean isOptionBoolean(String option) {
        try {
            return isOptionBoolean(option, fieldMap);
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    public Boolean isOptionBoolean(String[] options) {
        if (arrayWithOneElement(options)) return isOptionBoolean(options[0]);
        try {
            return isOptionBoolean(options[options.length - 1], getArray(options));
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    private Boolean isOptionBoolean(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        optionExists(option, fieldMap);
        return getOption(option, fieldMap).getClass() == Boolean.class;
    }

    public Boolean isOptionArray(String option) {
        try {
            return isOptionArray(option, fieldMap);
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    public Boolean isOptionArray(String[] options) {
        if (arrayWithOneElement(options)) return isOptionArray(options[0]);
        try {
            return isOptionArray(options[options.length - 1], getArray(options));
        } catch (PHPSerializedDataReaderException e) {
            return false;
        }
    }

    private Boolean isOptionArray(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        optionExists(option, fieldMap);
        return getOption(option, fieldMap).getClass() == HashMap.class;
    }

    public Object getOption(String option) throws PHPSerializedDataReaderException {
        return getOption(option, fieldMap);
    }

    public Object getOption(String[] options) throws PHPSerializedDataReaderException {
        if (arrayWithOneElement(options)) return getOption(options[0]);
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
        if (arrayWithOneElement(options)) return getOptionString(options[0]);
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
        if (arrayWithOneElement(options)) return getOptionBoolean(options[0]);
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
        if (arrayWithOneElement(options)) return getOptionArray(options[0]);
        return getOptionArray(options[options.length - 1], getArray(options));
    }

    private Map<String, Object> getOptionArray(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        if (isOptionArray(option, fieldMap)) //noinspection unchecked
            return (Map<String, Object>) getOption(option, fieldMap);
        throw new PHPSerializedDataReaderException("Is not an array");
    }

    public Integer getOptionInteger(String option) throws PHPSerializedDataReaderException {
        return getOptionInteger(option, fieldMap);
    }

    public Integer getOptionInteger(String[] options) throws PHPSerializedDataReaderException {
        if (arrayWithOneElement(options)) return getOptionInteger(options[0]);
        return getOptionInteger(options[options.length - 1], getArray(options));
    }

    private Integer getOptionInteger(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        if (isOptionInteger(option, fieldMap)) return (Integer) getOption(option, fieldMap);
        throw new PHPSerializedDataReaderException("Is not an Integer");
    }

    public Double getOptionDouble(String option) throws PHPSerializedDataReaderException {
        return getOptionDouble(option, fieldMap);
    }

    public Double getOptionDouble(String[] options) throws PHPSerializedDataReaderException {
        if (arrayWithOneElement(options)) return getOptionDouble(options[0]);
        return getOptionDouble(options[options.length - 1], getArray(options));
    }

    private Double getOptionDouble(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        if (isOptionDouble(option, fieldMap)) return (Double) getOption(option, fieldMap);
        throw new PHPSerializedDataReaderException("Is not a Double");
    }

    public Object getOptionNull(String option) throws PHPSerializedDataReaderException {
        return getOptionNull(option, fieldMap);
    }

    public Object getOptionNull(String[] options) throws PHPSerializedDataReaderException {
        if (arrayWithOneElement(options)) return getOptionNull(options[0]);
        return getOptionNull(options[options.length - 1], getArray(options));
    }

    private Object getOptionNull(String option, Map<String, Object> fieldMap) throws PHPSerializedDataReaderException {
        if (isOptionNull(option, fieldMap)) return getOption(option, fieldMap);
        throw new PHPSerializedDataReaderException("Is not a Null");
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
        try {
            pointer = 0;
            if (phpArraySerial.charAt(pointer) == 'a') {
                pointer++;//skip a
                fieldMap = getArrayData();
            } else if (phpArraySerial.charAt(pointer) == '{') {
                fieldMap = new HashMap<>();
                fieldMap.put("root", getArrayStructure());
            } else
                throw new PHPSerializedDataReaderException("not an implemented SerializedPHPArray(starts with a) or ArrayStructure (starts with {)");
        } catch (StringIndexOutOfBoundsException e) {
            throw new PHPSerializedDataReaderException("string is not properly formed");
        }
    }

    private Map<String, Object> getArrayData() throws PHPSerializedDataReaderException {
        Map<String, Object> fieldMap = new HashMap<>();
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
            fieldMap.put(nameValuePair.name, nameValuePair.value);
            numberFields++;
        }
        pointer++;//skip }
        return fieldMap;
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

    @SuppressWarnings("SameReturnValue")
    private Object getNullData() {
        pointer++;//skip ;
        return null;
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

    private Double getDoubleData() {
        pointer++;//skip :
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(phpArraySerial.charAt(pointer));
        pointer++;
        while (phpArraySerial.charAt(pointer) != ';') {
            stringBuilder.append(phpArraySerial.charAt(pointer));
            pointer++;
        }
        pointer++; //skip ;
        return Double.valueOf(stringBuilder.toString());
    }

    private NameValuePair handleSequenceOfFields() throws PHPSerializedDataReaderException {
        String fieldName;
        if (phpArraySerial.charAt(pointer) == 's') {
            pointer++; //skip s fieldName identifier
            fieldName = getStringData();
        } else {
            pointer++; //skip i fieldName identifier
            fieldName = getIntegerData().toString();
        }
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
        } else if (type == 'd') {
            return new NameValuePair(fieldName, getDoubleData());
        } else if (type == 'N') {
            return new NameValuePair(fieldName, getNullData());
        }
        throw new PHPSerializedDataReaderException("not implemented type: " + type);
    }

    private Map<String, Object> getArrayStructure() {
        Map<String, Object> fieldMap = new HashMap<>();
        pointer++; //skip {
        while (phpArraySerial.charAt(pointer) != '}') {
            NameValuePair nameValuePair = getNameValuePair();
            fieldMap.put(nameValuePair.name, nameValuePair.value);
            if (phpArraySerial.charAt(pointer) == ',') pointer++; //skip ,
        }
        pointer++; //skip }
        return fieldMap;
    }

    private NameValuePair getNameValuePair() {
        String fieldName = getString();
        pointer++; //skip =
        if (phpArraySerial.charAt(pointer) == '{') {
            return new NameValuePair(fieldName, getArrayStructure());
        } else {
            return new NameValuePair(fieldName, getString());
        }
    }

    private String getString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (phpArraySerial.charAt(pointer) == '"') {
            pointer++; //skip "

            while (phpArraySerial.charAt(pointer) != '"') {
                if (phpArraySerial.charAt(pointer) == '\\') {
                    pointer++; //skip \
                }
                stringBuilder.append(phpArraySerial.charAt(pointer));
                pointer++;
            }
            pointer++; //skip "
        } else {
            while (phpArraySerial.charAt(pointer) != ',' && phpArraySerial.charAt(pointer) != '}') {
                stringBuilder.append(phpArraySerial.charAt(pointer));
                pointer++;
            }
            if (phpArraySerial.charAt(pointer) == ',') pointer++;
        }
        return stringBuilder.toString();
    }

    private Boolean arrayWithOneElement(String[] options) {
        return options.length == 1;
    }

    @Override
    public String toString() {
        return "PHPSerializedDataReader{" +
                "fieldMap=" + fieldMap +
                '}';
    }

    public int arrayDepth() {
        return arrayDepth(fieldMap);
    }

    @SuppressWarnings("unchecked")
    private int arrayDepth(Map<String, Object> fieldMap) {
        int depth = 1;
        for (String key : fieldMap.keySet()) {
            Object object = fieldMap.get(key);
            if (object != null && object.getClass() == HashMap.class) {
                depth += arrayDepth((Map<String, Object>) object);
            }
        }
        return depth;
    }

    public int arrayWidth() {
        return arrayWidth(fieldMap);
    }

    @SuppressWarnings("unchecked")
    private int arrayWidth(Map<String, Object> fieldMap) {
        int width = 0;
        for (String key : fieldMap.keySet()) {
            Object object = fieldMap.get(key);
            if (object != null && object.getClass() == HashMap.class) {
                width += arrayWidth((Map<String, Object>) object);
            } else width++;
        }
        return width;
    }

    private List<List<String>> printableArray() {
        List<List<String>> fieldArray = new ArrayList<>();
        int depth = arrayDepth(fieldMap) + 1;//For valueRow
        int width = arrayWidth(fieldMap);
        for (int d = 0; d < depth; d++) {
            List<String> row = new ArrayList<>();
            for (int w = 0; w < width; w++) {
                row.add("");
            }
            fieldArray.add(row);
        }
        fillFieldArray(fieldMap, fieldArray, new CurrentLocation());
        return fieldArray;
    }

    @SuppressWarnings("unchecked")
    private void fillFieldArray(Map<String, Object> fieldMap, List<List<String>> fieldArray, CurrentLocation l) {
        for (String key : fieldMap.keySet()) {
            Object object = fieldMap.get(key);
            if (object != null && object.getClass() == HashMap.class) {
                fieldArray.get(l.row).set(l.col, key);
                l.row++;
                fillFieldArray((Map<String, Object>) object, fieldArray, l);
                l.row--;
            } else {
                fieldArray.get(l.row).set(l.col, key);
                if (object != null) {
                    fieldArray.get(fieldArray.size() - 1).set(l.col, object.toString());
                } else {
                    fieldArray.get(fieldArray.size() - 1).set(l.col, "null");
                }
                l.col++;
            }

        }
    }

    public void exportFieldMapToExcelCSVFile(String path) throws FileNotFoundException {
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream)) {
            for (List<String> row : printableArray()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String col : row) {
                    stringBuilder.append("\"");
                    String t = col.replaceAll("\"", "\"\"");//Excel must have two " to show "
                    stringBuilder.append(t);
                    stringBuilder.append("\";");
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                stringBuilder.append("\n");
                outputStreamWriter.write(stringBuilder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}

class NameValuePair {
    final String name;
    final Object value;

    public NameValuePair(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}

class CurrentLocation {
    int row;
    int col;

}




