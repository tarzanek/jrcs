/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: ToString.java,v 1.3 2006/06/08 01:22:27 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
 * This class delegates handling of the to a StringBuffer based version.
 * 
 * @version $Revision: 1.3 $ $Date: 2006/06/08 01:22:27 $
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 */
public class ToString
{
    public ToString()
    {
    }

    /**
     * Default implementation of the
     * {@link java.lang.Object#toString toString() } method that delegates work
     * to a {@link java.lang.StringBuffer StringBuffer} base version.
     */
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        toString(s);
        return s.toString();
    }

    /**
     * Place a string image of the object in a StringBuffer.
     * 
     * @param s
     *            the string buffer.
     */
    public void toString(StringBuffer s)
    {
        s.append(super.toString());
    }

    /**
     * Breaks a string into an array of strings. Use the value of the
     * <code>line.separator</code> system property as the linebreak character.
     * 
     * @param value
     *            the string to convert.
     */
    public static String[] stringToArray(String value)
    {
        BufferedReader reader = new BufferedReader(new StringReader(value));
        List l = new LinkedList();
        String s;
        try
        {
            while ((s = reader.readLine()) != null)
            {
                l.add(s);
            }
        }
        catch (java.io.IOException e)
        {
        }
        return (String[]) l.toArray(new String[l.size()]);
    }

    /**
     * Converts an array of {@link Object Object} to a string Use the value of
     * the <code>line.separator</code> system property the line separator.
     * 
     * @param o
     *            the array of objects.
     */
    public static String arrayToString(Object[] o)
    {
        return arrayToString(o, System.getProperty("line.separator"));
    }

    /**
     * Converts an array of {@link Object Object} to a string using the given
     * line separator.
     * 
     * @param o
     *            the array of objects.
     * @param eol
     *            the string to use as line separator.
     */
    public static String arrayToString(Object[] o, String eol)
    {
        StringBuffer buf = new StringBuffer();
        if (o.length > 0)
        {
            for (int i = 0; i < o.length-1; i++)
            {
                buf.append(o[i]);
                buf.append(eol);
            }
            buf.append(o[o.length - 1]);
        }
        return buf.toString();
    }

    public static String toStringOfChars(String s)
    {
        StringBuffer buf = new StringBuffer();
        byte[] chars = s.getBytes();
        for (int i = 0; i < chars.length; i++)
        {
            buf.append('[');
            buf.append(chars[i]);
            buf.append(']');
        }
        return buf.toString();
    }

    public static String[] stringToArray(String text, String newline)
    {
        return text.split(newline, -1);
    }
}
