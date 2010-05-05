/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: Archive.java,v 1.7 2007-07-27 17:19:47 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.suigeneris.jrcs.diff.Diff;
import org.suigeneris.jrcs.diff.DiffException;
import org.suigeneris.jrcs.diff.DifferentiationFailedException;
import org.suigeneris.jrcs.diff.PatchFailedException;
import org.suigeneris.jrcs.rcs.impl.BranchNode;
import org.suigeneris.jrcs.rcs.impl.Line;
import org.suigeneris.jrcs.rcs.impl.Lines;
import org.suigeneris.jrcs.rcs.impl.Node;
import org.suigeneris.jrcs.rcs.impl.NodeNotFoundException;
import org.suigeneris.jrcs.rcs.impl.Path;
import org.suigeneris.jrcs.rcs.impl.Phrases;
import org.suigeneris.jrcs.rcs.impl.TrunkNode;
import org.suigeneris.jrcs.rcs.parse.ArchiveParser;
import org.suigeneris.jrcs.rcs.parse.FastCharStream;
import org.suigeneris.jrcs.rcs.parse.ParseException;
import org.suigeneris.jrcs.rcs.text.KeywordFormatter;
import org.suigeneris.jrcs.util.ToString;

/**
 * Handling of RCS/CVS style version control archives.
 * 
 * 
 * <p>
 * JRCS is a library that knows how to manipulate the archive files produced by
 * the RCS and CVS version control systems. JRCS is not intended to replace
 * neither tool. JRCS was written to be able create archive analysis tools that
 * can do things like identify hot spots in the source code, measure the
 * contributions by each developer, or assess how bugs make it in.
 * </p>
 * 
 * <p>
 * The reasons why JRCS has the ability do do check-ins and save archives is API
 * symmetry, and to simplify the writing of unit tests.
 * </p>
 * 
 * <p>
 * <b>CAVEAT UTILITOR:</b> Do not make modifications to your archives with
 * JRCS. There needs to be an important amount of additional testing before it's
 * safe to do that.
 * </p>
 * 
 * <p>
 * The {@link org.suigeneris.jrcs.rcs rcs} package implements the archive
 * handling functionality. The entry point to the library is class
 * {@link org.suigeneris.jrcs.rcs.Archive Archive}.
 * </p>
 * 
 * 
 * <p>
 * The {@link org.suigeneris.jrcs.diff diff} package implements the differencing
 * engine that JRCS uses. The engine has the power of Unix diff, is simple to
 * understand, and can be used independently of the archive handling
 * functionality. The entry point to the differencing engine is class
 * {@link org.suigeneris.jrcs.diff.Diff Diff}.
 * </p>
 * 
 * <p>
 * Within this library, the word <i>text</i> means a unit of information
 * subject to version control. The word <i>revision</i> means a particular
 * version of a text. Each <i>revision</i> has a <i>version number</i>
 * associated to it. <i>Version numbers</i> are dot-separated lists of numbers.
 * Version numbers with an odd number of dots indicate revisions, while those
 * with an even number of dots (including zero dots) designate branches.
 * </p>
 * 
 * <p>
 * Revisions of a text are represented as <code>Object[]</code> because the
 * diff engine is capable of handling more than plain text. In fact, arrays of
 * any type that implements {@link java.lang.Object#hashCode hashCode()} and
 * {@link java.lang.Object#equals equals()} correctly can be subject to
 * differencing and version control using this library.
 * </p>
 * 
 * <p>
 * To create an empty archive use: <code><pre>
 * Archive archive = new Archive();
 * </pre></code>
 * </p>
 * 
 * <p>
 * To read an archive from the file system, use: <code><pre>
 * Archive archive = new Archive(&quot;/path/to/archive,v&quot;);
 * </pre></code>
 * </p>
 * 
 * <p>
 * You can also initialize archives from streams.
 * </p>
 * 
 * <p>
 * To retreive a revision from an archive use: <code><pre>
 * String versionNumber = &quot;1.2&quot;;
 * Object[] text = archive.getRevision(versionNumber);
 * </pre></code>
 * </p>
 * 
 * <p>
 * You can also retreive revisions in such a way that each item is annotated
 * with the version number of the revision in which it was last changed or
 * added. To retrieve annotated text use: <code><pre>
 *                                                      String versionNumber = &quot;1.2&quot;;
 *                                                      {@link Line Line[]} text = archive.getRevision(versionNumber);
 *                                                      for(int i = 0; i &lt; text.length(); i++)
 *                                                          System.out.println(text[i].revision.version);
 * </pre></code>
 * </p>
 * 
 * <p>
 * This class is NOT thread safe.
 * </p>
 * 
 * @see org.suigeneris.jrcs.diff
 * 
 * @version $Id: Archive.java,v 1.7 2007-07-27 17:19:47 juanca Exp $
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 */
public class Archive extends ToString
{
    /*
     * RCS/CVS keyword expansion modes.
     */
    public static final String EXP_KeywordAndValue = "kv"; // (Default)
    public static final String EXP_KeywordValueAndLocker = "kvl"; // TODO:
    // support
    // this one
    public static final String EXP_KeywordOnly = "k"; // 
    public static final String EXP_PreserveOriginal = "o";
    public static final String EXP_Binary = "b";
    public static final String EXP_ValueOnly = "v";

