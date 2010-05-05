/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: ValueOnlyFormatter.java,v 1.1 2006/06/08 15:07:10 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs.text;

import java.text.Format;
import java.text.MessageFormat;

/**
 * Formatter for the RCS keywords in "value only" mode.
 * 
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 * @author <a href="mailto:juanca@suigeneris.org">Juancarlo Anez</a>
 */
public class ValueOnlyFormatter extends KeywordFormatter
{
    private final Format Header_FORMAT = new MessageFormat("{1} {2} {3, date,yyyy/MM/dd HH:mm:ss} {4} {5}");
    private final Format Id_FORMAT = new MessageFormat("{1} {2} {3, date,yyyy/MM/dd HH:mm:ss} {4} {5}");
    private final Format RCSFile_FORMAT = new MessageFormat("{1}");
    private final Format Revision_FORMAT = new MessageFormat("{2}");
    private final Format Date_FORMAT = new MessageFormat("{3, date,yyyy/MM/dd HH:mm:ss}");
    private final Format Author_FORMAT = new MessageFormat("{4}");
    private final Format State_FORMAT = new MessageFormat("{5}");
    private final Format Locker_FORMAT = new MessageFormat("{6}");
    private final Format Source_FORMAT = new MessageFormat("{0}");

    public ValueOnlyFormatter()
    {
    }

    public Format getAuthorFormat()
    {
        return Author_FORMAT;
    }

    public Format getDateFormat()
    {
        return Date_FORMAT;
    }

    public Format getHeaderFormat()
    {
        return Header_FORMAT;
    }

    public Format getIdFormat()
    {
        return Id_FORMAT;
    }

    public Format getLockerFormat()
    {
        return Locker_FORMAT;
    }

    public Format getRCSFileFormat()
    {
        return RCSFile_FORMAT;
    }

    public Format getRevisionFormat()
    {
        return Revision_FORMAT;
    }

    public Format getSourceFormat()
    {
        return Source_FORMAT;
    }

    public Format getStateFormat()
    {
        return State_FORMAT;
    }
}
