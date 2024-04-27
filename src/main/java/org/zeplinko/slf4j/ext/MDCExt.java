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
package org.zeplinko.slf4j.ext;

import org.slf4j.MDC;

import java.util.Map;
import java.util.Set;

/**
 * Utility class providing enhanced MDC (Mapped Diagnostic Context)
 * functionality. This class facilitates the management of MDC data by providing
 * methods to handle context data more efficiently and safely within a
 * try-with-resources block.
 */
public final class MDCExt {

    private MDCExt() {
        // Private constructor to prevent instantiation
    }

    /**
     * Places a key-value pair in the MDC, returning a {@link MDCExtCloseable} that
     * removes the entry when closed.
     *
     * @param key   the key to store in the MDC
     * @param value the value associated with the key
     * @return a closeable object that, when closed, will remove the key from the
     *         MDC
     */
    public static MDCExtCloseable putCloseable(String key, String value) {
        MDC.put(key, value);
        return new MDCExtCloseable(key);
    }

    /**
     * Places multiple key-value pairs in the MDC from an array of Map entries,
     * returning a {@link MDCExtCloseable} that removes the entries when closed.
     *
     * @param entries varargs parameter of Map entries to add to the MDC
     * @return a closeable object that, when closed, will remove the added entries
     *         from the MDC
     */
    @SafeVarargs
    public static MDCExtCloseable putCloseable(Map.Entry<String, String>... entries) {
        return putCloseable(Map.ofEntries(entries));
    }

    /**
     * Places multiple key-value pairs in the MDC from a Map, returning a
     * {@link MDCExtCloseable} that removes the entries when closed.
     *
     * @param entryMap a map containing the entries to add to the MDC
     * @return a closeable object that, when closed, will remove the added entries
     *         from the MDC
     */
    public static MDCExtCloseable putCloseable(Map<String, String> entryMap) {
        entryMap.forEach(MDC::put);
        return new MDCExtCloseable(entryMap.keySet());
    }

    /**
     * AutoCloseable implementation to support removing MDC entries easily using
     * try-with-resources block.
     */
    public static class MDCExtCloseable implements AutoCloseable {
        private final Set<String> keys;

        /**
         * Creates an instance to manage a single key.
         *
         * @param key the key to manage in the MDC
         */
        public MDCExtCloseable(String key) {
            this(Set.of(key));
        }

        /**
         * Creates an instance to manage multiple keys.
         *
         * @param keys the keys to manage in the MDC
         */
        public MDCExtCloseable(Set<String> keys) {
            this.keys = keys;
        }

        /**
         * Removes all managed keys from the MDC when the close method is called.
         */
        @Override
        public void close() {
            this.keys.forEach(MDC::remove);
        }
    }
}