    public static final char RCS_NEWLINE_CHAR = '\n';
    public static final String RCS_NEWLINE = Character
            .toString(RCS_NEWLINE_CHAR);

    protected TrunkNode head;
    protected Version branch;

    /*
     * !!! check Node.compareTo for correct RCS order
     */
    protected Map nodes = new TreeMap();
    protected Set users = new TreeSet();
    protected Set locked = new TreeSet();
    protected Map symbols = new TreeMap();
    protected Phrases phrases = new Phrases();
    protected String desc = new String();
    protected boolean strictLocking = true;
    protected String expand;
    protected String comment = "# ";
    protected String filename = "__unknown__,v";

    /**
     * Creates a new archive and sets the text of the initial revision.
     * 
     * @param text
     *            The text of the initial revision.
     * @param desc
     *            The archives description (not the log message).
     */
    public Archive(Object[] text, String desc)
    {
        this(text, desc, new Version(1, 1));
    }

    /**
     * Creates a new archive with the specified initial version number and sets
     * the text of the initial revision. The initial revision must be of the
     * form "n.m" (i.e. a trunk revision).
     * 
     * @param text
     *            The text of the initial revision.
     * @param desc
     *            The archives description (not the log message).
     * @param vernum
     *            The initial revision number.
     */
    public Archive(Object[] text, String desc, String vernum)
    {
        this(text, desc, new Version(vernum));
    }

    /**
     * Creates a new archive with the specified initial version number and sets
     * the text of the initial revision. The initial revision must be of the
     * form "n.m" (i.e. a trunk revision).
     * 
     * @param text
     *            The text of the initial revision.
     * @param desc
     *            The archives description (not the log message).
     * @param vernum
     *            The initial revision number.
     */
    public Archive(Object[] text, String desc, Version vernum)
    {
        // can only add a trunk version
        if (vernum.size() > 2)
        {
            throw new InvalidVersionNumberException(vernum
                    + " must be a trunk version");
        }
        while (vernum.size() < 2)
        {
            vernum = vernum.newBranch(1);
        }
        // now add the _head node
        this.head = (TrunkNode) newNode(vernum, null);
        this.head.setText(text);
        this.head.setLog(desc);
    }

    /**
     * Load an archive from an Reader. Parses the archive given by the
     * input stream, and gives it the provided name.
     * 
     * @param fname
     *            The name to give to the archive.
     * @param reader
     *            Where to read the archive from
     */
    public Archive(String fname, Reader reader) throws ParseException
    {
        this.filename = fname;
        ArchiveParser.load(this, new FastCharStream(reader));
    }

    /**
     * Load an archive from an input stream. Parses the archive given by the
     * input stream, and gives it the provided name.
     * 
     * @param fname
     *            The name to give to the archive.
     * @param input
     *            Where to read the archive from
     */
    public Archive(String fname, InputStream input) throws ParseException
    {
        this(fname, new InputStreamReader(input));
    }

    /**
     * Load an archive from an a file given by name.
     * 
     * @param path
     *            The path to the file wher the archive resides.
     */
    public Archive(String path) throws ParseException, FileNotFoundException
    {
        this.filename = new File(path).getPath();
        ArchiveParser.load(this, this.filename);
    }

    /**
     * Load an archive from an a file given by name.
     * 
     * @param path
     *            The path to the file wher the archive resides.
     */
    public Archive(URL url) throws ParseException, IOException
    {
        this(url.getFile(), url.openStream());
    }

    /**
     * Create an unitialized Archive. Used internally by the ArchiveParser.
     * 
     * @see ArchiveParser
     */
    Archive()
    {
    }

    /**
     * Retrieves a {@link Version} object from a numeric version string or a
     * symbolic name. Symbolic names must have been previously defined using
     * {@link #addSymbol(String, String) addSymbol}.
     * 
     * @param vernum
     *            a numeric version identifier or a symbolic name.
     * @return the {@link Version} object
     * @throws InvalidVersionNumberException
     */
    public Version version(String vernum) throws InvalidVersionNumberException
    {
        if (vernum.length() > 0 && !Character.isDigit(vernum.charAt(0)))
        {
            return (Version) symbols.get(vernum);
        }
        return new Version(vernum);
    }

    /**
     * Set the name of the file for this archive
     * 
     * @param path
     *            The full path name.
     */
    public void setFileName(String path)
    {
        this.filename = path;
    }

    /**
     * Save the archive to the provided stream.
     * 
     * @param output
     *            The stream to save the archive to.
     */
    public void save(OutputStream output) throws IOException
    {
        output.write(toByteArray());
    }

    /**
     * Save the archive to a file and the the Archives filename accordingly.
     * 
     * @param path
     *            The file's path.
     */
    public void save(String path) throws IOException
    {
        OutputStream output = new FileOutputStream(path);
        try
        {
            save(output);
            this.filename = new File(path).getPath();
        }
        finally
        {
            output.close();
        }
    }

    /**
     * Add a head node with the given version number.
     * 
     * @param vernum
     *            The version number to use.
     */
    protected void setHead(Version vernum) throws InvalidVersionNumberException
    {
        if (head != null)
        {
            throw new HeadAlreadySetException(head.getVersion());
        }
        head = new TrunkNode(vernum, null);
        nodes.put(vernum, head);
    }

