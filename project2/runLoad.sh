#!/bin/bash

mysql CS144 < ./drop.sql

mysql CS144 < ./create.sql

ant
ant run-all

for f in ./*.dat
do
    cp $f ./tmp.dat
    sort --output=./tmp.dat ./tmp.dat
    uniq ./tmp.dat > $f
done

mysql CS144 < ./load.sql

rm -rf ./tmp.dat
