/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: KeywordsFormatTest.java,v 1.6 2006/06/08 15:07:11 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs;

import java.util.Date;

import org.suigeneris.jrcs.rcs.text.KeywordAndValueFormatter;

import junit.framework.TestCase;

/**
 * Basic test for the formatter.
 * 
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 */
public class KeywordsFormatTest extends TestCase
{
    static public final KeywordAndValueFormatter FORMATTER = new KeywordAndValueFormatter();

    private static final String RCS_KEYWORDS = "$Id: KeywordsFormatTest.java,v 1.6 2006/06/08 15:07:11 juanca Exp $\n"
            + "$Header: /var/lib/cvs//jrcs/src/test/org/suigeneris/jrcs/rcs/KeywordsFormatTest.java,v 1.6 2006/06/08 15:07:11 juanca Exp $\n"
            + "$Source: /var/lib/cvs//jrcs/src/test/org/suigeneris/jrcs/rcs/KeywordsFormatTest.java,v $\n"
            + "$RCSfile: KeywordsFormatTest.java,v $\n" + "$Revision: 1.6 $\n" + "$Date: 2006/06/08 15:07:11 $\n"
            + "$Author: juanca $\n" + "$State: Exp $\n" + "$Locker:  $\n";

    // don't get bitten by rcs keywords it should not be interpreted
    private static final String RCS_CLEAN_KEYWORDS = "$" + "Id$\n" + "$" + "Header$\n" + "$" + "Source$\n" + "$" + "RCSfile$\n"
            + "$" + "Revision$\n" + "$" + "Date$\n" + "$" + "Author$\n" + "$" + "State$\n" + "$" + "Locker$\n";

    static public final Object[] REVISION_INFO = new Object[]
    { "a/b/c/d/File.ext", "File.ext", "1.1", new Date(), "theauthor", "thestate", "thelocker" };

    private static final String RCS_NOW = 
        FORMATTER.getIdFormat().format(REVISION_INFO) + "\n"
            + FORMATTER.getHeaderFormat().format(REVISION_INFO) + "\n" 
            + FORMATTER.getSourceFormat().format(REVISION_INFO) + "\n"
            + FORMATTER.getRCSFileFormat().format(REVISION_INFO) + "\n" 
            + FORMATTER.getRevisionFormat().format(REVISION_INFO) + "\n"
            + FORMATTER.getDateFormat().format(REVISION_INFO) + "\n" 
            + FORMATTER.getAuthorFormat().format(REVISION_INFO) + "\n"
            + FORMATTER.getStateFormat().format(REVISION_INFO) + "\n" 
            + FORMATTER.getLockerFormat().format(REVISION_INFO) + "\n";

    public KeywordsFormatTest(String s)
    {
        super(s);
    }

    public void testReset() throws Exception
    {
        String result = FORMATTER.reset(RCS_KEYWORDS);
        assertEquals(RCS_CLEAN_KEYWORDS, result);
    }

    public void testUpdate() throws Exception
    {
        String result = FORMATTER.update(RCS_KEYWORDS, REVISION_INFO);
        assertEquals(RCS_NOW, result);
    }
}