    /**
     * Set the active branch to the one identified by the given version number.
     * Incomplete version numbers of the form "1" or "2.1.3" are accepted.
     * 
     * @param v
     *            The version number.
     */
    public void setBranch(String v) throws InvalidBranchVersionNumberException
    {
        if (v == null)
            branch = null;
        else
            setBranch(version(v));
    }

    /**
     * Set the active branch to the one identified by the given version number.
     * 
     * @param vernum
     *            The version number.
     */
    public void setBranch(Version vernum)
            throws InvalidBranchVersionNumberException
    {
        if (!vernum.isBranch())
        {
            throw new InvalidBranchVersionNumberException(vernum);
        }
        if (head == null || vernum.getBase(2).isGreaterThan(head.getVersion()))
        {
            throw new InvalidBranchVersionNumberException(vernum
                    + "is greater than _head version " + head.getVersion());
        }
        branch = vernum;
    }

    /**
     * Add a user name to the list of archive users.
     * 
     * @param name
     *            The user name.
     */
    public void addUser(String name)
    {
        users.add(name);
    }

    public boolean isValidSymbol(String sym)
    {
        return sym != null && sym.matches("[A-Za-z_-][A-Za-z0-9_-]*");
    }

    /**
     * Tag a given version with a symbol.
     * 
     * @param sym
     *            The tag.
     * @param vernum
     *            The version to tag.
     */
    public void addSymbol(String sym, String vernum)
    {
        if (!isValidSymbol(sym))
            throw new IllegalArgumentException("invalid symbolic tag");
        addSymbol(sym, new Version(vernum));
    }

    /**
     * Tag a given version with a symbol.
     * 
     * @param sym
     *            The tag.
     * @param vernum
     *            The version to tag.
     */
    public void addSymbol(String sym, Version vernum)
            throws InvalidVersionNumberException
    {
        // @TODO: Verify if the symbol is valid
        symbols.put(sym, vernum);
    }

    /**
     * Returns a Map of the symbols (tags) associated with each revision. The
     * symbols are the keys and the revision numbers are the values.
     * 
     * @return A map of symbol/revision number pairs.
     */
    public Map getSymbols()
    {
        return symbols;
    }

    /**
     * Add a lock over a revison.
     * 
     * @param user
     *            The user that locks the revision.
     * @param vernum
     *            The version number of the revision to lock.
     */
    public void addLock(String user, Version vernum)
            throws InvalidVersionNumberException, NodeNotFoundException
    {
        addUser(user);
        Node node = newNode(vernum);
        node.setLocker(user);
        if (user == null)
        {
            locked.remove(node);
        }
        else
        {
            locked.add(node);
        }
    }

    /**
     * Set the strict locking flag for the archive.
     * 
     * @param value
     *            Indicates if strict locking should be on or off.
     */
    public void setStrictLocking(boolean value)
    {
        strictLocking = value;
    }

    /**
     * Set the keyword expansion flag for the archive.
     * 
     * @param value
     *            The keyword expansion value. It should be one of:
     *            <ul>
     *            <li> kv (Default) Substitue keyword and value.
     *            <li> kvl Substitute keyword, value, and locker (if any).
     *            <li> k Substitute keyword only.
     *            <li> o Preserve original string.
     *            <li> b Like o, but mark file as binary.
     *            <li> v Substitue value only.
     *            </ul>
     */
    public void setExpand(String value)
    {
        expand = value;
    }

    /**
     * Get the expansin flag.
     * 
     * @return the expansion flag value.
     * @see #setExpand
     */
    public String getExpand()
    {
        return expand;
    }

    /**
     * Determine if expansion is set to binary.
     * 
     * @return <code>true</code> if expansion is set to binary.
     */
    public boolean isBinary()
    {
        return EXP_Binary.equals(getExpand());
    }

    /**
     * Set the archive's comment.
     * 
     * @param value
     *            The comment.
     */
    public void setComment(String value)
    {
        comment = value;
    }

    /**
     * Set the archives description.
     * 
     * @param value
     *            The descriptions text.
     */
    public void setDesc(String value)
    {
        desc = value;
    }

    /**
     * Add a new phrase to the archive. Phrases are used to provide for
     * extensions of the archive format. Each phrase has a key and a list of
     * values associated with it.
     * 
     * @param key
     *            The phrases key.
     * @param values
     *            The values under the key.
     */
    public void addPhrase(String key, Collection values)
    {
        phrases.put(key, values);
    }

    protected Phrases getPhrases()
    {
        return this.phrases;
    }

    protected Node newNode(Version vernum)
    {
        return newNode(vernum, null);
    }

    protected Node newNode(Version vernum, Node prev)
            throws InvalidVersionNumberException, NodeNotFoundException
    {
        if (!vernum.isRevision())
        {
            throw new InvalidVersionNumberException(vernum);
        }
        Node node = (Node) nodes.get(vernum);
        if (node == null)
        {
            node = Node.newNode(vernum, prev);
            nodes.put(vernum, node);
        }
        return node;
    }

    protected TrunkNode newTrunkNode(Version vernum)
            throws InvalidVersionNumberException, NodeNotFoundException
    {
        if (!vernum.isTrunk())
        {
            throw new InvalidTrunkVersionNumberException(vernum);
        }
        return (TrunkNode) newNode(vernum);
    }

    protected BranchNode newBranchNode(Version vernum)
            throws InvalidVersionNumberException, NodeNotFoundException
    {
        if (!vernum.isBranch())
        {
            throw new InvalidBranchVersionNumberException(vernum);
        }
        return (BranchNode) newNode(vernum);
    }

