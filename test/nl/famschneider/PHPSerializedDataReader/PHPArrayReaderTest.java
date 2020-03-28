package nl.famschneider.PHPSerializedDataReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PHPArrayReaderTest {
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
    private PHPArrayReader phpArrayReader;

    @BeforeEach
    void setUp() {
        phpArrayReader = new PHPArrayReader(s);
    }

    @Test
    void getFieldMap() {
        Map<String, Object> fieldList = phpArrayReader.getFieldMap();
    }

    @Test
    void isOptionString() {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.isOptionString("Present Alkmaar"));
        try {
            assertTrue(phpArrayReader.isOptionString("from_name_field"));
            assertFalse(phpArrayReader.isOptionString("smtp_settings"));
        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void isOptionBoolean() {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.isOptionBoolean("Present Alkmaar"));
        try {
            assertTrue(phpArrayReader.isOptionBoolean("force_from_name_replace"));
            assertFalse(phpArrayReader.isOptionBoolean("smtp_settings"));
        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void isOptionArray() {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.isOptionArray("Present Alkmaar"));
        try {
            assertFalse(phpArrayReader.isOptionArray("force_from_name_replace"));
            assertTrue(phpArrayReader.isOptionArray("smtp_settings"));
        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void getOption() {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOption("Present Alkmaar"));
        try {
            assertEquals("Present Alkmaar", phpArrayReader.getOption("from_name_field"));
            assertFalse((Boolean) phpArrayReader.getOption("force_from_name_replace"));
            Map<String, Object> array = (Map<String, Object>) phpArrayReader.getOption("smtp_settings");
            assertFalse((Boolean) array.get("encrypt_pass"));
        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void getOptionString() {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOptionString("Present Alkmaar"));
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOptionString("force_from_name_replace"));
        try {
            assertEquals("Present Alkmaar", phpArrayReader.getOptionString("from_name_field"));
        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void getOptionBoolean() {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOptionBoolean("Present Alkmaar"));
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOptionBoolean("from_name_field"));
        try {
            assertFalse(phpArrayReader.getOptionBoolean("force_from_name_replace"));
        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void getOptionArray() {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOptionArray("Present Alkmaar"));
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOptionArray("from_name_field"));
        try {
            Map<String, Object> temp = phpArrayReader.getOptionArray("smtp_settings");
            assertEquals("presentmail.nl", temp.get("host"));

        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void testIsOptionString() {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.isOptionString(new String[]{"smtp_settings", " pietje"}));
        try {
            assertTrue(phpArrayReader.isOptionString(new String[]{"smtp_settings", "port"}));
            assertFalse(phpArrayReader.isOptionString(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void testIsOptionBoolean() throws PHPArrayReaderExeption {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.isOptionBoolean(new String[]{"smtp_settings", " pietje"}));
        try {
            assertFalse(phpArrayReader.isOptionBoolean(new String[]{"smtp_settings", "port"}));
            assertTrue(phpArrayReader.isOptionBoolean(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void testIsOptionArray() {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.isOptionArray(new String[]{"smtp_settings", " pietje"}));
        try {
            assertTrue(phpArrayReader.isOptionArray(new String[]{"smtp_settings", "secret"}));
            assertFalse(phpArrayReader.isOptionArray(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void testGetOption() {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOption(new String[]{"smtp_settings", " pietje"}));
        try {
            assertEquals("presentmail.nl", phpArrayReader.getOption(new String[]{"smtp_settings", "host"}));
            assertFalse((Boolean) phpArrayReader.getOption(new String[]{"smtp_settings", "force_from_name_replace"}));
            Map<String, Object> array = (Map<String, Object>) phpArrayReader.getOption(new String[]{"smtp_settings", "host"});
            assertFalse((Boolean) array.get("force_from_name_replace"));
        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void testGetOptionString() {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOptionString(new String[]{"smtp_settings", " pietje"}));
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOptionString(new String[]{"smtp_settings", "force_from_name_replace"}));
        try {
            assertEquals("587", phpArrayReader.getOptionString(new String[]{"smtp_settings", "port"}));
        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void testGetOptionBoolean() {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOptionBoolean(new String[]{"smtp_settings", " pietje"}));
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOptionBoolean(new String[]{"smtp_settings", "port"}));
        try {
            assertTrue(phpArrayReader.getOptionBoolean(new String[]{"smtp_settings", "force_from_name_replace"}));
        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void testGetOptionArray() {
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOptionArray(new String[]{"smtp_settings", " pietje"}));
        assertThrows(PHPArrayReaderExeption.class, () -> phpArrayReader.getOptionArray(new String[]{"smtp_settings", "port"}));
        try {
            assertTrue(phpArrayReader.getOptionBoolean(new String[]{"smtp_settings", "host"}));
        } catch (PHPArrayReaderExeption e) {
        }
        ;
    }

    @Test
    void isOption() {
        assertTrue(phpArrayReader.isOption("from_email_field"));
        assertFalse(phpArrayReader.isOption("from_e"));
    }

    @Test
    void testIsOption() {
        assertTrue(phpArrayReader.isOption(new String[] {"smtp_settings","host"}));
        assertFalse(phpArrayReader.isOption(new String[] {"smtp_settings","host","piet"}));
    }
}