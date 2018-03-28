//
//  ========================================================================
//  Copyright (c) 1995-2018 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.util.ajax;

import java.io.IOException;

@SuppressWarnings("Duplicates")
public class JSONEscapedStringEncoder implements JSON.StringEncoder
{
    @Override
    public void encode(Appendable appendable, String string)
    {
        if (string == null)
            return;

        try
        {
            appendable.append('"');
            if (string.length() > 0)
            {
                escape(appendable, string);
            }
            appendable.append('"');
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Escape following the <a href="https://tools.ietf.org/html/rfc8259#section-7">RFC 8259: Section 7</a> rules.
     *
     * @param buffer the buffer to populate
     * @param input the input string to escape
     */
    private void escape(Appendable buffer, String input) throws IOException
    {
        for (int i = 0; i < input.length(); ++i)
        {
            char c = input.charAt(i);

            // ASCII printable range
            if ((c >= 0x20) && (c <= 0x7E))
            {
                // Special cases for quotation-mark, reverse-solidus, and solidus
                if ((c == '"') || (c == '\\') || (c == '/'))
                {
                    buffer.append('\\').append(c);
                }
                else
                {
                    // ASCII printable (that isn't escaped above)
                    buffer.append(c);
                }
            }
            else
            {
                // All other characters are escaped (in some way)

                // First we deal with the special short-form escaping
                if (c == '\b') // backspace
                    buffer.append("\\b");
                else if (c == '\f') // form-feed
                    buffer.append("\\f");
                else if (c == '\n') // line feed
                    buffer.append("\\n");
                else if (c == '\r') // carriage return
                    buffer.append("\\r");
                else if (c == '\t') // tab
                    buffer.append("\\t");
                else
                {
                    // Everything else is slash-u escaped
                    buffer.append("\\u").append(String.format("%04x", (short)c));
                }
            }
        }
    }
}