    protected Node getNode(Version vernum)
            throws InvalidVersionNumberException, NodeNotFoundException
    {
        if (!vernum.isRevision())
        {
            throw new InvalidVersionNumberException(vernum);
        }
        Node node = (Node) nodes.get(vernum);
        if (node == null)
        {
            throw new NodeNotFoundException(vernum);
        }
        return node;
    }

    /**
     * Return the node with the version number that matches the one provided.
     * The given version number may be partial.
     * 
     * @param vernum
     *            the version number to match.
     * @return the node, or null if no match found.
     */
    public Node findNode(Version vernum)
    {
        Path path = getRevisionPath(vernum);
        return (path == null ? null : path.last());
    }

    /**
     * Place a string image of the archive in the given StringBuffer.
     * 
     * @param s
     *            Where the image shoul go.
     */
    public void toString(StringBuffer s)
    {
        toString(s, RCS_NEWLINE);
    }

    /**
     * Return a text image of the archive.
     * 
     * @param eol
     *            The token to use as line separator.
     * @return The text image of the archive.
     */
    public String toString(String eol)
    {
        StringBuffer s = new StringBuffer();
        toString(s, eol);
        return s.toString();
    }

    /**
     * Return a text image of the archive.
     * 
     * @param EOL
     *            The token to use as line separator.
     * @return The text image of the archive.
     */
    public String toString(char eol)
    {
        return toString(Character.toString(eol));
    }

    /**
     * Return a text image of the archive as a char array. This is useful for
     * writing the archive to a file without having the characters be
     * interpreted by the writer.
     * 
     * @return The archive image.
     */
    public char[] toCharArray()
    {
        return toString(Archive.RCS_NEWLINE).toCharArray();
    }

    /**
     * Return a text image of the archive as a char array. This is useful for
     * writing the archive to a file without having the characters be
     * interpreted by the writer.
     * 
     * @return The archive image.
     */
    public byte[] toByteArray()
    {
        return toString(Archive.RCS_NEWLINE).getBytes();
    }

    /**
     * Returns the path from the head node to the node identified by the given
     * version number.
     * 
     * @param vernum
     *            The version number that identifies the final node. Partial
     *            version numbers are OK.
     * @return The path to the node, or null if not found.
     */
    protected Path getRevisionPath(Version vernum)
    {
        if (head == null)
        {
            return null;
        }
        try
        {
            Path path = head.pathTo(vernum, true);
            Node revisionFound = path.last();
            if (revisionFound == null)
            {
                return null;
            }
            if (revisionFound.getVersion().isLessThan(vernum))
            {
                return null;
            }
            return path;
        }
        catch (NodeNotFoundException e)
        {
            return null;
        }
    }

    /**
     * Return the actual revision number of the node identified by the given
     * version number.
     * 
     * @param vernum
     *            The version number that identifies the node. Partial version
     *            numbers are OK.
     * @return The actual version, or null if a node is not found.
     */
    public Version getRevisionVersion(Version vernum)
    {
        Path path = getRevisionPath(vernum);
        return (path == null ? null : path.last().getVersion());
    }

    /**
     * Return the actual revision number of the node identified by the given
     * version number.
     * 
     * @param vernum
     *            The version number that identifies the node. Partial version
     *            numbers are OK.
     * @return The actual version, or null if a node is not found.
     */
    public Version getRevisionVersion(String vernum)
    {
        return getRevisionVersion(version(vernum));
    }

    /**
     * Return the actual revision number of the active revision. The revision
     * will be the tip of the branch identified as active, or the head revision
     * of the trunk if no branch is set as active.
     * 
     * @return The version number of the active revision, or null if there is
     *         none.
     */
    public Version getRevisionVersion()
    {
        if (branch != null)
        {
            return getRevisionVersion(branch);
        }
        else if (head != null)
        {
            return head.getVersion();
        }
        else
        {
            return null;
        }
    }

    /**
     * Append a text image of the archive to the given buffer using the given
     * token as line separator.
     * 
     * @param s
     *            where to append the image.
     * @param EOL
     *            the line separator.
     */
    public void toString(StringBuffer s, String EOL)
    {
        String EOI = ";" + EOL;
        String NLT = EOL + "\t";

        s.append("head");
        if (head != null)
        {
            s.append("\t");
            head.getVersion().toString(s);
        }
        s.append(EOI);

        if (branch != null)
        {
            s.append("branch\t");
            s.append(branch.toString());
            s.append(EOI);
        }

        s.append("access");
        for (Iterator i = users.iterator(); i.hasNext();)
        {
            s.append(EOL);
            s.append("\t");
            s.append(i.next());
        }
        s.append(EOI);

        s.append("symbols");
        for (Iterator i = symbols.entrySet().iterator(); i.hasNext();)
        {
            Map.Entry e = (Map.Entry) i.next();
            s.append(NLT);
            s.append(e.getKey().toString());
            s.append(":");
            s.append(e.getValue().toString());
        }
        s.append(EOI);

        s.append("locks");
        for (Iterator i = locked.iterator(); i.hasNext();)
        {
            String locker = ((Node) i.next()).getLocker();
            s.append(NLT);
            s.append(locker);
        }
        if (strictLocking)
        {
            s.append("; strict");
        }
        s.append(EOI);

        if (comment != null)
        {
            s.append("comment\t");
            s.append(Archive.quoteString(comment));
            s.append(EOI);
        }

        if (expand != null)
        {
            s.append("expand\t");
            s.append(Archive.quoteString(expand));
            s.append(EOI);
        }

        if (phrases != null)
        {
            phrases.toString(s, EOL);
        }
        s.append(EOL);

        for (Iterator i = nodes.values().iterator(); i.hasNext();)
        {
            Node n = (Node) i.next();
            if (!n.getVersion().isGhost() && n.getText() != null)
            {
                n.toString(s, EOL);
            }
        }

        s.append(EOL + EOL);
        s.append("desc");
        s.append(EOL);
        s.append(quoteString(desc));
        s.append(EOL);

        Node n = head;
        while (n != null)
        {
            n.toText(s, EOL);
            n = n.getRCSNext();
        }
    }

