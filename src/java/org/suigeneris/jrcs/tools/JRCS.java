/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: JRCS.java,v 1.3 2006/06/08 05:28:34 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.tools;

import org.suigeneris.jrcs.diff.Diff;
import org.suigeneris.jrcs.diff.DiffHelper;
import org.suigeneris.jrcs.rcs.Archive;
import org.suigeneris.jrcs.rcs.Version;

/**
 * A tool to extract information from CVS/RCS archives.
 * <p>
 * This simple tool was built as a feature test of the
 * {@linkplain org.suigeneris.jrcs.rcs rcs} library.
 * </p>
 */
public class JRCS
{
    static Archive archive;
    static Object[] orig;

    static public void main(String[] args) throws Exception
    {
        // try {
        if (args.length > 2)
        {
            System.err.println("usage: ");
        }
        else
        {
            if (args.length >= 1)
            {
                archive = new Archive(args[0]);
            }
            else
            {
                archive = new Archive("test,v", System.in);
            }
            System.err.println();
            System.err.println("==========================================");
            // System.out.println(archive.toString());
            // System.out.println(archive.getRevision("5.2.7.1"));
            // System.out.println(archive.getRevision(args[1], true));
            // archive.addRevision(new Object[]{}, args[1]);
            orig = archive.getRevision("1.3");
            System.out.println("*-orig-*********");
            System.out.print(Diff.arrayToString(orig));
            System.out.println("**********");
            // !! commented out because of package access error (jvz).
            // Object[] other = archive.getRevision("1.2");
            // System.out.println(Diff.diff(archive.removeKeywords(orig),
            // archive.removeKeywords(other)).toRCSString());
            trywith("1.2.3.1");
            trywith("1.2.3.5");
            trywith("1.2.3.1.");
            trywith("1.2");
            trywith("1.2.2");
            /*
             * trywith("1.2.2.2"); trywith("1.2.2.1.1"); trywith("1.2.2");
             * trywith("1.2.3"); trywith("1.2.3");
             */
        }
        /*
         * } catch(java.io.IOException e) {
         * System.out.println(e.getClass().toString());
         * System.out.println(e.getMessage()); e.printStackTrace(); }
         */
    }

    static int n = 1;

    static void trywith(String ver)
    {
        try
        {
            System.out.println();
            System.out.println("-------------");
            System.out.println("Adding " + ver);

            /*
             * List editor = new ArrayList(Arrays.asList(orig));
             * editor.subList(0,1).clear(); editor.add(0, "hola!"); Object[] rev =
             * editor.toArray();
             */
            Object[] rev = DiffHelper.randomEdit(orig, n++);
            // rev =
            // Diff.stringToArray(archive.doKeywords(Diff.arrayToString(rev),
            // null));
            // System.out.print(Archive.arrayToString(rev));

            Version newVer = archive.addRevision(rev, ver);
            System.out.println(newVer + " added");

            if (newVer != null)
            {
                // Object[] rec = archive.getRevision(newVer);
                // System.out.print(Archive.arrayToString(rec));

                /*
                 * !! commented out because of package access errors (jvz). if
                 * (!Diff.compare(archive.removeKeywords(rec),
                 * archive.removeKeywords(rev))) { System.out.println("revisions
                 * differ!"); System.out.println("**********");
                 * System.out.println(Diff.arrayToString(rec));
                 * System.out.println("**********");
                 * System.out.println(Diff.arrayToString(rev));
                 * System.out.println("**********");
                 * System.out.print(Diff.diff(rec, rev).toRCSString());
                 * System.out.println("**********"); }
                 */
            }
            System.out.println(ver + " OK");
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
