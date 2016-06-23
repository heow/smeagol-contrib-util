(ns smeagol.contrib.util
  (:require [clojure.string :as string]
            [markdown.core :as md]))

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
    (md/md-to-html-string-with-meta (slurp filename)) 
    (catch Exception e)))

(defn camel-caseify
     "converts hi-there to HiThere"
     [cc]
     (apply str (map string/capitalize (string/split cc #"-"))) )

(defn- make-filename [article-name] (str "content/" article-name ".md"))

(defn- read-file-camelfallback [article-name fn]
  (if-let [article (fn (make-filename article-name))] article
          (fn (make-filename (camel-caseify article-name)))))

(defn- list-articles []
  (filter #(.endsWith (.getName %) ".md")
          (file-seq (clojure.java.io/file "./content"))))

(defn fetch-articles
  ([]     (map md->html-with-meta (list-articles)))
  ([pred] (filter pred (fetch-articles))))

;; this is an example of passing in a predicate
(comment
  (fetch-articles (fn [x] (not (nil? (:date (:metadata x)))))) )

(defn fetch-html
  "Given a Markdown filename, returns HTML string."
  [article-name]
  (read-file-camelfallback article-name (partial md->html)))

(defn fetch-article
  "Given a Markdown filename, returns {:title 'page-name' :html '<p>foo</p>' :metadata {:author 'Bar'}}"
  [article-name]
  (assoc (read-file-camelfallback article-name (partial md->html-with-meta))
    :title article-name ))

