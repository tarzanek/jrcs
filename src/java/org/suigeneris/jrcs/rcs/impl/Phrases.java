/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: Phrases.java,v 1.1 2006/06/08 15:07:11 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs.impl;

import java.util.Iterator;
import java.util.TreeMap;

import org.suigeneris.jrcs.rcs.Archive;

/**
 * A set of "new phrases" for an Archive. Phrases are keyed lists of symbols. An
 * Archive stores the keys it doesn't recognizes in a Phrases set to preserve
 * them. Unrecognized keys probably belong to archive extensions.
 * 
 * @see Archive
 * 
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @version $Id: Phrases.java,v 1.1 2006/06/08 15:07:11 juanca Exp $
 */
public class Phrases extends TreeMap
{
    public void toString(StringBuffer s, String EOL)
    {
        Iterator i = keySet().iterator();
        while (i.hasNext())
        {
            String key = i.next().toString();
            String value = get(key).toString();
            s.append(key.toString());
            s.append(" ");
            s.append(value);
            s.append(EOL);
        }
    }

    public String toString()
    {
        StringBuffer s = new StringBuffer();
        toString(s, Archive.RCS_NEWLINE);
        return s.toString();
    }
}
