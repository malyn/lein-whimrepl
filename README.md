# lein-whimrepl

A Leiningen plugin that makes it easy to send Clojure forms from the Win32 version of Vim to a Clojure REPL.  whimrepl uses [VimServer][] to listen for strings of text sent using [Vim's remote protocol][].  A [modified version][] of [vim-slime][] is then used to send text from Vim to the VimServer-wrapped Leiningen REPL.

[modified version]: https://github.com/malyn/vim-slime
[Vim's remote protocol]: http://vimdoc.sourceforge.net/htmldoc/remote.html
[vim-slime]: https://github.com/jpalardy/vim-slime
[VimServer]: https://github.com/malyn/vimserver

## Installation

Add the whimrepl plugin to your global Leiningen config (`%USERPROFILE%\.lein\profiles.clj`) like so:

```clojure
{:user {:plugins [[lein-whimrepl "1.0.0"]]}}
```

Add [vim-slime][] to your Vim config, most likely by way of [Pathogen][], and then configure vim-slime to use whimrepl in your `vimrc` file:

[Pathogen]: https://github.com/tpope/vim-pathogen

```vim
" Point vim-slime at whimrepl.
let g:slime_target = "whimrepl"
```

## Launching whimrepl

whimrepl wraps the underlying `lein repl` command and passes through all of the standard REPL options.  The easiest way to start whimrepl is just to type `lein whimrepl`:

    C:\Users\malyn\mycljproj> lein whimrepl
    whimrepl 1.0.0 available at mycljproj
    nREPL server started on port 54127
    REPL-y 0.1.6
    Clojure 1.4.0
        Exit: Control+D or (exit) or (quit)
    Commands: (user/help)
        Docs: (doc function-name-here)
            (find-doc "part-of-name-here")
    Source: (source function-name-here)
            (user/sourcery function-name-here)
    Javadoc: (javadoc java-object-or-class-here)
    Examples from clojuredocs.org: [clojuredocs or cdoc]
            (user/clojuredocs name-here)
            (user/clojuredocs "ns-here" "name-here")
    user=>

By default whimrepl will register itself as a Vim target with the name of your Leiningen project.  If you are not in a project directory then whimrepl will default to "whimrepl".  You can override the default by supplying a target at the end of the command line invocation:

    C:\Users\malyn\mycljproj> lein whimrepl replthis
    whimrepl 1.0.0 available at replthis
    nREPL server started on port 54172
    REPL-y 0.1.6
    Clojure 1.4.0
        Exit: Control+D or (exit) or (quit)
    Commands: (user/help)
        Docs: (doc function-name-here)
            (find-doc "part-of-name-here")
    Source: (source function-name-here)
            (user/sourcery function-name-here)
    Javadoc: (javadoc java-object-or-class-here)
    Examples from clojuredocs.org: [clojuredocs or cdoc]
            (user/clojuredocs name-here)
            (user/clojuredocs "ns-here" "name-here")
    user=>

Note that whimrepl is smart enough to ignore Leiningen's `:headless` option and will not start a whimrepl listener when that option is present.  On a related note, because whimrepl just wraps Leiningen's normal REPL, you can use whimrepl when connected to a remote REPL using `:connect`.

## Using whimrepl

whimrepl integrates with [vim-slime][], so you can send a Clojure form from Vim to your REPL with a simple Control-c, Control-c.
