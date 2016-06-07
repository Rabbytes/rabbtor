/*
 * Copyright 2016 - Rabbytes Incorporated
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Rabbytes Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Rabbytes Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Rabbytes Incorporated.
 */
package com.rabbtor.gsp.util

import groovy.transform.CompileStatic
import org.grails.encoder.Encoder
import org.springframework.web.util.HtmlUtils
import org.springframework.web.util.WebUtils

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CompileStatic
class GspWebUtils
{
    public static String lookupEncoding(HttpServletResponse response, HttpServletRequest request) {
        String encoding = response ? response.getCharacterEncoding() : null
        if (encoding == null)
            encoding = request ? request.getCharacterEncoding() : null
        if (encoding)
            encoding = WebUtils.DEFAULT_CHARACTER_ENCODING
        return encoding
    }

    public static String htmlEncode(Object object, Encoder encoder, String encoding) {
        if (object == null)
            return null
        if (encoder != null)
            return encoder.encode(object);
        if (encoding == null)
            return HtmlUtils.htmlEscape(String.valueOf(object))
        else
            return HtmlUtils.htmlEscape(String.valueOf(object),encoding)
    }


}
