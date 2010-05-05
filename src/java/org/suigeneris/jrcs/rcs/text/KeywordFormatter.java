/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
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

import java.util.regex.*;

/**
 * Generic formatter for the RCS keywords. It is intended as an helper class to
 * replace the use of gnu.regexp. This class is NOT threadsafe.
 * 
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 * @author <a href="mailto:juanca@suigeneris.org">Juancarlo Anez</a>
 */
public abstract class KeywordFormatter
{
    private static final String DOLLAR = "\\\\\\$";

    private final Format Header_FORMAT = new MessageFormat("$"
            + "Header: {1} {2} {3, date,yyyy/MM/dd HH:mm:ss} {4} {5} " + "$");
    private final Format Id_FORMAT = new MessageFormat("$"
            + "Id: {1} {2} {3, date,yyyy/MM/dd HH:mm:ss} {4} {5} " + "$");
    private final Format RCSFile_FORMAT = new MessageFormat("$"
            + "RCSfile: {1} " + "$");
    private final Format Revision_FORMAT = new MessageFormat("$"
            + "Revision: {2} " + "$");
    private final Format Date_FORMAT = new MessageFormat("$"
            + "Date: {3, date,yyyy/MM/dd HH:mm:ss} " + "$");
    private final Format Author_FORMAT = new MessageFormat("$" + "Author: {4} "
            + "$");
    private final Format State_FORMAT = new MessageFormat("$" + "State: {5} "
            + "$");
    private final Format Locker_FORMAT = new MessageFormat("$" + "Locker: {6} "
            + "$");
    private final Format Source_FORMAT = new MessageFormat("$" + "Source: {0} "
            + "$");

    protected final Pattern ID_RE;
    protected final Pattern HEADER_RE;
    protected final Pattern SOURCE_RE;
    protected final Pattern RCSFILE_RE;
    protected final Pattern REVISION_RE;
    protected final Pattern DATE_RE;
    protected final Pattern AUTHOR_RE;
    protected final Pattern STATE_RE;
    protected final Pattern LOCKER_RE;
    protected final Pattern NAME_RE;
    protected final Pattern LOG_RE;

    private static final KeywordFormatter V_FORMATTER = new ValueOnlyFormatter();
    private static final KeywordFormatter K_FORMATTER = new KeywordOnlyFormatter();
    // synchronize this if this has to be used in MT !
    private static final KeywordFormatter KV_FORMATTER = new KeywordAndValueFormatter();

    public KeywordFormatter()
    {
        ID_RE = Pattern.compile("\\$Id(:[^\\$]*)?\\$");
        HEADER_RE = Pattern.compile("\\$Header(:[^\\$]*)?\\$");
        SOURCE_RE = Pattern.compile("\\$Source(:[^\\$]*)?\\$");
        RCSFILE_RE = Pattern.compile("\\$RCSfile(:[^\\$]*)?\\$");
        REVISION_RE = Pattern.compile("\\$Revision(:[^\\$]*)?\\$");
        DATE_RE = Pattern.compile("\\$Date(:[^\\$]*)?\\$");
        AUTHOR_RE = Pattern.compile("\\$Author(:[^\\$]*)?\\$");
        STATE_RE = Pattern.compile("\\$State(:[^\\$]*)?\\$");
        LOCKER_RE = Pattern.compile("\\$Locker(:[^\\$]*)?\\$");
        NAME_RE = Pattern.compile("\\$Name(:[^\\$]*)?\\$");
        LOG_RE = Pattern.compile("\\$Log(:[^\\$]*)?\\$");
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
        String result = new String(text);
        result = substitute(result, ID_RE, Id_FORMAT.format(revisionInfo));
        result = substitute(result, HEADER_RE, Header_FORMAT
                .format(revisionInfo));
        result = substitute(result, SOURCE_RE, Source_FORMAT
                .format(revisionInfo));
        result = substitute(result, RCSFILE_RE, RCSFile_FORMAT
                .format(revisionInfo));
        result = substitute(result, REVISION_RE, Revision_FORMAT
                .format(revisionInfo));
        result = substitute(result, DATE_RE, Date_FORMAT.format(revisionInfo));
        result = substitute(result, AUTHOR_RE, Author_FORMAT
                .format(revisionInfo));
        result = substitute(result, STATE_RE, State_FORMAT.format(revisionInfo));
        result = substitute(result, LOCKER_RE, Locker_FORMAT
                .format(revisionInfo));
        // @TODO: should do something about Name and Log
        return result;
    }

    /**
     * Reinitialize all RCS keywords match.
     * 
     * @param text
     *            the text to look for RCS keywords.
     * @return the text with initialized RCS keywords.
     */
    public String reset(String text)
    {
        // WARNING: Do not remove the string concatenations
        // or CVS will mangle the strings on check in/out.
        String data = text;
        data = substitute(data, ID_RE, "$" + "Id" + "$");
        data = substitute(data, HEADER_RE, "$" + "Header" + "$");
        data = substitute(data, SOURCE_RE, "$" + "Source" + "$");
        data = substitute(data, RCSFILE_RE, "$" + "RCSfile" + "$");
        data = substitute(data, REVISION_RE, "$" + "Revision" + "$");
        data = substitute(data, DATE_RE, "$" + "Date" + "$");
        data = substitute(data, AUTHOR_RE, "$" + "Author" + "$");
        data = substitute(data, STATE_RE, "$" + "State" + "$");
        data = substitute(data, LOCKER_RE, "$" + "Locker" + "$");
        data = substitute(data, NAME_RE, "$" + "Name" + "$");
        data = substitute(data, LOG_RE, "$" + "Log" + "$");
        return data;
    }

    /**
     * Remove all RCS keywords match.
     * 
     * @param text
     *            the text to look for RCS keywords.
     * @return the text with removed RCS keywords.
     */
    protected String remove(String text)
    {
        // WARNING: Do not remove the string concatenations
        // or CVS will mangle the strings on check in/out.
        String data = text;
        data = substitute(data, ID_RE, "");
        data = substitute(data, HEADER_RE, "");
        data = substitute(data, SOURCE_RE, "");
        data = substitute(data, RCSFILE_RE, "");
        data = substitute(data, REVISION_RE, "");
        data = substitute(data, DATE_RE, "");
        data = substitute(data, AUTHOR_RE, "");
        data = substitute(data, STATE_RE, "");
        data = substitute(data, LOCKER_RE, "");
        data = substitute(data, NAME_RE, "");
        data = substitute(data, LOG_RE, "");
        return data;
    }

    /**
     * Helper method for substitution that will substitute all matches of a
     * given pattern.
     * 
     * @param input
     *            the text to look for substitutions.
     * @param pattern
     *            the pattern to replace in the input text.
     * @param substitution
     *            the string to use as a replacement for the pattern.
     * @return the text with the subsituted value.
     */
    private final String substitute(String input, Pattern pattern,
            String substitution)
    {
        try
        {
            Matcher m = pattern.matcher(input);
            substitution = substitution.replaceAll("\\$", DOLLAR);
            return m.replaceAll(substitution);
        }
        catch (Throwable t)
        {
            throw new RuntimeException(t);
        }
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

    public static KeywordFormatter getKeywordOnlyFormatter()
    {
        return K_FORMATTER;
    }

    public static KeywordFormatter getKeywordAndValueFormatter()
    {
        return KV_FORMATTER;
    }

    public static KeywordFormatter getValueOnlyFormatter()
    {
        return V_FORMATTER;
    }

}
