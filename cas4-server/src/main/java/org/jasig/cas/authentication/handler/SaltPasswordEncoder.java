package org.jasig.cas.authentication.handler;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("saltPasswordEncoder")
public final class SaltPasswordEncoder
        implements PasswordEncoder
{
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    private static final int HEX_RIGHT_SHIFT_COEFFICIENT = 4;
    private static final int HEX_HIGH_BITS_BITWISE_FLAG = 15;
    private static final Logger LOGGER = LoggerFactory.getLogger(SaltPasswordEncoder.class);
    private final String encodingAlgorithm;
    private String characterEncoding;
    private String encodeSalt;

    @Autowired
    public void setCharacterEncoding(@Value("${cas.authn.password.encoding.char:}") String characterEncoding){
        this.characterEncoding = characterEncoding;
    }
    @Autowired
    public void setEncodeSalt(@Value("${cas.authn.password.encoding.salt:}") String encodeSalt){
        this.encodeSalt = encodeSalt;
    }

    @Autowired
    public SaltPasswordEncoder(@Value("${cas.authn.password.encoding.alg:}") String encodingAlgorithm)
    {
        this.encodingAlgorithm = encodingAlgorithm;
    }

    public String encode(String password)
    {
        if (password == null) {
            return null;
        }
        if (StringUtils.isBlank(this.encodingAlgorithm))
        {
            LOGGER.warn("No encoding algorithm is defined. Password cannot be encoded; Returning null");
            return null;
        }
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance(this.encodingAlgorithm);

            String encodingCharToUse = StringUtils.isNotBlank(this.characterEncoding) ?
                    this.characterEncoding : Charset.defaultCharset().name();

            LOGGER.warn("Using {} as the character encoding algorithm to update the digest", encodingCharToUse);
            messageDigest.update(encodeSalt.getBytes(encodingCharToUse));
            messageDigest.update(password.getBytes(encodingCharToUse));

            byte[] digest = messageDigest.digest();

            return getFormattedText(digest);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new SecurityException(e);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes)
    {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (int j = 0; j < bytes.length; j++)
        {
            buf.append(HEX_DIGITS[(bytes[j] >> 4 & 0xF)]);
            buf.append(HEX_DIGITS[(bytes[j] & 0xF)]);
        }
        return buf.toString();
    }
}
