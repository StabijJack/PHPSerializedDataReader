package nl.famschneider.phpSerializedDataReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"CatchMayIgnoreException","SpellCheckingInspection"})
class PHPSerializedDataReaderTest {
    private PHPSerializedDataReader phpSerializedDataReader;

    @BeforeEach
    void setUp() {
        String s = "a:8:{" +
                "s:16:\"from_email_field\";s:22:\"info@presentalkmaar.nl\";" +
                "s:7:\"Integer\";i:55246;"+
                "s:6:\"Double\";d:55.246;"+
                "s:15:\"from_name_field\";s:15:\"Present Alkmaar\";" +
                "s:13:\"smtp_settings\";a:8:{" +
                "s:4:\"host\";s:14:\"presentmail.nl\";" +
                "s:15:\"type_encryption\";s:3:\"tls\";" +
                "s:4:\"port\";s:3:\"587\";" +
                "s:13:\"autentication\";s:3:\"yes\";" +
                "s:8:\"username\";s:22:\"smtp@presentalkmaar.nl\";" +
                "s:8:\"password\";s:16:\"alpANTMtMTItMTg=\";" +
                "s:6:\"secret\";a:2:{" +
                "s:12:\"enable_debug\";b:1;" +
                "s:12:\"insecure_ssl\";b:0;}" +
                "s:12:\"encrypt_pass\";b:0;}" +
                "s:23:\"force_from_name_replace\";b:0;" +
                "s:14:\"reply_to_email\";s:0:\"\";" +
                "s:17:\"email_ignore_list\";s:0:\"\";" +
                "s:19:\"enable_domain_check\";b:0;" +
                "s:15:\"allowed_domains\";s:0:\"\";" +
                "}";
        try {
            phpSerializedDataReader = new PHPSerializedDataReader(s);
        } catch (PHPSerializedDataReaderException e){
            System.out.println( e.getMessage());
        }
    }

    @Test
    void getFieldMap() {
        Map<String, Object> fieldMap = phpSerializedDataReader.getFieldMap();
        assertTrue(fieldMap instanceof HashMap);
    }

