**JRCS** is a library that knows how to manipulate the archive files produced by the [RCS][1] and [CVS][2] version control systems. **JRCS** is not intended to replace neither tool. **JRCS** was written to be able create archive analysis tools that can do things like identify hot spots in the source code, measure the contributions by each developer, o assess how bugs make it in.

[1]: http://en.wikipedia.org/wiki/Revision_Control_System
[2]: http://en.wikipedia.org/wiki/Concurrent_Versions_System

The reasons why **JRCS** has the ability do do check-ins and save archives is API symmetry, and to simplify the writing of unit tests.

The [diff](https://github.com/tarzanek/jrcs/tree/master/src/java/org/suigeneris/jrcs/diff) package implements the differencing engine that **JRCS** uses. The engine has the power of Unix [diff](http://en.wikipedia.org/wiki/Diff_utility), is simple to understand, and can be used independently of the archive handling functionality. The entry point to the differencing engine is class [org.suigeneris.jrcs.diff.Diff](https://github.com/tarzanek/jrcs/blob/master/src/java/org/suigeneris/jrcs/diff/Diff.java).

The [rcs](https://github.com/tarzanek/jrcs/tree/master/src/java/org/suigeneris/jrcs/rcs) package implements the archive handling functionality. The entry point to the library is class [org.suigeneris.jrcs.rcs.Archive](https://github.com/tarzanek/jrcs/blob/master/src/java/org/suigeneris/jrcs/rcs/Archive.java).

## CAVEAT UTILITOR

*Do not make modifications to your archives with JRCS. There needs to be an important amount of additional testing before it's safe to do that.*
