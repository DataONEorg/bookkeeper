package org.dataone.bookkeeper.resources;

import static org.junit.Assert.assertTrue;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

public class PaymentsResourceTest {
    @Test
    void testValidateHash() {
        String combined = "123456789012,e6f157d2-66cf-43d5-8a56-c4c57d5760d7,1360870400";
        String account = "123456789012";
        String api_key = "e6f157d2-66cf-43d5-8a56-c4c57d5760d7";
        String timestamp = "1360870400";
        String hash_reference = "b48171ba3c4ffbc1345093087d661d52a109d836462455d208f52bf7392cbf95";
        String sha256hex = DigestUtils.sha256Hex(combined);
        assertTrue("Raw hashes match", sha256hex.equals(hash_reference));
        boolean valid = PaymentsResource.validateHash(account, api_key, timestamp, hash_reference);
        assertTrue("Calculated hash matches reference hash", valid);
    }
}