    @Test
    void isOptionString() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.isOptionString("Present Alkmaar"));
        try {
            assertTrue(phpSerializedDataReader.isOptionString("from_name_field"));
            assertFalse(phpSerializedDataReader.isOptionString("smtp_settings"));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void isOptionBoolean() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.isOptionBoolean("Present Alkmaar"));
        try {
            assertTrue(phpSerializedDataReader.isOptionBoolean("force_from_name_replace"));
            assertFalse(phpSerializedDataReader.isOptionBoolean("smtp_settings"));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void isOptionArray() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.isOptionArray("Present Alkmaar"));
        try {
            assertFalse(phpSerializedDataReader.isOptionArray("force_from_name_replace"));
            assertTrue(phpSerializedDataReader.isOptionArray("smtp_settings"));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void getOption() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOption("Present Alkmaar"));
        try {
            assertEquals("Present Alkmaar", phpSerializedDataReader.getOption("from_name_field"));
            assertFalse((Boolean) phpSerializedDataReader.getOption("force_from_name_replace"));
            @SuppressWarnings({"unchecked", "SpellCheckingInspection"}) Map<String, Object> array = (Map<String, Object>) phpSerializedDataReader.getOption("smtp_settings");
            assertFalse((Boolean) array.get("encrypt_pass"));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void getOptionString() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionString("Present Alkmaar"));
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionString("force_from_name_replace"));
        try {
            assertEquals("Present Alkmaar", phpSerializedDataReader.getOptionString("from_name_field"));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void getOptionBoolean() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionBoolean("Present Alkmaar"));
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionBoolean("from_name_field"));
        try {
            assertFalse(phpSerializedDataReader.getOptionBoolean("force_from_name_replace"));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void getOptionArray() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionArray("Present Alkmaar"));
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionArray("from_name_field"));
        try {
            @SuppressWarnings("SpellCheckingInspection") Map<String, Object> temp = phpSerializedDataReader.getOptionArray("smtp_settings");
            assertEquals("presentmail.nl", temp.get("host"));

        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void testIsOptionString() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.isOptionString(new String[]{"smtp_settings", " pietje"}));
        try {
            assertTrue(phpSerializedDataReader.isOptionString(new String[]{"smtp_settings", "port"}));
            assertFalse(phpSerializedDataReader.isOptionString(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void testIsOptionBoolean() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.isOptionBoolean(new String[]{"smtp_settings", " pietje"}));
        try {
            assertFalse(phpSerializedDataReader.isOptionBoolean(new String[]{"smtp_settings", "port"}));
            assertTrue(phpSerializedDataReader.isOptionBoolean(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void testIsOptionArray() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.isOptionArray(new String[]{"smtp_settings", " pietje"}));
        try {
            assertTrue(phpSerializedDataReader.isOptionArray(new String[]{"smtp_settings", "secret"}));
            assertFalse(phpSerializedDataReader.isOptionArray(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void testGetOption() {
        // noinspection SpellCheckingInspection
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOption(new String[]{"smtp_settings", " pietje"}));
        try {
            assertEquals("presentmail.nl", phpSerializedDataReader.getOption(new String[]{"smtp_settings", "host"}));
            assertFalse((Boolean) phpSerializedDataReader.getOption(new String[]{"smtp_settings", "force_from_name_replace"}));
            @SuppressWarnings({"unchecked", "SpellCheckingInspection"}) Map<String, Object> array = (Map<String, Object>) phpSerializedDataReader.getOption(new String[]{"smtp_settings", "host"});
            assertFalse((Boolean) array.get("force_from_name_replace"));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void testGetOptionString() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionString(new String[]{"smtp_settings", " pietje"}));
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionString(new String[]{"smtp_settings", "force_from_name_replace"}));
        try {
            assertEquals("587", phpSerializedDataReader.getOptionString(new String[]{"smtp_settings", "port"}));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void testGetOptionBoolean() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionBoolean(new String[]{"smtp_settings", " pietje"}));
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionBoolean(new String[]{"smtp_settings", "port"}));
        try {
            assertTrue(phpSerializedDataReader.getOptionBoolean(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void testGetOptionArray() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionArray(new String[]{"smtp_settings", " pietje"}));
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionArray(new String[]{"smtp_settings", "port"}));
        try {
            assertTrue(phpSerializedDataReader.getOptionArray(new String[]{"smtp_settings", "host"}) instanceof HashMap);
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void isOption() {
        assertTrue(phpSerializedDataReader.isOption("from_email_field"));
        assertFalse(phpSerializedDataReader.isOption("from_e"));
    }

    @Test
    void testIsOption() {
        assertTrue(phpSerializedDataReader.isOption(new String[] {"smtp_settings","host"}));
        assertFalse(phpSerializedDataReader.isOption(new String[] {"smtp_settings","host","piet"}));
    }

    @Test
    void isOptionInteger() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.isOptionInteger("Present Alkmaar"));
        try {
            assertTrue(phpSerializedDataReader.isOptionInteger("Integer"));
            assertFalse(phpSerializedDataReader.isOptionInteger("smtp_settings"));
        } catch (PHPSerializedDataReaderException e) {
        }

    }

    @Test
    void testIsOptionInteger() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.isOptionInteger(new String[]{"smtp_settings", " pietje"}));
        try {
            assertTrue(phpSerializedDataReader.isOptionInteger(new String[]{"Integer"}));
            assertFalse(phpSerializedDataReader.isOptionInteger(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPSerializedDataReaderException e) {
        }

    }

    @Test
    void getOptionInteger() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionInteger("Present Alkmaar"));
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionInteger("force_from_name_replace"));
        try {
            assertEquals(55246, phpSerializedDataReader.getOptionInteger("Integer"));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void testGetOptionInteger() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionInteger(new String[]{"smtp_settings", " pietje"}));
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionInteger(new String[]{"smtp_settings", "force_from_name_replace"}));
        try {
            assertEquals(55246, phpSerializedDataReader.getOptionInteger(new String[]{"Integer"}));
        } catch (PHPSerializedDataReaderException e) {
        }
    }
    @Test
    void isOptionDouble() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.isOptionDouble("Present Alkmaar"));
        try {
            assertTrue(phpSerializedDataReader.isOptionDouble("Double"));
            assertFalse(phpSerializedDataReader.isOptionDouble("smtp_settings"));
        } catch (PHPSerializedDataReaderException e) {
        }

    }

    @Test
    void testIsOptionDouble() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.isOptionDouble(new String[]{"smtp_settings", " pietje"}));
        try {
            assertTrue(phpSerializedDataReader.isOptionDouble(new String[]{"Double"}));
            assertFalse(phpSerializedDataReader.isOptionDouble(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPSerializedDataReaderException e) {
        }

    }

    @Test
    void getOptionDouble() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionDouble("Present Alkmaar"));
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionDouble("force_from_name_replace"));
        try {
            assertEquals(55.246, phpSerializedDataReader.getOptionDouble("Double"));
        } catch (PHPSerializedDataReaderException e) {
        }
    }

    @Test
    void testGetOptionDouble() {
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionDouble(new String[]{"smtp_settings", " pietje"}));
        assertThrows(PHPSerializedDataReaderException.class, () -> phpSerializedDataReader.getOptionDouble(new String[]{"smtp_settings", "force_from_name_replace"}));
        try {
            assertEquals(55.246, phpSerializedDataReader.getOptionDouble(new String[]{"Double"}));
        } catch (PHPSerializedDataReaderException e) {
        }
    }
}