    /**
     * Quote a string. RCS strings are quoted using @. Any @ in the original
     * string is doubled to
     * @@.
     * @param s
     *            the string to quote.
     * @return The string quoted in RCS style.
     */
    static public String quoteString(String s)
    {
        StringBuffer result = new StringBuffer(s.replaceAll("@", "@@"));
        result.insert(0, '@');
        result.append('@');
        return result.toString();
    }

    /**
     * Unquote a string quoted in RCS style.
     * 
     * @param s
     *            the quoted string.
     * @return s the string unquoted.
     */
    static public String unquoteString(String s) throws ParseException
    {
        return unquoteString(s, true);
    }

    /**
     * Unquote a string quoted in RCS style.
     * 
     * @param s
     *            the quoted string.
     * @param removeExtremes
     * Determines if the enclosing @ quotes should be removed.
     * @return s the string unquoted.
     */
    static public String unquoteString(String s, boolean removeExtremes)
            throws ParseException
    {
        if (removeExtremes)
        {
            if (s.charAt(0) != '@' || s.charAt(s.length() - 1) != '@')
                throw new ParseException("bad quoted string:" + s);
            s = s.substring(1, s.length() - 1);
        }
        Pattern pat = Pattern.compile("@@");
        return pat.matcher(s).replaceAll("@");
    }

    /**
     * Get the text belonging to the head revision.
     * 
     * @return The text of the head revision
     * @throws NodeNotFoundException
     *             if the revision could not be found.
     * @throws InvalidFileFormatException
     *             if any of the deltas cannot be parsed.
     * @throws PatchFailedException
     *             if any of the deltas could not be applied
     */
    public Object[] getRevision() throws InvalidFileFormatException,
            PatchFailedException, NodeNotFoundException
    {
        return getRevision(false);
    }

    /**
     * Get the text belonging to the head revision. Set annotate to true to have
     * the lines be annotated with the number of the revision in which they were
     * added or changed.
     * 
     * @param annotate
     *            set to true to have the text be annotated
     * @return The text of the head revision
     * @throws NodeNotFoundException
     *             if the revision could not be found.
     * @throws InvalidFileFormatException
     *             if any of the deltas cannot be parsed.
     * @throws PatchFailedException
     *             if any of the deltas could not be applied to produce a new
     *             revision.
     */
    public Object[] getRevision(boolean annotate)
            throws InvalidFileFormatException, PatchFailedException,
            NodeNotFoundException
    {
        if (branch != null)
        {
            return getRevision(branch);
        }
        else if (head != null)
        {
            return getRevision(head.getVersion());
        }
        else
        {
            throw new IllegalStateException("no head node");
        }
    }

    /**
     * Get the text belonging to the revision identified by the given version
     * number. Partial version numbers are OK.
     * 
     * @param vernum
     *            the version number.
     * @return The text of the revision if found.
     * @throws InvalidVersionNumberException
     *             if the version number cannot be parsed.
     * @throws NodeNotFoundException
     *             if the revision could not be found.
     * @throws InvalidFileFormatException
     *             if any of the deltas cannot be parsed.
     * @throws PatchFailedException
     *             if any of the deltas could not be applied
     */
    public Object[] getRevision(String vernum)
            throws InvalidFileFormatException, PatchFailedException,
            InvalidVersionNumberException, NodeNotFoundException
    {
        return getRevision(vernum, false);
    }

    /**
     * Get the text belonging to the revision identified by the given version
     * number. Partial version numbers are OK.
     * 
     * @param vernum
     *            the version number.
     * @param expansion
     *            the desired keyword expansion mode
     * @return The text of the revision if found.
     * @throws InvalidVersionNumberException
     *             if the version number cannot be parsed.
     * @throws NodeNotFoundException
     *             if the revision could not be found.
     * @throws InvalidFileFormatException
     *             if any of the deltas cannot be parsed.
     * @throws PatchFailedException
     *             if any of the deltas could not be applied
     */
    public Object[] getRevision(String vernum, String expansion)
            throws InvalidFileFormatException, PatchFailedException,
            InvalidVersionNumberException, NodeNotFoundException
    {
        return getRevision(vernum, expansion, false);
    }

