/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: KeywordOnlyFormatter.java,v 1.1 2006/06/08 15:07:10 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs.text;

/**
 * Formatter for the RCS keywords in "keyword only" mode.
 * 
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 */
public class KeywordOnlyFormatter extends KeywordFormatter
{

    public KeywordOnlyFormatter()
    {
    }

    /**
     * update the given text made of RCS keywords with the appropriate revision
     * info.
     * 
     * @param text
     *            the input text containing the RCS keywords.
     * @param revisionInfo
     *            the revision information.
     * @return the formatted text with the RCS keywords.
     */
    public String update(String text, Object[] revisionInfo)
    {
        return reset(text);
    }
}
