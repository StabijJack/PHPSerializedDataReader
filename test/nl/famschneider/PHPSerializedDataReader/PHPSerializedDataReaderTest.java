package nl.famschneider.PHPSerializedDataReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PHPSerializedDataReaderTest {
    private String s = "a:8:{" +
            "s:16:\"from_email_field\";s:22:\"info@presentalkmaar.nl\";" +
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
    private PHPSerializedDataReader phpSerializedDataReader;

    @BeforeEach
    void setUp() {
        phpSerializedDataReader = new PHPSerializedDataReader(s);
    }

    @Test
    void getFieldMap() {
        Map<String, Object> fieldList = phpSerializedDataReader.getFieldMap();
    }

    @Test
    void isOptionString() {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.isOptionString("Present Alkmaar"));
        try {
            assertTrue(phpSerializedDataReader.isOptionString("from_name_field"));
            assertFalse(phpSerializedDataReader.isOptionString("smtp_settings"));
        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
    }

    @Test
    void isOptionBoolean() {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.isOptionBoolean("Present Alkmaar"));
        try {
            assertTrue(phpSerializedDataReader.isOptionBoolean("force_from_name_replace"));
            assertFalse(phpSerializedDataReader.isOptionBoolean("smtp_settings"));
        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
    }

    @Test
    void isOptionArray() {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.isOptionArray("Present Alkmaar"));
        try {
            assertFalse(phpSerializedDataReader.isOptionArray("force_from_name_replace"));
            assertTrue(phpSerializedDataReader.isOptionArray("smtp_settings"));
        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
    }

    @Test
    void getOption() {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOption("Present Alkmaar"));
        try {
            assertEquals("Present Alkmaar", phpSerializedDataReader.getOption("from_name_field"));
            assertFalse((Boolean) phpSerializedDataReader.getOption("force_from_name_replace"));
            Map<String, Object> array = (Map<String, Object>) phpSerializedDataReader.getOption("smtp_settings");
            assertFalse((Boolean) array.get("encrypt_pass"));
        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
    }

    @Test
    void getOptionString() {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOptionString("Present Alkmaar"));
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOptionString("force_from_name_replace"));
        try {
            assertEquals("Present Alkmaar", phpSerializedDataReader.getOptionString("from_name_field"));
        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
    }

    @Test
    void getOptionBoolean() {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOptionBoolean("Present Alkmaar"));
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOptionBoolean("from_name_field"));
        try {
            assertFalse(phpSerializedDataReader.getOptionBoolean("force_from_name_replace"));
        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
    }

    @Test
    void getOptionArray() {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOptionArray("Present Alkmaar"));
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOptionArray("from_name_field"));
        try {
            Map<String, Object> temp = phpSerializedDataReader.getOptionArray("smtp_settings");
            assertEquals("presentmail.nl", temp.get("host"));

        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
    }

    @Test
    void testIsOptionString() {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.isOptionString(new String[]{"smtp_settings", " pietje"}));
        try {
            assertTrue(phpSerializedDataReader.isOptionString(new String[]{"smtp_settings", "port"}));
            assertFalse(phpSerializedDataReader.isOptionString(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
    }

    @Test
    void testIsOptionBoolean() throws PHPSerializedDataReaderExeption {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.isOptionBoolean(new String[]{"smtp_settings", " pietje"}));
        try {
            assertFalse(phpSerializedDataReader.isOptionBoolean(new String[]{"smtp_settings", "port"}));
            assertTrue(phpSerializedDataReader.isOptionBoolean(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
    }

    @Test
    void testIsOptionArray() {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.isOptionArray(new String[]{"smtp_settings", " pietje"}));
        try {
            assertTrue(phpSerializedDataReader.isOptionArray(new String[]{"smtp_settings", "secret"}));
            assertFalse(phpSerializedDataReader.isOptionArray(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
    }

    @Test
    void testGetOption() {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOption(new String[]{"smtp_settings", " pietje"}));
        try {
            assertEquals("presentmail.nl", phpSerializedDataReader.getOption(new String[]{"smtp_settings", "host"}));
            assertFalse((Boolean) phpSerializedDataReader.getOption(new String[]{"smtp_settings", "force_from_name_replace"}));
            Map<String, Object> array = (Map<String, Object>) phpSerializedDataReader.getOption(new String[]{"smtp_settings", "host"});
            assertFalse((Boolean) array.get("force_from_name_replace"));
        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
    }

    @Test
    void testGetOptionString() {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOptionString(new String[]{"smtp_settings", " pietje"}));
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOptionString(new String[]{"smtp_settings", "force_from_name_replace"}));
        try {
            assertEquals("587", phpSerializedDataReader.getOptionString(new String[]{"smtp_settings", "port"}));
        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
    }

    @Test
    void testGetOptionBoolean() {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOptionBoolean(new String[]{"smtp_settings", " pietje"}));
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOptionBoolean(new String[]{"smtp_settings", "port"}));
        try {
            assertTrue(phpSerializedDataReader.getOptionBoolean(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
    }

    @Test
    void testGetOptionArray() {
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOptionArray(new String[]{"smtp_settings", " pietje"}));
        assertThrows(PHPSerializedDataReaderExeption.class, () -> phpSerializedDataReader.getOptionArray(new String[]{"smtp_settings", "port"}));
        try {
            assertTrue(phpSerializedDataReader.getOptionBoolean(new String[]{"smtp_settings", "host"}));
        } catch (PHPSerializedDataReaderExeption e) {
        }
        ;
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
}