    /**
     * Get the text belonging to the revision identified by the given version
     * number. Partial version numbers are OK. Set annotate to true to have the
     * lines be annotated with the number of the revision in which they were
     * added or changed.
     * 
     * @param vernum
     *            the version number.
     * @param annotate
     *            set to true to have the text be annotated
     * @return The text of the revision if found.
     * @throws InvalidVersionNumberException
     *             if the version number cannot be parsed.
     * @throws NodeNotFoundException
     *             if the revision could not be found.
     * @throws InvalidFileFormatException
     *             if any of the deltas cannot be parsed.
     * @throws PatchFailedException
     *             if any of the deltas could not be applied
     */
    public Object[] getRevision(String vernum, boolean annotate)
            throws InvalidVersionNumberException, NodeNotFoundException,
            InvalidFileFormatException, PatchFailedException
    {
        return getRevision(version(vernum), annotate);
    }

    /**
     * Get the text belonging to the revision identified by the given version
     * number. Partial version numbers are OK. Set annotate to true to have the
     * lines be annotated with the number of the revision in which they were
     * added or changed.
     * 
     * @param vernum
     *            the version number.
     * @param expansion
     *            the desired keyword expansion mode
     * @param annotate
     *            set to true to have the text be annotated
     * @return The text of the revision if found.
     * @throws InvalidVersionNumberException
     *             if the version number cannot be parsed.
     * @throws NodeNotFoundException
     *             if the revision could not be found.
     * @throws InvalidFileFormatException
     *             if any of the deltas cannot be parsed.
     * @throws PatchFailedException
     *             if any of the deltas could not be applied
     */
    public Object[] getRevision(String vernum, String expansion,
            boolean annotate) throws InvalidVersionNumberException,
            NodeNotFoundException, InvalidFileFormatException,
            PatchFailedException
    {
        return getRevision(version(vernum), expansion, annotate);
    }

    /**
     * Get the text belonging to the revision identified by the given version
     * number. Partial version numbers are OK.
     * 
     * @param vernum
     *            the version number.
     * @return The text of the revision if found.
     * @throws NodeNotFoundException
     *             if the revision could not be found.
     * @throws InvalidFileFormatException
     *             if any of the deltas cannot be parsed.
     * @throws PatchFailedException
     *             if any of the deltas could not be applied
     */
    public Object[] getRevision(Version vernum)
            throws InvalidFileFormatException, PatchFailedException,
            NodeNotFoundException
    {
        return getRevision(vernum, false);
    }

    /**
     * Get the text belonging to the revision identified by the given version
     * number. Partial version numbers are OK. Set annotate to true to have the
     * lines be annotated with the number of the revision in which they were
     * added or changed.
     * 
     * @param vernum
     *            the version number.
     * @param annotate
     *            set to true to have the text be annotated
     * @return The text of the revision if found.
     * @throws NodeNotFoundException
     *             if the revision could not be found.
     * @throws InvalidFileFormatException
     *             if any of the deltas cannot be parsed.
     * @throws PatchFailedException
     *             if any of the deltas could not be applied
     */
    public Object[] getRevision(Version vernum, boolean annotate)
            throws InvalidFileFormatException, PatchFailedException,
            NodeNotFoundException
    {
        return getRevision(vernum, getExpand(), annotate);
    }

    /**
     * Get the text belonging to the revision identified by the given version
     * number. Partial version numbers are OK. Set annotate to true to have the
     * lines be annotated with the number of the revision in which they were
     * added or changed.
     * 
     * @param vernum
     *            the version number.
     * @param expansion
     *            the desired keyword expansion mode
     * @param annotate
     *            set to true to have the text be annotated
     * @return The text of the revision if found.
     * @throws NodeNotFoundException
     *             if the revision could not be found.
     * @throws InvalidFileFormatException
     *             if any of the deltas cannot be parsed.
     * @throws PatchFailedException
     *             if any of the deltas could not be applied
     */
    public Object[] getRevision(Version vernum, String expansion,
            boolean annotate) throws InvalidFileFormatException,
            PatchFailedException, NodeNotFoundException
    {
        Path path = getRevisionPath(vernum);
        if (path == null)
        {
            throw new NodeNotFoundException(vernum);
        }

        if (EXP_Binary.equals(expansion))
        {
            return path.last().mergedText();
        }

        Lines lines = new Lines();
        Node revisionFound = path.last();
        path.patch(lines, annotate);

        return doKeywords(lines.toArray(), revisionFound, expansion);
    }

    /**
     * Add the given revision to the active branch on the archive.
     * 
     * @param text
     *            the text of the revision.
     * @param log
     *            the log: a short note explaining what the revision is.
     * @return The version number assigned to the revision.
     */
    public Version addRevision(Object[] text, String log)
            throws InvalidFileFormatException, DiffException,
            InvalidVersionNumberException, NodeNotFoundException
    {
        if (branch != null)
        {
            return addRevision(text, branch, log);
        }
        else
        {
            return addRevision(text, head.getVersion().next(), log);
        }
    }

    /**
     * Add the given revision to the the archive using the given version number.
     * The version number may be partial. If so, the rules used by RCS/CVS are
     * used to decide which branch the revision should be added to. A new branch
     * may be created if required.
     * 
     * @param text
     *            the text of the revision.
     * @param vernum
     *            is the version number wanted, or, if partial, identifies the
     *            target branch.
     * @param log
     *            the log: a short note explaining what the revision is.
     * @return The version number assigned to the revision.
     */
    public Version addRevision(Object[] text, String vernum, String log)
            throws InvalidFileFormatException, DiffException,
            InvalidVersionNumberException, NodeNotFoundException
    {
        return addRevision(text, version(vernum), log);
    }

