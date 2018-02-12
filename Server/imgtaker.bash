#!/bin/bash
#uses webcrawler to download the first image result for the word given as an arg

part1='http://www.webcrawler.com/serp?qc=images&q=';

website=$part1$1;

echo $website;
wget -O url.txt $website;

cat url.txt | grep -Eo "(http|https)://[a-zA-Z0-9./?=_-]*.jpg" | head -n 1 > link.txt;

wget -i link.txt -O image.jpg;

rm link.txt;
rm url.txt;

