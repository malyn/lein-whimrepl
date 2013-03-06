(ns leiningen.whimrepl
  "Start a repl session in a Vim-targetable server."
  (:import (com.michaelalynmiller.jnaplatext.win32 CmdExeTyper ProcessUtils)
           (com.michaelalynmiller.vimserver IVimDataHandler VimServer))
  (:require [leiningen.repl :as lein.repl]
            [whimrepl.version]))

; List of 'lein repl' modes that are safe to use with whimrepl.
(def safe-leinrepl-modes [":connect"])

(defn should-start-whimrepl?
  "Returns true if the given 'lein repl' args are compatible with
  whimrepl (or nil), else false."
  [args]
  (let [flag (first args)]
    (or (nil? flag)
        (some (partial = flag) safe-leinrepl-modes))))

(defn replname?
  "Returns true if the given string is a valid whimrepl name,
  else false."
  [s]
  (and (seq s)
       (not= \: (first s))))

(defn split-replname-from-args
  "Returns a vector of [replname args] given the command line
  arguments to 'lein repl'.  replname will be nil if a whimrepl
  name was not provided on the command line."
  [args]
  (let [maybe-replname (first args)]
    (if (replname? maybe-replname)
      [maybe-replname (rest args)]
      [nil (vec args)])))

(defn find-cmdexe-pid
  "Returns the process id for our cmd.exe ancestor."
  []
  (->> (ProcessUtils/getProcessAncestors)
       (filter #(.endsWith (.getImageName %) "cmd.exe"))
       first
       .getProcessId))

(defn start-whimrepl
  "Starts a whimrepl server with the given target replname."
  [replname]
  (let [typer (CmdExeTyper. (find-cmdexe-pid))
        relay (proxy [IVimDataHandler] []
                (handleReceivedText [text] (.write typer text)))
        vimserver (VimServer. replname)]
    (.start vimserver relay)
    (printf "whimrepl %s available at %s\n" whimrepl.version/string replname)))

(defn ^:no-project-needed whimrepl
  "Start a repl session in a Vim-targetable server.

USAGE: lein whimrepl [lein repl args]

  This will start a whimrepl server and Leiningen REPL.  The whimrepl
  server will be given the same name as the current Leiningen project.
  If whimrepl was started outside of a Leiningen project then the
  default name of 'whimrepl' will be used.  Either way, the final name
  will be displayed at startup.

  Arguments to 'lein repl' may also be supplied and will be passed
  straight through without modification.  Note that whimrepl will only
  activate itself in 'lein repl' modes that are known to be compatible
  with the Vim server environment -- it wouldn't make sense to start
  whimrepl when using :headless mode, for example.

USAGE: lein whimrepl [replname] [lein repl args]

  Same as above, but the supplied replname will be used instead of the
  default name."
  [project & args]
  (let [[replname args] (split-replname-from-args args)
        replname (or replname (:name project) "whimrepl")]
    (if (should-start-whimrepl? args) (start-whimrepl replname))
    (apply lein.repl/repl project args)))