    /**
     * Add the given revision to the the archive using the given version number.
     * The version number may be partial. If so, the rules used by RCS/CVS are
     * used to decide which branch the revision should be added to. A new branch
     * may be created if required.
     * 
     * @param text
     *            the text of the revision.
     * @param vernum
     *            is the version number wanted, or, if partial, identifies the
     *            target branch.
     * @param log
     *            the log: a short note explaining what the revision is.
     * @return The version number assigned to the revision.
     */
    public Version addRevision(Object[] text, Version vernum, String log)
            throws InvalidFileFormatException, DiffException,
            NodeNotFoundException, InvalidVersionNumberException
    {
        if (head == null)
        {
            throw new IllegalStateException("no head node");
        }

        Path path = head.pathTo(vernum, true);
        Node target = path.last();

        if (vernum.size() < target.getVersion().size())
        {
            vernum = target.nextVersion();
        }
        else if (!vernum.isGreaterThan(target.getVersion()))
        {
            throw new InvalidVersionNumberException(vernum
                    + " revision must be higher than " + target.getVersion());
        }
        else if (vernum.odd())
        {
            if (vernum.last() == 0)
            {
                vernum = target.newBranchVersion();
            }
            else
            {
                vernum = vernum.newBranch(1);
            }
        }
        else if (vernum.last() == 0)
        {
            vernum = vernum.next();
        }

        boolean headAdd = (target == head && !vernum.isBranch());

        text = removeKeywords(text);
        String deltaText = null;
        if (!isBinary())
        {
            if (headAdd)
            {
                deltaText = Diff.diff(text, head.getText()).toRCSString(
                        RCS_NEWLINE);
            }
            else
            {
                Object[] oldText = path.patch().toArray();
                deltaText = Diff.diff(oldText, text).toRCSString(RCS_NEWLINE);
            }
            if (deltaText.length() == 0)
            {
                return null;
            } // no changes, no new version
        }

        Node newNode = null;
        if (headAdd)
        {
            newNode = newNode(vernum, head);
            newNode.setText(text);
            if (!isBinary())
            {
                head.setText(deltaText);
            }
            head = (TrunkNode) newNode;
        }
        else
        { // adding a branch node
            newNode = newNode(vernum);
            if (!isBinary())
            {
                newNode.setText(deltaText);
            }
            else
            {
                newNode.setText(text);
            }
            if (vernum.size() > target.getVersion().size())
            {
                target.addBranch((BranchNode) newNode);
            }
            else
            {
                target.setRCSNext(newNode);
            }
        }
        newNode.setLog(log);
        return newNode.getVersion();
    }

    /**
     * Returns the given text with values added to CVS-style keywords.
     * 
     * @param text
     *            the text on which substitutions will be applied.
     * @param rev
     *            a node that identifies the revision to which the given text
     *            belongs.
     * @param expansion
     * @return the text with substitutions performed.
     */
    public Object[] doKeywords(Object[] text, Node rev, String expansion)
            throws PatchFailedException
    {
        if (EXP_Binary.equals(expansion)
                || EXP_PreserveOriginal.equals(expansion))
            return text;

        // !!! this is used specifically for the way
        // !!! in which the keyword replacer works. Should be moved there.
        // !!! Write a Format.format(Object[], Node rev) instead.
        Object[] revisionInfo = new Object[]
        {
                filename,
                new File(filename).getName(),
                rev.getVersion().toString(),
                rev.getDate(),
                rev.getAuthor(),
                rev.getState(),
                rev.getLocker() };

        KeywordFormatter formatter;
        if (EXP_KeywordOnly.equals(expansion))
            formatter = KeywordFormatter.getKeywordOnlyFormatter();
        if (EXP_ValueOnly.equals(expansion))
            formatter = KeywordFormatter.getValueOnlyFormatter();
        formatter = KeywordFormatter.getKeywordAndValueFormatter();

        Object[] result = new Object[text.length];
        for (int i = 0; i < text.length; i++)
        {
            result[i] = formatter.update(text[i].toString(), revisionInfo);
        }
        return result;
    }

    /**
     * Returns the given text removing the values of any CVS-style keywords.
     * 
     * @param text
     *            the text on which substitutions will be applied.
     * @return the text with substitutions performed.
     */
    protected static Object[] removeKeywords(Object[] text)
            throws PatchFailedException
    {
        Object[] result = new Object[text.length];
        for (int i = 0; i < text.length; i++)
        {
            result[i] = KeywordFormatter.getKeywordAndValueFormatter().reset(
                    text[i].toString());
        }
        return result;
    }

    /**
     * Return the list of nodes between the head revision and the root revision.
     */
    public Node[] changeLog()
    {
        return changeLog(head.version);
    }

    /**
     * Return the list of nodes between the the given revision and the root
     * revision.
     * 
     * @param latest
     *            the version of the last revision in the log.
     */
    public Node[] changeLog(Version latest)
    {
        return changeLog(latest, head.root().version);
    }

