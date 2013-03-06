(load-file "src/whimrepl/version.clj")

(defproject lein-whimrepl whimrepl.version/string
  :description "Integrate vim-slime with a Leiningen REPL (on Win32)."
  :url "https://github.com/malyn/lein-whimrepl"
  :license {:name "BSD"
            :url "http://www.opensource.org/licenses/BSD-3-Clause"
            :distribution :repo }

  :dependencies [[com.michaelalynmiller/jnaplatext "1.0.0"]
                 [com.michaelalynmiller/vimserver "1.0.0"]]

  :profiles {:dev {:dependencies [[leiningen "2.0.0"]
                                  [speclj "2.5.0"]]}}
  :plugins [[speclj "2.5.0"]]
  :test-paths ["spec"]

  :eval-in-leiningen true)
