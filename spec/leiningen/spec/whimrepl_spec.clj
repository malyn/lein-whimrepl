(ns leiningen.spec.whimrepl-spec
  (:use [speclj.core]
        [leiningen.whimrepl])
  (:require [leiningen.repl :as lein.repl]))

(describe "replname?"
  (it "'nil' is an invalid replname"
    (should-not (replname? nil)))
  (it "the empty string is an invalid replname"
    (should-not (replname? "")))
  (it "the word 'foo' is a valid replname"
    (should (replname? "foo")))
  (it "a keyword string like ':foo' is not a valid replname"
    (should-not (replname? ":foo"))))

(describe "split-replname-from-args"
  (it "handles the standard case of no args"
    (should=
      [nil []]
      (split-replname-from-args [])))
  (it "as well as advanced scenarios such as the :connect arg"
    (should=
      [nil [":connect" 12345]]
      (split-replname-from-args [":connect" 12345])))

  (it "returns just the replname if given only a replname in the args"
    (should=
      ["foo" []]
      (split-replname-from-args ["foo"])))
  (it "returns the replname and args if given both things"
    (should=
      ["foo" [":connect" 12345]]
      (split-replname-from-args ["foo" ":connect" 12345]))))

(describe "should-start-whimrepl?"
  (it "returns true if given an empty arg list"
    (should (should-start-whimrepl? [])))
  (it "returns true if given the ':connect' arg"
    (should (should-start-whimrepl? [":connect" 12345])))
  (it "returns false if given the ':headless' arg"
    (should-not (should-start-whimrepl? [":headless" 12345])))
  (it "fails-safe (returns false) if given a new, unknown arg"
    (should-not (should-start-whimrepl? [":bestnewarg" 27]))))

(describe "whimrepl"
  (with-all fakeproj {:name "fake"})

  (with replname (atom nil))
  (with replargs (atom nil))

  (around [it]
    (with-redefs [start-whimrepl #(reset! @replname %)
                  lein.repl/repl (fn [& args] (reset! @replargs args))]
      (it)))

  (describe "outside of a project"
    (it "should use the default name in the standard case of no args"
      (whimrepl nil)
      (should= "whimrepl" @@replname)
      (should= [nil] @@replargs))
    (it "should use the default name if given only 'lein repl' args"
      (whimrepl nil ":connect" 12345)
      (should= "whimrepl" @@replname)
      (should= [nil ":connect" 12345] @@replargs))

    (it "should use the specified name if given only that name as an arg"
      (whimrepl nil "foo")
      (should= "foo" @@replname)
      (should= [nil] @@replargs))
    (it "should use the specified name and 'lein repl' args"
      (whimrepl nil "foo" ":connect" 12345)
      (should= "foo" @@replname)
      (should= [nil ":connect" 12345] @@replargs))

    (it "should not start whimrepl in :headless mode"
      (whimrepl nil ":headless" 12345)
      (should= nil @@replname)
      (should= [nil ":headless" 12345] @@replargs)))

  (describe "in a project"
    (it "should use the project name in the standard case of no args"
      (whimrepl @fakeproj)
      (should= (:name @fakeproj) @@replname)
      (should= [@fakeproj] @@replargs))
    (it "should use the project name if given only 'lein repl' args"
      (whimrepl @fakeproj ":connect" 12345)
      (should= (:name @fakeproj) @@replname)
      (should= [@fakeproj ":connect" 12345] @@replargs))

    (it "should use the specified name if given only that name as an arg"
      (whimrepl @fakeproj "foo")
      (should= "foo" @@replname)
      (should= [@fakeproj] @@replargs))
    (it "should use the specified name and 'lein repl' args"
      (whimrepl @fakeproj "foo" ":connect" 12345)
      (should= "foo" @@replname)
      (should= [@fakeproj ":connect" 12345] @@replargs))

    (it "should not start whimrepl in :headless mode"
      (whimrepl @fakeproj ":headless" 12345)
      (should= nil @@replname)
      (should= [@fakeproj ":headless" 12345] @@replargs))))