    /**
     * Return the list of nodes between the the given two revisions.
     * 
     * @param latest
     *            the version of the last revision in the log.
     * @param earliest
     *            the version of the first revision in the log.
     */
    public Node[] changeLog(Version latest, Version earliest)
    {
        Node last = findNode(latest);
        if (last == null)
        {
            throw new NodeNotFoundException(latest.toString());
        }

        Node first = findNode(earliest);
        if (first == null)
        {
            throw new NodeNotFoundException(earliest.toString());
        }

        List result = new LinkedList();

        Node node = last;
        while (node != null)
        {
            result.add(0, node);
            if (node == first)
            {
                break;
            }
            node = node.getParent();
        }

        if (node == null)
        {
            throw new NodeNotFoundException(earliest.toString());
        }

        return (Node[]) result.toArray(new Node[result.size()]);
    }

    /**
     * Returns the description associated with the archive.
     * 
     * @return the description
     */
    public String getDesc()
    {
        return desc;
    }

    /**
     * Returns the log message associated with the given revision.
     * 
     * @param version -
     *            the version to get the log message for
     * @return the log message for the version.
     * @exception -
     *                if the version does not exist for the archive.
     */
    public String getLog(Version version) throws NodeNotFoundException
    {
        Node node = this.findNode(version);
        if (node == null)
        {
            throw new NodeNotFoundException("There's no version " + version);
        }
        return node.getLog();
    }

    /**
     * Returns the log message associated with the given revision.
     * 
     * @param version -
     *            the version to get the log message for
     * @return the log message for the version.
     * @exception -
     *                if the version does not exist for the archive.
     */
    public String getLog(String vernum) throws InvalidVersionNumberException,
            NodeNotFoundException
    {
        return getLog(version(vernum));
    }

    /**
     * Removes the tip revision from the archive. The current branch is taken
     * into account.
     * 
     * @throws NodeNotFoundException
     * @throws InvalidFileFormatException
     * @throws PatchFailedException
     * @throws DifferentiationFailedException
     */
    public void Remove() throws NodeNotFoundException,
            InvalidFileFormatException, PatchFailedException,
            DifferentiationFailedException
    {
        if (nodes.size() == 1)
            throw new UnsupportedOperationException(
                    "attempt to delete all revisions");
        Version v = getRevisionVersion();
        Node top = getNode(v);
        // Node prev = top.getParent();
        // if (v.isTrunk())
        // {
        // Object[] text = getRevision(prev.version, false);
        // prev.setText(text);
        // head = (TrunkNode) prev;
        // }
        // else if (prev.version.isBranch())
        // branch = prev.version;
        // else
        // {
        // branch = null;
        // }

        removeNode(top);
    }

    /**
     * Removes the revision identified by the provided version string.
     * 
     * @param vernum
     *            the version string
     * @throws NodeNotFoundException
     * @throws InvalidFileFormatException
     * @throws PatchFailedException
     * @throws InvalidVersionNumberException
     * @throws DifferentiationFailedException
     */
    public void Remove(String vernum) throws NodeNotFoundException,
            InvalidFileFormatException, PatchFailedException,
            InvalidVersionNumberException, DifferentiationFailedException
    {
        Remove(version(vernum));
    }

    /**
     * Removes the revision identified by the given {@link Version}
     * 
     * @param version
     *            the version object
     * 
     * @throws NodeNotFoundException
     * @throws InvalidFileFormatException
     * @throws PatchFailedException
     * @throws DifferentiationFailedException
     */
    public void Remove(Version version) throws NodeNotFoundException,
            InvalidFileFormatException, PatchFailedException,
            DifferentiationFailedException
    {
        Node target = getNode(version);
        /*
         * if (target.version.equals(getRevisionVersion())) { Remove(); } else {
         * Node next = target.getChild(); Node prev = target.getParent();
         * Object[] nextTxt = getRevision(next.version); Object[] prevTxt =
         * getRevision(prev.version);
         * 
         * if (!target.version.isBranch()) { String deltaText =
         * Diff.diff(nextTxt, prevTxt).toRCSString( RCS_NEWLINE);
         * prev.setText(deltaText); } else { String deltaText =
         * Diff.diff(prevTxt, nextTxt).toRCSString( RCS_NEWLINE);
         * next.setText(deltaText); } }
         */
        removeNode(target);
    }

    /**
     * Removes a node from the archive.
     * 
     * @param node
     *            the node to remove.
     * @throws NodeNotFoundException
     * @throws InvalidFileFormatException
     * @throws PatchFailedException
     * @throws DifferentiationFailedException
     */
    private void removeNode(Node node) throws NodeNotFoundException,
            InvalidFileFormatException, PatchFailedException,
            DifferentiationFailedException
    {
        if (nodes.size() == 1)
            throw new UnsupportedOperationException(
                    "attempt to delete all revisions");
        Node parent = node.getParent();
        Node child = node.getChild();

        if (node.version.isBranch() && child == null)
        {
            parent.removeBranch(node);
            parent.setRCSNext(null);
        }
        else if (head == node)
        {
            Object[] text = getRevision(parent.version, false);
            parent.setText(text);
            head = (TrunkNode) parent;
        }
        else
        {
            Object[] parentTxt = getRevision(parent.version);
            Object[] childTxt = getRevision(child.version);
            if (node.version.isTrunk())
            {
                String deltaText = Diff.diff(childTxt, parentTxt).toRCSString(
                        RCS_NEWLINE);
                parent.setText(deltaText);
            }
            else
            {
                String deltaText = Diff.diff(parentTxt, childTxt).toRCSString(
                        RCS_NEWLINE);
                child.setText(deltaText);
                parent.removeBranch(node);
                parent.setRCSNext(child);
            }
        }
        nodes.remove(node.version);
    }

}
