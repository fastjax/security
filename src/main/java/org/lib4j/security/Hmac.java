/* Copyright (c) 2018 FastJAX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.lib4j.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public enum Hmac {
  HmacSHA1("HmacSHA1"),
  HmacSHA256("HmacSHA256"),
  HmacSHA512("HmacSHA512");

  private final ThreadLocal<Mac> mac;

  private Hmac(final String algorithm) {
    this.mac = new ThreadLocal<>() {
      @Override
      protected Mac initialValue() {
        try {
          return Mac.getInstance(algorithm);
        }
        catch (final NoSuchAlgorithmException e) {
          throw new UnsupportedOperationException(e);
        }
      }
    };
  }

  /**
   * Generate the Hashed Message Authentication Code for the given {@code key}
   * and {@code data}.
   * <p>
   * This method uses JCE to provide the crypto algorithm.
   *
   * @param key The HMAC key.
   * @param data The text to be authenticated.
   * @return The Hashed Message Authentication Code.
   * @throws IllegalArgumentException If {@code key} is invalid.
   */
  public byte[] generate(final byte[] key, final byte[] data) {
    try {
      final SecretKeySpec secretKey = new SecretKeySpec(key, "RAW");
      mac.get().init(secretKey);
      return mac.get().doFinal(data);
    }
    catch (final InvalidKeyException e) {
      throw new IllegalArgumentException(e);
    }
  }
}