(ns smeagol.contrib.util
  (:require [clojure.string :as string]
            [markdown.core :as md]))

(def ^:const filepath "content/")

(def ^:const extension ".md")

(defn- md->html
  "reads a markdown file and returns an HTML string"
  [filename]
  (try
    (md/md-to-html-string (slurp filename) :parse-meta? true) ; metadata parsed but not returned
    (catch Exception e)))

(defn- md->html-with-meta
  "reads a markdown file and returns {:html 'foo' :metadata {:author ['bar']}}}"
  [filename]
  (try
    (assoc (md/md-to-html-string-with-meta (slurp filename)) :filepathname filename ) 
    (catch Exception e)))

(defn camel-caseify
     "converts hi-there to HiThere, leaves HiThere alone"
     [s]
     (cond (not (string/includes? s "-")) s
           :else                           (apply str (map string/capitalize (string/split s #"-")))) )

(defn- make-filename [article-name] (str filepath article-name extension))

(defn- read-file-camelfallback [article-name f]
  (if-let [article (f (make-filename article-name))] article
          (f (make-filename (camel-caseify article-name)))))

(defn- list-articles []
  (filter #(.endsWith (.getName %) extension)
          (file-seq (clojure.java.io/file filepath))))

;; this is an example of passing in a predicate
(comment
  (fetch-articles (fn [x] (not (nil? (:date (:metadata x)))))) )

(defn fetch-html
  "Given a Markdown filename, returns HTML string."
  [article-name]
  (read-file-camelfallback article-name (partial md->html)))

(defn- remove-path [f]
  (let [path-or-file (string/split f (re-pattern filepath))]
    (cond (empty? (first path-or-file)) (second path-or-file)
          :else                         (first path-or-file))
    ))

(defn- remove-extension [f] (first (string/split f (re-pattern extension))))

(defn fetch-article
  "Given a Markdown filename, returns {:html '<p>foo</p>' :metadata {:author 'Bar'}} :filepathname 'content/Foo.md' :basefilename 'Foo.md' :basename 'Foo'"
  [article-name]
  (if-let [article (read-file-camelfallback article-name (partial md->html-with-meta))]
    (-> article
        (assoc :basefilename (remove-path (:filepathname article)))
        (assoc :basename     (remove-path (remove-extension (:filepathname article))))
        (assoc :articlename  article-name)
        )
    {:articlename article-name}))

(defn fetch-articles
  ([]     (map fetch-article (map #(remove-extension (.getName %)) (list-articles))))
  ([pred] (filter pred (fetch-articles))))