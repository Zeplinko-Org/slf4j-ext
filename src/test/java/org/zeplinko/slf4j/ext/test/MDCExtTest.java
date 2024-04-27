/*
   Copyright 2024 Zeplinko

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package org.zeplinko.slf4j.ext.test;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.zeplinko.slf4j.ext.MDCExt;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MDCExtTest {

    public static final String KEY_USER_ID = "userId";

    public static final String KEY_SESSION_ID = "sessionId";

    public static final String VALUE_USER_ID = "12345";

    public static final String VALUE_SESSION_ID = "abc123";

    @Test
    void testPutCloseableWithSingleEntry() {
        try (MDCExt.MDCExtCloseable closeable = MDCExt.putCloseable(KEY_USER_ID, VALUE_USER_ID)) {
            assertEquals(VALUE_USER_ID, MDC.get(KEY_USER_ID));
        }
        assertNull(MDC.get(KEY_USER_ID));
    }

    @Test
    void testPutCloseableWithMultipleEntries() {
        try (
                MDCExt.MDCExtCloseable closeable = MDCExt.putCloseable(
                        Map.entry(KEY_USER_ID, VALUE_USER_ID),
                        Map.entry(KEY_SESSION_ID, VALUE_SESSION_ID)
                )
        ) {
            assertEquals(VALUE_USER_ID, MDC.get(KEY_USER_ID));
            assertEquals(VALUE_SESSION_ID, MDC.get(KEY_SESSION_ID));
        }
        assertNull(MDC.get(KEY_USER_ID));
        assertNull(MDC.get(KEY_SESSION_ID));
    }

    @Test
    void testPutCloseableWithMap() {
        Map<String, String> entries = Map.of(KEY_USER_ID, VALUE_USER_ID, KEY_SESSION_ID, VALUE_SESSION_ID);
        try (MDCExt.MDCExtCloseable closeable = MDCExt.putCloseable(entries)) {
            assertEquals(VALUE_USER_ID, MDC.get(KEY_USER_ID));
            assertEquals(VALUE_SESSION_ID, MDC.get(KEY_SESSION_ID));
        }
        assertNull(MDC.get(KEY_USER_ID));
        assertNull(MDC.get(KEY_SESSION_ID));
    }
}
